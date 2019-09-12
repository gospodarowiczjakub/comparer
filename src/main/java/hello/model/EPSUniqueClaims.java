package hello.model;

import java.util.List;
import java.util.Objects;

public class EPSUniqueClaims {
    private String claimNumber;
    private String EPSNumber;
    private List<Attachment> attachments;

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


    //epsuni
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EPSUniqueClaims that = (EPSUniqueClaims) o;
        return claimNumber.equals(that.claimNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(claimNumber);
    }
}
