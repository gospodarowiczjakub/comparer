package hello.config;

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

    @Autowired
    private ZevigDatabaseProperties zevigDatabaseProperties;
    @Autowired
    private MFSDatabaseProperties mfsDatabaseProperties;
    @Autowired
    private WmConfigDatabaseProperties wmConfigDatabaseProperties;
    @Autowired
    private FileConfiguration fileConfiguration;

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
