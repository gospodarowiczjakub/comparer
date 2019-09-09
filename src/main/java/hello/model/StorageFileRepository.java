package hello.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface StorageFileRepository {

    int count();

    int save(StorageFile StorageFile);

    int update(StorageFile StorageFile);

    int deleteById(Long id);

    List<StorageFile> findAll();

    List<StorageFile> findByNameAndPrice(String name, BigDecimal price);

    Optional<StorageFile> findById(Long id);

    String getNameById(Long id);

}
