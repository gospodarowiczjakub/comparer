package hello.service;

import hello.model.*;
import hello.model.db.DbClaim;
import hello.utils.CSVUtils;

import java.sql.SQLException;
import java.util.*;

public interface ReportsComparer {
    public List<ReportClaim> executeComparison();
    public List<DbClaim> getDbClaimCases(List<ReportUniqueClaim> uniqueClaims);
    public List<Optional<Order>> findOrdersByClaimNumber(String claimNumber);
    public List<Attachment> addAttachmentsByOrderId(String orderId);
    public List<ReportUniqueClaim> groupReportAttachmentsByClaim(List<ReportClaim> reportClaims);
    public Optional<Lead> findLeadsByOrderId(String orderId) throws SQLException;
    public DbClaim attachLeads(DbClaim dbClaim);
    public List<ReportClaim> equalizeModels(List<DbClaim> dbClaim);
    public List<ReportClaim> importCSVReport();
    public List<ReportClaim> compareSets(List<ReportClaim> dbClaims, List<ReportClaim> reportClaims);
}
