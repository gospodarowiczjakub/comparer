package hello.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ReportUniqueClaim implements Serializable{
    @JsonProperty("claimNumber")
    private String claimNumber;
    @JsonProperty("EPSNumber")
    private String EPSNumber;
    @JsonUnwrapped
    private List<Attachment> attachments;

    public ReportUniqueClaim(String claimNumber, String EPSNumber, List<Attachment> attachments) {
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
        return "ReportUniqueClaim{" +
                "claimNumber='" + claimNumber + '\'' +
                ", EPSNumber='" + EPSNumber + '\'' +
                ", attachments=" + attachments.toString() +
                "}"+System.lineSeparator();
    }
}
