package hello.model;


import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "FileStorage_DomainValueXREF")
public class DomainValue {
    private String DomainId;
    private String ValueText;
    private String ValueInt;
    private String FileId;


    public DomainValue(String valueInt) {
        ValueInt = valueInt;
    }

    public String getDomainId() {

        return DomainId;
    }

    public void setDomainId(String domainId) {
        DomainId = domainId;
    }

    public String getValueText() {
        return ValueText;
    }

    public void setValueText(String valueText) {
        ValueText = valueText;
    }

    public String getFileId() {
        return FileId;
    }

    public void setFileId(String fileId) {
        FileId = fileId;
    }

    public String getValueInt() {
        return ValueInt;
    }

    public void setValueInt(String valueInt) {
        ValueInt = valueInt;
    }

    @Override
    public String toString() {
        return "DomainValue{" +
                "DomainId='" + DomainId + '\'' +
                ", ValueText='" + ValueText + '\'' +
                ", ValueInt='" + ValueInt + '\'' +
                ", FileId='" + FileId + '\'' +
                '}';
    }
}
