package hello.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="Order")
public class Order {
    private String orderId;
    private String claimCaseNumber;
    private String inspectionTypeId;


    public Order(String orderId, String claimCaseNumber, String inspectionTypeId) {
        this.orderId = orderId;
        this.claimCaseNumber = claimCaseNumber;
        this.inspectionTypeId = inspectionTypeId;
    }

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

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", claimCaseNumber='" + claimCaseNumber + '\'' +
                ", inspectionTypeId='" + inspectionTypeId + '\'' +
                '}';
    }
}
