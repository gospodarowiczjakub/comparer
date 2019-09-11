package hello.model.db;

import hello.model.DomainValue;
import hello.model.Lead;
import hello.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcDataRepository implements DataRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int count(){
        return jdbcTemplate
                .queryForObject("select count(*) from [dbo].[FileStorage_DomainValueXREF]", Integer.class);
    }

    @Override
    public int save(DomainValue DomainValue) {
        return 0;
    }

    @Override
    public int update(DomainValue DomainValue) {
        return 0;
    }

    @Override
    public int deleteById(Long id) {
        return 0;
    }

    @Override
    public List<DomainValue> findAll() {
        return null;
    }

    @Override
    public List<DomainValue> findByNameAndPrice(String name, BigDecimal price) {
        return null;
    }

    @Override
    public List<Optional<Order>> findByClaimCaseNumber(String claimCaseNumber) {
        return null;
    }

    @Override
    public List<Optional<DomainValue>> findById(String ValueInt) {
/*        return jdbcTemplate.queryForObject(
                "select * from [dbo].[FileStorage_DomainValueXREF] where ValueInt = ?",
                new Object[]{ValueInt},
                (rs, rowNum) ->
                        Optional.of(new DomainValue(
                                rs.getString("ValueInt")
                        ))
        );*/
        return null;
    }

    @Override
    public String getNameById(Long id) {
        return null;
    }

    @Override
    public List<Optional<Lead>> findByEkspertyzaOrderId(String ekspertyzaOrderId){
        return null;
    }
}
