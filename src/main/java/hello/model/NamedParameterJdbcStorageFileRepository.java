package hello.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class NamedParameterJdbcStorageFileRepository extends JdbcStorageFileRepository {
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Optional<StorageFile> findById(Long ValueInt){
        return namedParameterJdbcTemplate.queryForObject(
          "select * from [dbo].[FileStorage_DomainValueXREF] where ValueInt = :ValueInt",
          new MapSqlParameterSource("ValueInt", ValueInt),
                (rs, rownum) ->
                        Optional.of(new StorageFile(rs.getString("ValueInt")))
        );
    }
}
