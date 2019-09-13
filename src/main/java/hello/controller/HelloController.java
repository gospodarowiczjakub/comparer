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
    private AppConfiguration appConfiguration;
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

        List<Optional<Lead>> leads = findLeadsByOrderId(dbClaimCases.get(2).getClaimCaseNumber());
        findAttachmentsByOrderId(dbClaimCases.get(0).getOrders().get(0).get().getOrderId());

        return dependencyInjectionExample.getHelloValue();
    }

    private List<DbClaimCase> getDbClaimCases(List<EPSUniqueClaims> epsUniqueClaims){
        List<DbClaimCase> dbClaimCases = new ArrayList<>();
        for(EPSUniqueClaims uniqueClaim: epsUniqueClaims)
        {
            List<Optional<Order>> orders = findOrdersByClaimNumber(uniqueClaim.getClaimNumber());
            dbClaimCases.add(new DbClaimCase(uniqueClaim.getClaimNumber(), orders));
        }

        for(DbClaimCase claim: dbClaimCases)
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

    public List<Optional<Lead>> findLeadsByOrderId(String orderId) throws SQLException{
        List<Optional<Lead>> leads = dataRepository.findByEkspertyzaOrderId(orderId);
        for (Optional<Lead> l : leads)
            l.ifPresent(res -> {
                System.out.println("EPSLead: " + res.toString());
            });

        return leads;
    }

    public List<Optional<Order>> findOrdersByClaimNumber(String claimNumber) {

        List<Optional<Order>> orders = dataRepository.findByClaimCaseNumber(claimNumber);
        for (Optional<Order> o : orders)
            o.ifPresent(ord -> {
                System.out.println("OrderId: " + ord.toString());
            });
        return orders;
    }

    public List<Optional<DomainValue>> findAttachmentsByOrderId(String orderId) {
        List<Optional<DomainValue>> dom = dataRepository.findById(orderId);
        for (Optional<DomainValue> d : dom)
            d.ifPresent(storageFile -> {
                System.out.println("Storage file" + storageFile.toString());
            });
        return dom;
    }

    public List<EPSClaim> importCSVReport() {
        CSVDataLoader csvDataLoader = new CSVDataLoader();
        List<EPSClaim> claims = csvDataLoader.loadObjectList(EPSClaim.class, fileConfiguration.getFileName());

        LOGGER.info("{} rows successfully loaded from {} ", claims.size(), fileConfiguration.getFileName());
        for (EPSClaim claim : claims)
            LOGGER.info("Loaded claims: {}", claim.toString());

        return claims;
    }

}
