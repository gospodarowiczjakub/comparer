package hello.model;

import java.util.List;
import java.util.Objects;

public class EPSUniqueClaims {
    private String claimNumber;
    private String EPSNumber;
    private List<Attachment> attachments;

    public EPSUniqueClaims(String claimNumber, String EPSNumber, List<Attachment> attachments) {
        this.claimNumber = claimNumber;
        this.EPSNumber = EPSNumber;
        this.attachments = attachments;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public String getEPSNumber() {
        return EPSNumber;
    }

    public void setEPSNumber(String EPSNumber) {
        this.EPSNumber = EPSNumber;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }


    @Override
    public String toString() {
        return "EPSUniqueClaims{" +
                "claimNumber='" + claimNumber + '\'' +
                ", EPSNumber='" + EPSNumber + '\'' +
                ", attachments=" + attachments.toString() +
                '}';
    }
}
