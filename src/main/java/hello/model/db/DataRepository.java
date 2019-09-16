package hello.model.db;

import hello.model.Attachment;
import hello.model.DomainValue;
import hello.model.Lead;
import hello.model.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface DataRepository {

    int count();

    int save(DomainValue DomainValue);

    int update(DomainValue DomainValue);

    int deleteById(Long id);

    List<DomainValue> findAll();

    List<DomainValue> findByNameAndPrice(String name, BigDecimal price);

    List<Optional<Order>> findByClaimCaseNumber(String claimCaseNumber);

    List<Optional<Lead>> findByEkspertyzaOrderId(String ekspertyzaOrderId);

    List<Attachment> findById(String id);

    String getNameById(Long id);

}
