package hello.model;


import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="FileStorage_DomainValueXREF")
public class StorageFile {

    private String fileId;
    private String filename;
    private String location;
    private Date expirationDate;
    private Date creationDate;
    private Date creatorId;

    public StorageFile() {
    }
}
