import hello.DependencyInjectionExample;
import hello.controller.HelloController;
import hello.model.Order;
import hello.model.db.DataRepository;
import hello.model.db.NamedParameterJdbcDataRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Qualifier;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReportsComparatorTest {

    @Mock
    private DependencyInjectionExample dependencyInjectionExample;

    @Mock
    @Qualifier("namedParameterJdbcDataRepository")
    private DataRepository dataRepository;

    @Test
    public void testGettingOrders() throws SQLException {
        HelloController helloController = new HelloController(dependencyInjectionExample);

        NamedParameterJdbcDataRepository dataRepository = mock(NamedParameterJdbcDataRepository.class);
        ArrayList<Optional<Order>> ordersDb = new ArrayList<>();
        ordersDb.add(Optional.of(new Order("1", "claim1", "430")));
        ordersDb.add(Optional.of(new Order("2", "claim2", "430")));
        ordersDb.add(Optional.of(new Order("3", "claim3", "430")));

        when(dataRepository.findByClaimCaseNumber("1231")).thenReturn(ordersDb);

        List<Optional<Order>> result = dataRepository.findByClaimCaseNumber("1231");
        verify(dataRepository, atLeastOnce()).findByClaimCaseNumber("1231");
        //helloController.addAttachmentsByOrderId();
        //helloController.index();
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
}
