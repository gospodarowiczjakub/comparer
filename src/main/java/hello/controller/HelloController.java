package hello.controller;

import hello.DependencyInjectionExample;
import hello.config.AppConfiguration;
import hello.config.FileConfiguration;
import hello.jdbc.ConnectionFactory;
import hello.jdbc.MfsConfigConnection;
import hello.jdbc.WmConfigConnection;
import hello.jdbc.ZevigConfigConnection;
import hello.model.DomainValue;
import hello.model.EPSClaim;
import hello.model.Lead;
import hello.model.Order;
import hello.model.db.DataRepository;
import hello.utils.CSVDataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
public class HelloController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);
    private final DependencyInjectionExample dependencyInjectionExample;

    @Autowired
    private FileConfiguration fileConfiguration;
    @Autowired
    private AppConfiguration appConfiguration;
    @Autowired
    @Qualifier("namedParameterJdbcDataRepository")
    private DataRepository dataRepository;

    public HelloController(DependencyInjectionExample dependencyInjectionExample) {
        this.dependencyInjectionExample = dependencyInjectionExample;
    }

    @RequestMapping("/")
    public String index() throws SQLException {

        Connection conn = connectToDbs(null);

        List<Optional<DomainValue>> dom = dataRepository.findById("");
        for (Optional<DomainValue> d : dom)
            d.ifPresent(storageFile -> {
                System.out.println("Storage file" + storageFile.toString());
            });

        List<Optional<Order>> order = dataRepository.findByClaimCaseNumber("");
        for (Optional<Order> o : order)
            o.ifPresent(ord -> {
                System.out.println("OrderId: " + ord.toString());
            });

        List<Optional<Lead>> lead = dataRepository.findByEkspertyzaOrderId("");
        for (Optional<Lead> l : lead)
            l.ifPresent(res -> {
                System.out.println("EPSLead: " + res.toString());
            });

        return dependencyInjectionExample.getHelloValue();
    }

    public String importCSV() {
        CSVDataLoader csvDataLoader = new CSVDataLoader();
        List<EPSClaim> claims = csvDataLoader.loadObjectList(EPSClaim.class, fileConfiguration.getFileName());

        return claims.get(0).getAttachmentName();
    }

    public Connection connectToDbs(ConnectionFactory connectionFactory) throws SQLException {

        //LOGGER.info(claims.get(0).toString());
        ConnectionFactory wmConfigConnection = new WmConfigConnection();
        ConnectionFactory zevigConfigConnection = new ZevigConfigConnection();
        ConnectionFactory mfsConfigConnection = new MfsConfigConnection();

        Connection wmConfigConn = wmConfigConnection.getConnection(appConfiguration.getWmConfigDatabaseProperties());
        Connection mfsConn = mfsConfigConnection.getConnection(appConfiguration.getMfsDatabaseProperties());
        Connection zevigConn = zevigConfigConnection.getConnection(appConfiguration.getZevigDatabaseProperties());

        LOGGER.info(": " + wmConfigConn.isClosed());
        LOGGER.info(": " + mfsConn.isClosed());
        LOGGER.info(": " + zevigConn.isClosed());

        return mfsConn;
    }


}
