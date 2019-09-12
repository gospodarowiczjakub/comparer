package hello.model;

import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class Lead {

    private String EPSLeadId;
    private String EPSService;


    public Lead(String epsLeadId, String epsService) {
        EPSLeadId = epsLeadId;
        EPSService = epsService;
    }

    public String getEPSLeadId() {
        return EPSLeadId;
    }

    public void setEPSLeadId(String EPSLeadId) {
        this.EPSLeadId = EPSLeadId;
    }

    public String getEPSService() {
        return EPSService;
    }

    public void setEPSService(String EPSService) {
        this.EPSService = EPSService;
    }

    @Override
    public String toString() {
        return "Lead{" +
                "EPSLeadId='" + EPSLeadId + '\'' +
                ", EPSService='" + EPSService + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lead lead = (Lead) o;
        return EPSLeadId.equals(lead.EPSLeadId) &&
                EPSService.equals(lead.EPSService);
    }

    @Override
    public int hashCode() {
        return Objects.hash(EPSLeadId, EPSService);
    }
}
