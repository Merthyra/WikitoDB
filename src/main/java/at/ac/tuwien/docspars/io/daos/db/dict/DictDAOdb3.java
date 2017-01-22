package at.ac.tuwien.docspars.io.daos.db.dict;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.impl.Dict;
import at.ac.tuwien.docspars.io.daos.db.SQLStatements;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DictDAOdb3 extends DictDAOdb {

  private final String READ_CURRENT_DF_VALUES_FROM_DICT_HIST = SQLStatements.getString("sql.dict_hist.read");

  private final Logger logger = LogManager.getLogger(this.getClass());

  public DictDAOdb3(JdbcTemplate template) {
    super(template);
  }

  @Override
  protected ResultSetExtractor<Map<String, Dictionable>> getResultSetExtractor() {
    ResultSetExtractor<Map<String, Dictionable>> dfValueExtractor = new ResultSetExtractor<Map<String, Dictionable>>() {
      @Override
      public Map<String, Dictionable> extractData(ResultSet rs) throws SQLException, DataAccessException {
        final Map<String, Dictionable> dictVal = new HashMap<>();
        while (rs.next()) {
          dictVal.put(rs.getString(2), new Dict(rs.getInt(1), rs.getString(2), rs.getInt(3)));
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
