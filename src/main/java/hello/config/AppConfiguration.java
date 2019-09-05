package hello.config;

import hello.config.db.MFSDatabaseProperties;
import hello.config.db.WmConfigDatabaseProperties;
import hello.config.db.ZevigDatabaseProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource({"classpath:config-dev.properties", "classpath:config-${spring.profiles.active}.properties"})
public class AppConfiguration {
    @Value("${text.welcome}")
    private String helloValue;

    public ZevigDatabaseProperties getZevigDatabaseProperties() {
        return zevigDatabaseProperties;
    }

    public MFSDatabaseProperties getMfsDatabaseProperties() {
        return mfsDatabaseProperties;
    }

    public WmConfigDatabaseProperties getWmConfigDatabaseProperties() {
        return wmConfigDatabaseProperties;
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    private ZevigDatabaseProperties zevigDatabaseProperties;
    private MFSDatabaseProperties mfsDatabaseProperties;
    private WmConfigDatabaseProperties wmConfigDatabaseProperties;
    private FileConfiguration fileConfiguration;

    @Autowired
    public AppConfiguration(ZevigDatabaseProperties zdp, MFSDatabaseProperties mdp, WmConfigDatabaseProperties wdp, FileConfiguration fc) {
        this.wmConfigDatabaseProperties=wdp;
        this.zevigDatabaseProperties=zdp;
        this.mfsDatabaseProperties=mdp;
        this.fileConfiguration=fc;
    }

    public String getHelloValue() {
        return fileConfiguration.getFileName()+mfsDatabaseProperties.getUrl() + zevigDatabaseProperties.getUrl() + wmConfigDatabaseProperties.getUrl();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev(){
        return new PropertySourcesPlaceholderConfigurer();
    }
}
