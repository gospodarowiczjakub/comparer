package hello.controller;

import hello.DependencyInjectionExample;
import hello.config.AppConfiguration;
import hello.config.FileConfiguration;
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
        importCSV();
        findLeadsByOrderId();
        findAttachmentsByOrderId();
        findOrdersByClaimNumber();
        return dependencyInjectionExample.getHelloValue();
    }

    public List<Optional<Lead>> findLeadsByOrderId() {
        List<Optional<Lead>> leads = dataRepository.findByEkspertyzaOrderId("3997352");
        for (Optional<Lead> l : leads)
            l.ifPresent(res -> {
                System.out.println("EPSLead: " + res.toString());
            });

        return leads;
    }

    public List<Optional<Order>> findOrdersByClaimNumber() {
        List<Optional<Order>> orders = dataRepository.findByClaimCaseNumber("02/K/0010001/01");
        for (Optional<Order> o : orders)
            o.ifPresent(ord -> {
                System.out.println("OrderId: " + ord.toString());
            });
        return orders;
    }

    public List<Optional<DomainValue>> findAttachmentsByOrderId() {
        List<Optional<DomainValue>> dom = dataRepository.findById("3428940");
        for (Optional<DomainValue> d : dom)
            d.ifPresent(storageFile -> {
                System.out.println("Storage file" + storageFile.toString());
            });
        return dom;
    }

    public List<EPSClaim> importCSV() {
        CSVDataLoader csvDataLoader = new CSVDataLoader();
        List<EPSClaim> claims = csvDataLoader.loadObjectList(EPSClaim.class, fileConfiguration.getFileName());
       /* for(EPSClaim claim: claims)
            LOGGER.info(claim.toString());*/

        return claims;
    }

    /*public Connection connectToDbs(ConnectionFactory connectionFactory) throws SQLException {

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
    }*/
}
