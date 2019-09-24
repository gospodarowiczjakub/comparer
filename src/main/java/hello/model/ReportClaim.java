package hello.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

import java.util.Objects;

public class ReportClaim {

    @JsonProperty("claimNumber")
    public String claimNumber;
    @JsonProperty("epsNumber")
    public String epsNumber;
    @JsonProperty("attachmentNumber")
    public String attachmentNumber;
    @JsonProperty("attachmentName")
    public String attachmentName;
    @JsonProperty("sent")
    @Nullable
    public String sent;

    public ReportClaim() {
    }

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

    public ReportClaim(String claimNumber, String epsNumber, String attachmentNumber, String attachmentName) {
        this.claimNumber = claimNumber;
        this.epsNumber = epsNumber;
        this.attachmentNumber = attachmentNumber;
        this.attachmentName = attachmentName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportClaim that = (ReportClaim) o;
        return claimNumber.equals(that.claimNumber) &&
                epsNumber.equals(that.epsNumber) &&
                attachmentNumber.equals(that.attachmentNumber) &&
                attachmentName.equals(that.attachmentName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(claimNumber, epsNumber, attachmentNumber, attachmentName);
    }
}



