package hello.model.db;

import hello.model.Attachment;
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
public class VigJdbcDataRepository extends JdbcDataRepository {
    @Autowired
    @Qualifier("mfsJdbcTemplate")
    NamedParameterJdbcTemplate mfsJdbcTemplate;

    @Autowired
    @Qualifier("wmConfigJdbcTemplate")
    NamedParameterJdbcTemplate wmConfigJdbcTemplate;

    @Autowired
    @Qualifier("zevigJdbcTemplate")
    NamedParameterJdbcTemplate zevigJdbcTemplate;

    @Override
    public List<Attachment> findAttachmentsByOrderId(String ValueInt) {
        return mfsJdbcTemplate.query(
                "SELECT FF.FILEID, FF.NAME " +
                        "FROM FILESTORAGE_FILE FF, FILESTORAGE_DOMAINVALUEXREF FD " +
                        "WHERE FF.FILEID = FD.FILEID " +
                        "AND FD.VALUEINT = :ValueInt",
                new MapSqlParameterSource("ValueInt", ValueInt),
                (rs, rownum) ->
                        (new Attachment(rs.getString("FILEID"),
                                rs.getString("NAME")))
        );
    }

    @Override
    public List<Optional<Order>> findOrdersByClaimCaseNumber(String claimCaseNumber) {
        return zevigJdbcTemplate.query(
                "SELECT O.ORDERID, O.CLAIMCASENUMBER " +/*, O.INSPECTIONTYPEID */
                        "FROM [ORDER] O " +
                        "WHERE O.CLAIMCASENUMBER = :CLAIMCASENUMBER " +
                        "AND O.INSPECTIONTYPEID between 430 and 438;",
                new MapSqlParameterSource("CLAIMCASENUMBER", claimCaseNumber),
                (rs, rownum) ->
                        Optional.of(new Order(rs.getString("ORDERID"),
                                rs.getString("CLAIMCASENUMBER"),
                                null))
        );
    }

    @Override
    public Optional<Lead> findLeadsByEkspertyzaOrderId(String ekspertyzaOrderID) {
        return wmConfigJdbcTemplate.query(
                "SELECT IE.EPSLEADID, IEPS.EPSSERVICEID " +
                        "FROM IDENTIFIERSEKSPERTYZAvsEPS IE, IDENTIFIERSEPSSERVICES IEPS " +
                        "WHERE IE.EPSLEADID = IEPS.EPSLEADID " +
                        "AND IE.EkspertyzaOrderID = :ekspertyzaOrderID",
                new MapSqlParameterSource("ekspertyzaOrderID", ekspertyzaOrderID),
                (rs) ->(
                        Optional.of(new Lead(rs.getString("EPSLEADID"),
                                rs.getString("EPSSERVICEID"))))
        );
    }
}
