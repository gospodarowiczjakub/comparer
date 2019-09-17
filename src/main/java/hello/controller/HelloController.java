package hello.controller;

import hello.DependencyInjectionExample;
import hello.config.AppConfiguration;
import hello.config.FileConfiguration;
import hello.model.*;
import hello.model.db.DataRepository;
import hello.model.db.DbClaimCase;
import hello.utils.CSVDataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.*;

@RestController
public class HelloController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);
    private final DependencyInjectionExample dependencyInjectionExample;

    @Autowired
    private FileConfiguration fileConfiguration;
    @Autowired
    @Qualifier("namedParameterJdbcDataRepository")
    private DataRepository dataRepository;

    public HelloController(DependencyInjectionExample dependencyInjectionExample) {
        this.dependencyInjectionExample = dependencyInjectionExample;
    }

    @RequestMapping("/")
    public String index() throws SQLException {
        List<EPSClaim> epsClaims = importCSVReport();
        List<EPSUniqueClaims> reportEpsUniqueClaims = groupReportAttachmentsByClaim(epsClaims);

        List<DbClaimCase> dbClaimCases = getDbClaimCases(reportEpsUniqueClaims);

        for (DbClaimCase dbClaimCase : dbClaimCases)
            addOrderParammeters(dbClaimCase);

        for (DbClaimCase dbClaimCase : dbClaimCases)
            for (Optional<Order> order : dbClaimCase.getOrders()) {
                List<Attachment> attachments = findAttachmentsByOrderId(order.get().getOrderId());
                order.get().setAttachments(attachments);
            }

        List<EPSUniqueClaims> dbUniqueClaims = equalizeModels(dbClaimCases);
        List<EPSUniqueClaims> losenAttachments = compareSets(dbUniqueClaims, reportEpsUniqueClaims);

        return dependencyInjectionExample.getHelloValue();
    }

    private List<EPSUniqueClaims> compareSets(List<EPSUniqueClaims> dbClaims, List<EPSUniqueClaims> reportClaims) {
        List<EPSUniqueClaims> losenAttachments = new ArrayList<>();

        reportClaims.removeAll(dbClaims);
        for(EPSUniqueClaims r: reportClaims)
        {
            Optional<EPSUniqueClaims> db = dbClaims.stream().filter(test -> test.getClaimNumber().equals(r.getClaimNumber())).findFirst();
            if(db.isPresent())
            {
                r.getAttachments().removeAll(db.get().getAttachments());
            }

        }

        for (EPSUniqueClaims r : reportClaims)
            LOGGER.info("Losen attachments: {}", r.toString());

        return reportClaims;
    }

    private List<EPSUniqueClaims> equalizeModels(List<DbClaimCase> dbClaimCase) {
        List<EPSUniqueClaims> dbClaims = new ArrayList<>();
        for (DbClaimCase cc : dbClaimCase) {
            for (Optional<Order> o : cc.getOrders()) {
                dbClaims.add(new EPSUniqueClaims(cc.getClaimCaseNumber(), o.get().getLeadId(), o.get().getAttachments()));
            }
        }
        return dbClaims;
    }

    private DbClaimCase addOrderParammeters(DbClaimCase dbClaimCase) {
        try {
            for (Optional<Order> o : dbClaimCase.getOrders()) {
                List<Optional<Lead>> leads = findLeadsByOrderId(o.get().getOrderId());
                o.get().setLeadId(leads.isEmpty() ? null:leads.get(0).get().getEPSLeadId());
            }
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
        return dbClaimCase;
    }

    private List<DbClaimCase> getDbClaimCases(List<EPSUniqueClaims> epsUniqueClaims) {
        List<DbClaimCase> dbClaimCases = new ArrayList<>();
        for (EPSUniqueClaims uniqueClaim : epsUniqueClaims) {
            List<Optional<Order>> orders = findOrdersByClaimNumber(uniqueClaim.getClaimNumber());
            DbClaimCase claimCase = new DbClaimCase(uniqueClaim.getClaimNumber(), orders);
            dbClaimCases.add(claimCase);

            try {
                if (uniqueClaim.getAttachments().equals(claimCase.getOrders().get(0).get().getAttachments()))
                    LOGGER.debug("Attacments for {} matches", uniqueClaim.getClaimNumber());
                else
                    LOGGER.debug("Attachments for {} don't match", uniqueClaim.getClaimNumber());
            } catch (Exception e) {
                LOGGER.debug("No orders for claim {}", uniqueClaim.getClaimNumber());
            }
        }

        for (DbClaimCase claim : dbClaimCases)
            LOGGER.info(claim.toString());

        return dbClaimCases;
    }

    private List<EPSUniqueClaims> groupReportAttachmentsByClaim(List<EPSClaim> epsClaims) {
        List<EPSUniqueClaims> epsUniqueClaims = new ArrayList<>();
        Map<String, List<Attachment>> groupedClaims = new HashMap<>();
        Map<String, String> leadsForOrders = new HashMap<>();
        for (EPSClaim e : epsClaims) {
            if (groupedClaims.containsKey(e.claimNumber))
                groupedClaims.get(e.claimNumber).add(new Attachment(e.attachmentNumber, e.attachmentName));
            else {
                ArrayList<Attachment> attachment = new ArrayList<>();
                attachment.add(new Attachment(e.attachmentNumber, e.attachmentName));
                groupedClaims.put(e.claimNumber, attachment);
                leadsForOrders.put(e.claimNumber, e.epsNumber);
            }
        }

        groupedClaims.forEach((key, value) -> {
            epsUniqueClaims.add(new EPSUniqueClaims(key, leadsForOrders.get(key), value));
        });

        return epsUniqueClaims;
    }

    public List<Optional<Lead>> findLeadsByOrderId(String orderId) throws SQLException {
        List<Optional<Lead>> leads = dataRepository.findByEkspertyzaOrderId(orderId);
        for (Optional<Lead> l : leads)
            l.ifPresent(res -> {
                LOGGER.debug("EPSLead for orderID {}: {}", orderId, res.toString());
            });

        return leads;
    }

    public List<Optional<Order>> findOrdersByClaimNumber(String claimNumber) {
        List<Optional<Order>> orders = dataRepository.findByClaimCaseNumber(claimNumber);
        for (Optional<Order> o : orders)
            o.ifPresent(ord -> {
                LOGGER.debug("Order for {}: {}", claimNumber, ord.toString());
            });
        return orders;
    }

    public List<Attachment> findAttachmentsByOrderId(String orderId) {
        List<Attachment> attachments = dataRepository.findById(orderId);
        for (Attachment a : attachments)
            LOGGER.debug("Attachment for orderId {}: {}", orderId, a.toString());
        return attachments;
    }

    public List<EPSClaim> importCSVReport() {
        CSVDataLoader csvDataLoader = new CSVDataLoader();
        List<EPSClaim> claims = csvDataLoader.loadObjectList(EPSClaim.class, fileConfiguration.getFileName());

        LOGGER.info("{} rows successfully loaded from {} ", claims.size(), fileConfiguration.getFileName());
        for (EPSClaim claim : claims)
            LOGGER.debug("Loaded claims: {}", claim.toString());

        return claims;
    }

}
