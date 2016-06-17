package at.ac.tuwien.docspars.io.daos.db.dict;

import at.ac.tuwien.docspars.entity.impl.Dict;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DictDAOdb3 extends DictDAOdb {

  private final String READ_CURRENT_DF_VALUES_FROM_DICT_HIST =
      "select dict.tid, dict.term, terms3.did from dict join terms3 on terms3.tid = dict.tid";

  public DictDAOdb3(JdbcTemplate template) {
    super(template);
  }

  @Override
  protected ResultSetExtractor<Map<String, Dict>> getResultSetExtractor() {
    ResultSetExtractor<Map<String, Dict>> dfValueExtractor = new ResultSetExtractor<Map<String, Dict>>() {
      @Override
      public Map<String, Dict> extractData(ResultSet rs) throws SQLException, DataAccessException {
        final Map<String, Dict> dictVal = new HashMap<>();
        while (rs.next()) {
          Integer did = rs.getInt(3);
          dictVal.merge(rs.getString(2), new Dict(rs.getInt(1), rs.getString(2), rs.getInt(3)), (v1, v2) -> v1.registerDocument(did));
        }
        return dictVal;
      }
    };
    return dfValueExtractor;
  }

  @Override
  String getLookupString() {
    return READ_CURRENT_DF_VALUES_FROM_DICT_HIST;
  }

}
