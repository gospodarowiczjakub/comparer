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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.*;

@Controller
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

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "compare", method = RequestMethod.GET)
    public ModelAndView compareReports(ModelAndView mav) throws SQLException {
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

        List<ReportClaim> dbUniqueClaims = equalizeModels(dbClaims);
        List<ReportClaim> losenAttachments = compareSets(dbUniqueClaims, reportClaims);
        mav.addObject("losenAttachments", losenAttachments);
        mav.addObject("inputReport", reportClaims);
        mav.setViewName("losenAttachments");
        return mav;
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    private void saveResult(@RequestParam(value = "losenAttachments") ArrayList<ReportClaim> losenAttachments, @RequestParam(defaultValue = "output") String filename, ModelAndView mav) {

        CSVUtils.saveObjectList(losenAttachments, filename);
    }

    @ModelAttribute(name = "losenAttachments")
    public List<ReportClaim> compareSets(@RequestParam List<ReportClaim> dbClaims,@RequestParam List<ReportClaim> reportClaims) {
        List<ReportClaim> losenAttachments = new ArrayList<>();

        for (ReportClaim report : reportClaims) {
            if (!dbClaims.contains(report)) {
                losenAttachments.add(report);
            }
        }

        for (ReportClaim la : losenAttachments)
            LOGGER.info("Losen attachments: {}", la.toString());
        return losenAttachments;
    }

    public List<ReportClaim> equalizeModels(List<DbClaim> dbClaim) {
        List<ReportClaim> dbClaims = new ArrayList<>();
        for (DbClaim claim : dbClaim) {
            for (Optional<Order> o : claim.getOrders()) {
                for (Attachment a : o.get().getAttachments())
                    dbClaims.add(new ReportClaim(claim.getClaimCaseNumber(), o.get().getLeadId(), a.getAttachmentNumber(), a.getFilename()));
            }
        }
        return dbClaims;
    }

    public DbClaim attachLeads(DbClaim dbClaim) {
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

    public List<DbClaim> getDbClaimCases(List<ReportUniqueClaim> uniqueClaims) {
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

    public List<ReportUniqueClaim> groupReportAttachmentsByClaim(List<ReportClaim> reportClaims) {
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
        List<Optional<Order>> orders = dataRepository.findOrdersByClaimCaseNumber(claimNumber);
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
