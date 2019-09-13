package hello.model.db;

import hello.model.DomainValue;
import hello.model.Lead;
import hello.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NamedParameterJdbcDataRepository extends JdbcDataRepository {
    @Autowired
    @Qualifier("mfsJdbcTemplate")
    NamedParameterJdbcTemplate mfsJdbcTemplate;

    @Autowired
    @Qualifier("wmConfigJdbcTemplate")
    NamedParameterJdbcTemplate wmConfigJdbcTemplate;

    @Autowired
    @Qualifier("zevigJdbcTemplate")
    NamedParameterJdbcTemplate zevigJdbcTemplate;

    //TODO change to return list
    @Override
    public List<Optional<DomainValue>> findById(String ValueInt) {
        return mfsJdbcTemplate.query(
                "SELECT FF.FILEID, NAME, VALUEINT " +
                        "FROM FILESTORAGE_FILE FF, FILESTORAGE_DOMAINVALUEXREF FD " +
                        "WHERE FF.FILEID = FD.FILEID " +
                        "AND FD.VALUEINT = :ValueInt",
                new MapSqlParameterSource("ValueInt", ValueInt),
                (rs, rownum) ->
                        Optional.of(new DomainValue(rs.getString("FILEID"),
                                rs.getString("NAME"),
                                rs.getString("VALUEINT")))
        );
    }

    //TODO change to return list
    @Override
    public List<Optional<Order>> findByClaimCaseNumber(String claimCaseNumber) {
        return zevigJdbcTemplate.query(
                "SELECT O.ORDERID, O.CLAIMCASENUMBER, O.INSPECTIONTYPEID " +
                        "FROM [ORDER] O " +
                        "WHERE O.INSPECTIONTYPEID IN (430, 431, 432, 433, 434, 435, 436, 437, 438, " +
                        "457) " +//to delete
                        "AND O.CLAIMCASENUMBER = :CLAIMCASENUMBER ",
                new MapSqlParameterSource("CLAIMCASENUMBER", claimCaseNumber),
                (rs, rownum) ->
                        Optional.of(new Order(rs.getString("ORDERID"),
                                rs.getString("CLAIMCASENUMBER"),
                                rs.getString("INSPECTIONTYPEID")))
        );
    }

    //TODO change to return list
    @Override
    public List<Optional<Lead>> findByEkspertyzaOrderId(String ekspertyzaOrderID) {
        return wmConfigJdbcTemplate.query(
                "SELECT IE.EPSLEADID, IEPS.EPSSERVICEID " +
                        "FROM IDENTIFIERSEKSPERTYZAvsEPS IE, IDENTIFIERSEPSSERVICES IEPS " +
                        "WHERE IE.EPSLEADID = IEPS.EPSLEADID " +
                        "AND IE.EkspertyzaOrderID = :ekspertyzaOrderID",
                new MapSqlParameterSource("ekspertyzaOrderID", ekspertyzaOrderID),

                (rs, rownum) ->
                        Optional.of(new Lead(rs.getString("EPSLEADID"),
                                rs.getString("EPSSERVICEID")))
        );
    }
}
