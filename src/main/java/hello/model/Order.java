package hello.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="Order")
public class Order {
    private String orderId;
    private String leadId;
    private String claimCaseNumber;
    private String inspectionTypeId;
    private List<Attachment> attachments;


    public Order(String orderId, String claimCaseNumber, String inspectionTypeId) {
        this.orderId = orderId;
        this.claimCaseNumber = claimCaseNumber;
        this.inspectionTypeId = inspectionTypeId;
    }

    public String getLeadId() {
        return leadId;
    }

    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
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
