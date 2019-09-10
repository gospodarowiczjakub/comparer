package hello.model.db;

import hello.model.DomainValue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface StorageFileRepository {

    int count();

    int save(DomainValue DomainValue);

    int update(DomainValue DomainValue);

    int deleteById(Long id);

    List<DomainValue> findAll();

    List<DomainValue> findByNameAndPrice(String name, BigDecimal price);

    Optional<DomainValue> findById(Long id);

    String getNameById(Long id);

}
