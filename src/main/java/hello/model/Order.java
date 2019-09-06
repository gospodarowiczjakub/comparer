package hello.model;

public class Order {
    private String orderId;
    private String claimCaseNumber;
    private String inspectionTypeId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getClaimCaseNumber() {
        return claimCaseNumber;
    }

    public void setClaimCaseNumber(String claimCaseNumber) {
        this.claimCaseNumber = claimCaseNumber;
    }

    public String getInspectionTypeId() {
        return inspectionTypeId;
    }

    public void setInspectionTypeId(String inspectionTypeId) {
        this.inspectionTypeId = inspectionTypeId;
    }
}
