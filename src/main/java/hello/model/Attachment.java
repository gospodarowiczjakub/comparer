package hello.model;

import java.util.Objects;

public class Attachment {
    private String attachmentNumber;
    private String filename;

    public Attachment(String attachmentNumber, String filename) {
        this.attachmentNumber = attachmentNumber;
        this.filename = filename;
    }

    public String getAttachmentNumber() {
        return attachmentNumber;
    }

    public void setAttachmentNumber(String attachmentNumber) {
        this.attachmentNumber = attachmentNumber;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attachment that = (Attachment) o;
        return attachmentNumber.equals(that.attachmentNumber) &&
                filename.equals(that.filename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attachmentNumber, filename);
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "attachmentNumber='" + attachmentNumber + '\'' +
                ", filename='" + filename + '\'' +
                '}';
    }
}
