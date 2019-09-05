package hello.config.db;

import org.springframework.stereotype.Repository;


public interface DatabaseProperties {
    public String getUrl();
    public String getUsername();
    public String getPassword();
}
