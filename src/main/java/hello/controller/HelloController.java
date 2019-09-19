package hello.controller;

import hello.DependencyInjectionExample;
import hello.config.FileConfiguration;
import hello.model.*;
import hello.model.db.DataRepository;
import hello.model.db.DbClaim;
import hello.utils.CSVUtils;
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
        List<ReportClaim> reportClaims = importCSVReport();
        List<ReportUniqueClaim> reportUniqueClaims = groupReportAttachmentsByClaim(reportClaims);

        List<DbClaim> dbClaims = getDbClaimCases(reportUniqueClaims);

        for (DbClaim dbClaim : dbClaims)
            attachLeads(dbClaim);

        for (DbClaim dbClaim : dbClaims)
            for (Optional<Order> order : dbClaim.getOrders()) {
                List<Attachment> attachments = addAttachmentsByOrderId(order.get().getOrderId());
                order.get().setAttachments(attachments);
            }

        List<ReportUniqueClaim> dbUniqueClaims = equalizeModels(dbClaims);
        List<ReportUniqueClaim> losenAttachments = compareSets(dbUniqueClaims, reportUniqueClaims);
        saveResult(losenAttachments);
        return dependencyInjectionExample.getHelloValue();
    }

    private void saveResult(List<ReportUniqueClaim> losenAttachments) {
        CSVUtils.saveObjectList(losenAttachments);
    }

    private List<ReportUniqueClaim> compareSets(List<ReportUniqueClaim> dbClaims, List<ReportUniqueClaim> reportClaims) {
        List<ReportUniqueClaim> losenAttachments = new ArrayList<>();

        //reportClaims.removeAll(dbClaims);
        for (ReportUniqueClaim r : reportClaims) {
            Optional<ReportUniqueClaim> db = dbClaims.stream().filter(test -> test.getClaimNumber().equals(r.getClaimNumber())).findFirst();
            if (db.isPresent()) {
                    r.getAttachments().removeAll(db.get().getAttachments());
            }
        }

        for (ReportUniqueClaim r : reportClaims)
            LOGGER.info("Losen attachments: {}", r.getAttachments());
        return reportClaims;
    }

    private List<ReportUniqueClaim> equalizeModels(List<DbClaim> dbClaim) {
        List<ReportUniqueClaim> dbClaims = new ArrayList<>();
        for (DbClaim claim : dbClaim) {
            for (Optional<Order> o : claim.getOrders()) {
                dbClaims.add(new ReportUniqueClaim(claim.getClaimCaseNumber(), o.get().getLeadId(), o.get().getAttachments()));
            }
        }
        return dbClaims;
    }

    private DbClaim attachLeads(DbClaim dbClaim) {
        try {
            for (Optional<Order> o : dbClaim.getOrders()) {
                Optional<Lead> lead = findLeadsByOrderId(o.get().getOrderId());
                o.get().setLeadId(lead.isPresent() ? lead.get().getEPSLeadId() : null);
            }
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
        return dbClaim;
    }

    private List<DbClaim> getDbClaimCases(List<ReportUniqueClaim> uniqueClaims) {
        List<DbClaim> dbClaims = new ArrayList<>();
        for (ReportUniqueClaim uniqueClaim : uniqueClaims) {
            List<Optional<Order>> orders = findOrdersByClaimNumber(uniqueClaim.getClaimNumber());
            DbClaim claimCase = new DbClaim(uniqueClaim.getClaimNumber(), orders);
            dbClaims.add(claimCase);
            /*
            try {
                if (uniqueClaim.getAttachments().equals(claimCase.getOrders().get(0).get().getAttachments()))
                    LOGGER.debug("Attachments for {} matches", uniqueClaim.getClaimNumber());
                else
                    LOGGER.debug("Attachments for {} don't match", uniqueClaim.getClaimNumber());
            } catch (Exception e) {
                LOGGER.debug("No orders for claim {}", uniqueClaim.getClaimNumber());
            }*/
        }

        for (DbClaim claim : dbClaims)
            LOGGER.debug(claim.toString());

        return dbClaims;
    }

    private List<ReportUniqueClaim> groupReportAttachmentsByClaim(List<ReportClaim> reportClaims) {
        List<ReportUniqueClaim> uniqueClaims = new ArrayList<>();
        Map<String, List<Attachment>> groupedClaims = new HashMap<>();
        Map<String, String> leadsForOrders = new HashMap<>();
        for (ReportClaim e : reportClaims) {
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
            uniqueClaims.add(new ReportUniqueClaim(key, leadsForOrders.get(key), value));
        });

        return uniqueClaims;
    }

    public Optional<Lead> findLeadsByOrderId(String orderId) throws SQLException {
        Optional<Lead> lead = dataRepository.findLeadsByEkspertyzaOrderId(orderId);

        lead.ifPresent(res -> {
            LOGGER.debug("EPSLead for orderID {}: {}", orderId, res.toString());
        });

        return lead;
    }

    public List<Optional<Order>> findOrdersByClaimNumber(String claimNumber) {
        List<Optional<Order>> orders = dataRepository.findByClaimCaseNumber(claimNumber);
        for (Optional<Order> o : orders)
            o.ifPresent(ord -> {
                LOGGER.debug("Found  order for {}: {}", claimNumber, ord.toString());
            });
        return orders;
    }

    public List<Attachment> addAttachmentsByOrderId(String orderId) {
        List<Attachment> attachments = dataRepository.findAttachmentsByOrderId(orderId);
        for (Attachment a : attachments)
            LOGGER.debug("Attachment for orderId {}: {}", orderId, a.toString());
        return attachments;
    }

    public List<ReportClaim> importCSVReport() {

        List<ReportClaim> claims = CSVUtils.loadObjectList(ReportClaim.class, fileConfiguration.getFileName());

        LOGGER.info("{} rows successfully loaded from {} ", claims.size(), fileConfiguration.getFileName());
        for (ReportClaim claim : claims)
            LOGGER.debug("Loaded claims: {}", claim.toString());

        return claims;
    }

}
