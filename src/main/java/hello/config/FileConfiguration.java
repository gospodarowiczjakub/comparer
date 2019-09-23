package hello.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@PropertySource({"classpath:config-dev.properties", "classpath:config-${spring.profiles.active}.properties"})
public class FileConfiguration {
    @Value("${file.inputReport}")
    private String fileName;

    @Value("${file.lostAttachments}")
    private String lostAttachments;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLostAttachments() {
        return lostAttachments;
    }

    public void setLostAttachments(String lostAttachments) {
        this.lostAttachments = lostAttachments;
    }
}
