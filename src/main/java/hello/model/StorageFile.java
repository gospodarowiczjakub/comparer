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

    public StorageFile(String valueInt) {
        ValueInt = valueInt;
    }

    private String ValueInt;

    public String getValueInt() {
        return ValueInt;
    }

    public void setValueInt(String valueInt) {
        ValueInt = valueInt;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Date creatorId) {
        this.creatorId = creatorId;
    }

}
