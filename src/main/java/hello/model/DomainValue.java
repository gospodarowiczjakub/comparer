package hello.model;


import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "FileStorage_DomainValueXREF")
public class DomainValue {
    //@Value("FILEID")
    private String fileId;
   // @Value("NAME")
    private String name;

    public DomainValue(String fileId, String name, String valueInt) {
        this.fileId = fileId;
        this.name = name;
        this.valueInt = valueInt;
    }

    //@Value("VALUEINT")
    private String valueInt;

    @Override
    public String toString() {
        return "DomainValue{" +
                "fileId='" + fileId + '\'' +
                ", name='" + name + '\'' +
                ", valueInt='" + valueInt + '\'' +
                '}';
    }
}
