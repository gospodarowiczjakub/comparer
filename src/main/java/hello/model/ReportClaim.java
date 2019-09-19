package hello.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReportClaim {

    public String claimNumber;
    public String epsNumber;
    public String attachmentNumber;
    public String attachmentName;
    public String sent;


    public ReportClaim(@JsonProperty("claimNumber") String claimNumber,
                       @JsonProperty("epsNumber") String epsNumber,
                       @JsonProperty("attachmentNumber") String attachmentNumber,
                       @JsonProperty("attachmentName") String attachmentName,
                       @JsonProperty("sent") String sent) {
        this.claimNumber = claimNumber;
        this.epsNumber = epsNumber;
        this.attachmentNumber = attachmentNumber;
        this.attachmentName = attachmentName;
        this.sent = sent;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public String getEpsNumber() {
        return epsNumber;
    }

    public void setEpsNumber(String epsNumber) {
        this.epsNumber = epsNumber;
    }

    public String getAttachmentNumber() {
        return attachmentNumber;
    }

    public void setAttachmentNumber(String attachmentNumber) {
        this.attachmentNumber = attachmentNumber;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }

    @Override
    public String toString() {
        return "EPSClaim{" +
                "claimNumber='" + claimNumber + '\'' +
                ", epsNumber='" + epsNumber + '\'' +
                ", attachmentNumber='" + attachmentNumber + '\'' +
                ", attachmentName='" + attachmentName + '\'' +
                ", sent='" + sent + '\'' +
                '}';
    }
}



