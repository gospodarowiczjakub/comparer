package hello.controller;

import hello.DependencyInjectionExample;
import hello.config.AppConfiguration;
import hello.config.FileConfiguration;
import hello.jdbc.*;
import hello.model.EPSClaim;
import hello.model.StorageFileRepository;
import hello.utils.CSVDataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RestController
public class HelloController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);
    private final DependencyInjectionExample dependencyInjectionExample;

    @Autowired
    private FileConfiguration fileConfiguration;
    @Autowired
    private AppConfiguration appConfiguration;
    @Qualifier("namedParameterJdbcStorageFileRepository")
    private StorageFileRepository storageFileRepository;


    public HelloController(DependencyInjectionExample dependencyInjectionExample) {
        this.dependencyInjectionExample = dependencyInjectionExample;
    }

    @RequestMapping("/")
    public String index() throws SQLException {
        CSVDataLoader csvDataLoader = new CSVDataLoader();
        List<EPSClaim> claims = csvDataLoader.loadObjectList(EPSClaim.class, fileConfiguration.getFileName());

        Connection conn = connectToDbs(null);

        //JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());

        int test;
        //test = jdbcTemplate.queryForInt("select count(*) from FileStorage_Domain");

        return dependencyInjectionExample.getHelloValue();
    }

    public Connection connectToDbs(ConnectionFactory connectionFactory) throws SQLException {

        //LOGGER.info(claims.get(0).toString());
        ConnectionFactory wmConfigConnection = new WmConfigConnection();
        ConnectionFactory zevigConfigConnection = new ZevigConfigConnection();
        ConnectionFactory mfsConfigConnection = new MfsConfigConnection();

        Connection wmConfigConn = wmConfigConnection.getConnection(appConfiguration.getWmConfigDatabaseProperties());
        Connection mfsConn = mfsConfigConnection.getConnection(appConfiguration.getMfsDatabaseProperties());
        Connection zevigConn = zevigConfigConnection.getConnection(appConfiguration.getZevigDatabaseProperties());

        //LOGGER.info(": " + wmConfig.toString());
        LOGGER.info(": " + mfsConn.isClosed());
        LOGGER.info(": " + zevigConn.isClosed());


        return mfsConn;
    }



}
