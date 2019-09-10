package hello.model.db;

import hello.model.DomainValue;
import hello.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class NamedParameterJdbcStorageFileRepository extends JdbcStorageFileRepository {
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    //TODO change sql to:"SELECT f.FileID, Name, ValueInt FROM "
    //              + "FileStorage_File f, \"" + prop.getProperty("MFS_DB.database") + "\".\"dbo\".\"FileStorage_DomainValueXREF\" d "
    //              + "WHERE f.FileID = d.FileID AND "
    //              + "d.ValueInt = ?;
    @Override
    public Optional<DomainValue> findById(Long ValueInt){
        return namedParameterJdbcTemplate.queryForObject(
          "select * from [dbo].[FileStorage_DomainValueXREF] where ValueInt = :ValueInt",
          new MapSqlParameterSource("ValueInt", ValueInt),
                (rs, rownum) ->
                        Optional.of(new DomainValue(rs.getString("ValueInt")))
        );
    }


    public Optional<Order> findByClaimCaseNumber(String claimCaseNumber){
        return namedParameterJdbcTemplate.queryForObject(
            "SELECT O.ORDERID, O.CLAIMCASENUMBER, O.INSPECTIONTYPEID" +
                    "FROM [ORDER] O" +
                    "WHERE O.CLAIMCASENUMBER = :CLAIMCASENUMBER",
                new MapSqlParameterSource("CLAIMCASENUMBER", claimCaseNumber),
                (rs, rownum) ->
                        Optional.of(new Order(rs.getString("ORDERID"),
                                rs.getString("CLAIMCASENUMBER"),
                                rs.getString("INSPECTIONTYPEID")))
        );
    }

    //TODO add findByEkspertyzaOrderId query
    // SELECT ie.EPSLeadID, ies.EPSServiceID "
    //               +"FROM IdentifiersEKSPERTYZAvsEPS ie, IdentifiersEPSServices ies WHERE "
    //               +"ie.EkspertyzaOrderID = ? AND ie.EPSLeadID = ies.EPSLeadID
}
