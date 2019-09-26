package hello.config;

import hello.config.db.DatabaseProperties;
import hello.config.db.MFSDatabaseProperties;
import hello.config.db.WmConfigDatabaseProperties;
import hello.config.db.ZevigDatabaseProperties;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.sql.SQLException;

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

    @Bean
    public JdbcTemplate setJdbcTemplate(){

        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUsername(this.getMfsDatabaseProperties().getUsername());
        dataSource.setPassword(this.getMfsDatabaseProperties().getPassword());
        dataSource.setUrl(this.getMfsDatabaseProperties().getUrl());
        JdbcTemplate jdbcTemplate = null;
        try {
            jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(dataSource.getConnection(), true));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jdbcTemplate;
    }

    @Bean
    @Qualifier("mfsJdbcTemplate")
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(this.getMfsDatabaseProperties().getUrl());
        dataSource.setUsername(this.getMfsDatabaseProperties().getUsername());
        dataSource.setPassword(this.getMfsDatabaseProperties().getPassword());

        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    @Qualifier("wmConfigJdbcTemplate")
    public NamedParameterJdbcTemplate getWmConfigJdbcTemplate(){

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(this.getWmConfigDatabaseProperties().getUrl());
        dataSource.setUsername(this.getWmConfigDatabaseProperties().getUsername());
        dataSource.setPassword(this.getWmConfigDatabaseProperties().getPassword());

        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    @Qualifier("zevigJdbcTemplate")
    public NamedParameterJdbcTemplate geZevigJdbcTemplate(){

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(this.getZevigDatabaseProperties().getUrl());
        dataSource.setUsername(this.getZevigDatabaseProperties().getUsername());
        dataSource.setPassword(this.getZevigDatabaseProperties().getPassword());

        return new NamedParameterJdbcTemplate(dataSource);
    }
}
