package hello.config.db;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:config-dev.properties", "classpath:config-${spring.profiles.active}.properties"})
public class MFSDatabaseProperties implements DatabaseProperties {
    @Value("${db.mfs.connection_string}")
    private String url;

    @Value("${db.mfs.user}")
    private String username;

    @Value("${db.mfs.password}")
    private String password;

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
