package hello.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.validation.executable.ValidateOnExecution;

@Configuration
@PropertySource({"classpath:config-dev.properties", "classpath:config-${spring.profiles.active}.properties"})
public class ZevigDatabaseProperties implements DatabaseProperties {
    @Value("${db.zevig.connection_string}")
    private String url;

    @Value("${db.zevig.user}")
    private String username;

    @Value("${db.zevig.password}")
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
