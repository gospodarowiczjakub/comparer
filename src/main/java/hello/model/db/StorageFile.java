package hello.model.db;


import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "FileStorage_File")
public class StorageFile {
    public String getFileId() {
        return FileId;
    }

    public void setFileId(String fileId) {
        FileId = fileId;
    }

    private String FileId;

    public StorageFile(String fileId) {
        FileId = fileId;
    }

    @Override
    public String toString() {
        return "StorageFile{" +
                "FileId='" + FileId + '\'' +
                '}';
    }
}
