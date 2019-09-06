package hello.jdbc;

import hello.model.StorageFile;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class StorageFileDao implements Dao<StorageFileDao> {


    @Override
    public Optional<StorageFileDao> get(long id) {
        return Optional.empty();
    }

    @Override
    public List<StorageFileDao> getAll() {
        return null;
    }

    @Override
    public void save(StorageFileDao storageFileDao) {

    }

    @Override
    public void update(StorageFileDao storageFileDao, String[] params) {

    }

    @Override
    public void delete(StorageFileDao storageFileDao) {

    }
}
