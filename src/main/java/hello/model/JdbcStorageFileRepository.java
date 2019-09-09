package hello.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcStorageFileRepository implements StorageFileRepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int count(){
        return jdbcTemplate
                .queryForObject("select count(*) from [dbo].[FileStorage_DomainValueXREF]", Integer.class);
    }

    @Override
    public int save(StorageFile StorageFile) {
        return 0;
    }

    @Override
    public int update(StorageFile StorageFile) {
        return 0;
    }

    @Override
    public int deleteById(Long id) {
        return 0;
    }

    @Override
    public List<StorageFile> findAll() {
        return null;
    }

    @Override
    public List<StorageFile> findByNameAndPrice(String name, BigDecimal price) {
        return null;
    }

    @Override
    public Optional<StorageFile> findById(Long ValueInt) {
        return jdbcTemplate.queryForObject(
                "select * from [dbo].[FileStorage_DomainValueXREF] where ValueInt = ?",
                new Object[]{ValueInt},
                (rs, rowNum) ->
                        Optional.of(new StorageFile(
                                rs.getString("ValueInt")
                        ))
        );
    }

    @Override
    public String getNameById(Long id) {
        return null;
    }

}
