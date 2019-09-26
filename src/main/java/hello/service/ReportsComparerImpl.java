package hello.service;

import hello.config.FileConfiguration;
import hello.model.*;
import hello.model.db.DataRepository;
import hello.model.db.DbClaim;
import hello.utils.CSVUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;


@Service
public class ReportsComparerImpl implements ReportsComparer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportsComparerImpl.class);

    @Autowired
    @Qualifier("vigJdbcDataRepository")
    private DataRepository dataRepository;

    @Autowired
    private FileConfiguration fileConfiguration;

    public List<ReportClaim> executeComparison() {
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

        return compareSets(dbUniqueClaims, reportClaims);
    }

    public List<ReportClaim> importCSVReport() {

        List<ReportClaim> claims = CSVUtils.loadObjectList(ReportClaim.class, fileConfiguration.getFileName());

        LOGGER.info("{} rows successfully loaded from {} ", claims.size(), fileConfiguration.getFileName());
        for (ReportClaim claim : claims)
            LOGGER.info("Loaded claims: {}", claim.toString());

        return claims;
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

    public List<DbClaim> getDbClaimCases(List<ReportUniqueClaim> uniqueClaims) {
        List<DbClaim> dbClaims = new ArrayList<>();
        for (ReportUniqueClaim uniqueClaim : uniqueClaims) {
            List<Optional<Order>> orders = findOrdersByClaimNumber(uniqueClaim.getClaimNumber());
            DbClaim claimCase = new DbClaim(uniqueClaim.getClaimNumber(), orders);
            dbClaims.add(claimCase);
            /*
            try {
                if (uniqueClaim.getAttachments().equals(claimCase.getOrders().get(0).get().getAttachments()))
                    LOGGER.info("Attachments for {} matches", uniqueClaim.getClaimNumber());
                else
                    LOGGER.info("Attachments for {} don't match", uniqueClaim.getClaimNumber());
            } catch (Exception e) {
                LOGGER.info("No orders for claim {}", uniqueClaim.getClaimNumber());
            }*/
        }

        for (DbClaim claim : dbClaims)
            LOGGER.info(claim.toString());

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

    public List<Attachment> addAttachmentsByOrderId(String orderId) {
        List<Attachment> attachments = dataRepository.findAttachmentsByOrderId(orderId);
        for (Attachment a : attachments)
            LOGGER.info("Attachment for orderId {}: {}", orderId, a.toString());
        return attachments;
    }

    public List<Optional<Order>> findOrdersByClaimNumber(String claimNumber) {
        List<Optional<Order>> orders = dataRepository.findOrdersByClaimCaseNumber(claimNumber);
        for (Optional<Order> o : orders)
            o.ifPresent(ord -> {
                LOGGER.info("Found  order for {}: {}", claimNumber, ord.toString());
            });
        return orders;
    }

    public Optional<Lead> findLeadsByOrderId(String orderId) throws SQLException {
        Optional<Lead> lead = dataRepository.findLeadsByEkspertyzaOrderId(orderId);

        lead.ifPresent(res -> {
            LOGGER.info("EPSLead for orderID {}: {}", orderId, res.toString());
        });

        return lead;
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

    public List<ReportClaim> compareSets(List<ReportClaim> dbClaims, List<ReportClaim> reportClaims) {
        List<ReportClaim> lostAttachments = new ArrayList<>();

        for (ReportClaim report : reportClaims) {
            if (!dbClaims.contains(report)) {
                lostAttachments.add(report);
            }
        }

        for (ReportClaim la : lostAttachments)
            LOGGER.info("lost attachments: {}", la.toString());
        return lostAttachments;
    }
}
