package hello.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@PropertySource({"classpath:config-dev.properties", "classpath:config-${spring.profiles.active}.properties"})
public class WmConfigDatabaseProperties implements DatabaseProperties {
    @Value("${db.wmConfig.connection_string}")
    private String url;

    @Value("${db.wmConfig.user}")
    private String username;

    @Value("${db.wmConfig.password}")
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
