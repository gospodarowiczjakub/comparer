package hello.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;
@JsonPropertyOrder({"claimNumber","epsNumber","attachmentNumber","attachmentName","sent"})
public class EPSClaim {
    private String claimNumber;
    private String epsNumber;
    private String attachmentNumber;
    private String attachmentName;
    private Date sent;


}
