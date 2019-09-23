import hello.DependencyInjectionExample;
import hello.controller.HelloController;
import hello.model.Attachment;
import hello.model.Order;
import hello.model.ReportClaim;
import hello.model.ReportUniqueClaim;
import hello.model.db.DataRepository;
import hello.model.db.NamedParameterJdbcDataRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Qualifier;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReportsComparatorTest {

    @Mock
    private DependencyInjectionExample dependencyInjectionExample;

    @Mock
    @Qualifier("namedParameterJdbcDataRepository")
    private DataRepository dataRepository;

    @Mock
    HelloController helloController;

    @Test
    public void testGettingOrders() throws SQLException {
        HelloController helloController = new HelloController(dependencyInjectionExample);

        NamedParameterJdbcDataRepository dataRepository = mock(NamedParameterJdbcDataRepository.class);
        ArrayList<Optional<Order>> ordersDb = new ArrayList<>();
        ordersDb.add(Optional.of(new Order("1", "claim1", "430")));
        ordersDb.add(Optional.of(new Order("2", "claim2", "430")));
        ordersDb.add(Optional.of(new Order("3", "claim3", "430")));

        when(dataRepository.findOrdersByClaimCaseNumber("1231")).thenReturn(ordersDb);

        List<Optional<Order>> result = dataRepository.findOrdersByClaimCaseNumber("1231");
        verify(dataRepository, atLeastOnce()).findOrdersByClaimCaseNumber("1231");
        //helloController.addAttachmentsByOrderId();
        //helloController.index.html();
    }

    @Test
    public void testCSVImport(){
        
    }

    @Test
    public void testGettingAttachmentsByOrderId(){
        HelloController helloController = new HelloController(dependencyInjectionExample);

        NamedParameterJdbcDataRepository dataRepository = mock(NamedParameterJdbcDataRepository.class);
        ArrayList<Optional<Order>> ordersDb = new ArrayList<>();
    }

    @Test
    public void testReportComparing() throws SQLException {
        HelloController helloController = new HelloController(dependencyInjectionExample);
        NamedParameterJdbcDataRepository dataRepository = mock(NamedParameterJdbcDataRepository.class);
        spy(helloController);
        ArrayList<Optional<Order>> ordersDb = new ArrayList<>();
        ordersDb.add(Optional.of(new Order("1", "claim1", "430")));
        ordersDb.add(Optional.of(new Order("2", "claim2", "430")));
        ordersDb.add(Optional.of(new Order("3", "claim3", "430")));

        ArrayList<Attachment> attachmentsDb = new ArrayList<>();
        attachmentsDb.add(new Attachment("att1", "attName1"));

        ArrayList<Attachment> attachmentsRep = new ArrayList<>();
        attachmentsRep.add(new Attachment("att1", "attName1"));
        attachmentsRep.add(new Attachment("att2", "attName2"));


        List<ReportClaim> claims = new ArrayList<>();
        claims.add(new ReportClaim("claim1","epsNum1", "att1", "attName1", "18.01.2019 05:31"));

        List<ReportUniqueClaim> uniqueClaimsDb = new ArrayList<>();
        uniqueClaimsDb.add(new ReportUniqueClaim("claim1", "epsNum1", attachmentsDb));


        List<ReportUniqueClaim> uniqueClaimsRep = new ArrayList<>();
        uniqueClaimsRep.add(new ReportUniqueClaim("claim1", "epsNum1", attachmentsRep));

        List<ReportClaim> claimsDb = new ArrayList<>();
        claimsDb.add(new ReportClaim("claim1","epsNum1", "att1", "attName1", "18.01.2019 05:31"));

        List<ReportClaim> claimsReport = new ArrayList<>();
        claimsReport.add(new ReportClaim("claim3","epsNum1", "att1", "attName1"));
        claimsReport.add(new ReportClaim("claim2","epsNum2", "att2", "attName2"));


        when(dataRepository.findOrdersByClaimCaseNumber("1231")).thenReturn(ordersDb);
        when(dataRepository.findAttachmentsByOrderId("1231")).thenReturn(attachmentsDb);
        //when(helloController.importCSVReport()).thenReturn(claims);

        List<ReportClaim> result = helloController.compareSets(claimsDb, claimsReport);
        assertEquals(result.size(), 2);

        //assertTrue(helloController.);
    }
}
