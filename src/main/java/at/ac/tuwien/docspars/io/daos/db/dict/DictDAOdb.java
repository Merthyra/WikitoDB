package at.ac.tuwien.docspars.io.daos.db.dict;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.impl.Dict;
import at.ac.tuwien.docspars.io.daos.db.CrudOperations;
import at.ac.tuwien.docspars.io.daos.db.SQLStatements;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DictDAOdb implements CrudOperations<Dictionable, Map<String, Dict>> {

  protected JdbcTemplate jdbcTemplate;

  @SuppressWarnings("unused")
  private DictDAOdb() {}

  public DictDAOdb(final JdbcTemplate template) {
    this.jdbcTemplate = template;
  }


  @Override
  public Map<String, Dict> read() {
    final ResultSetExtractor<Map<String, Dict>> resEx = new ResultSetExtractor<Map<String, Dict>>() {
      @Override
      public Map<String, Dict> extractData(final ResultSet res) throws SQLException, DataAccessException {
        final Map<String, Dict> dict = new HashMap<String, Dict>();
        while (res.next()) {
          dict.put(res.getString("term"), new Dict(res.getInt("tid"), res.getString("term")));
        }
        return dict;
      }
    };
    final Map<String, Dict> dicts = this.jdbcTemplate.query(SQLStatements.getString("sql.dict.read"), resEx);
    logger.debug("{} read {} dict entries from dict table", DictDAOdb.class.getTypeName(), dicts.size());
    return dicts;
  }

  @Override
  @PerformanceMonitored
  public boolean add(final List<Dictionable> dicts) {
    int[] updateCounts = null;
    // String[] currTerm = new String[dicts.size()];
    // try {
    updateCounts = this.jdbcTemplate.batchUpdate(SQLStatements.getString("sql.dict.insert"), new BatchPreparedStatementSetter() {
      @Override
      public void setValues(final PreparedStatement ps, final int i) throws SQLException {

        ps.setInt(1, dicts.get(i).getTId());
        // currTerm[i] = dicts.get(i).getTerm();
        ps.setString(2, dicts.get(i).getTerm());
      }

      @Override
      public int getBatchSize() {
        return dicts.size();
      }
    });
    logger.debug("{} wrote {} dict entries to dict table", DictDAOdb.class.getTypeName(), updateCounts.length);
    return updateCounts.length == dicts.size();

  }

  @Override
  public boolean drop() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean remove(final List<Dictionable> a) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean update(final List<Dictionable> a) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean create() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void setTimestamp(Timestamp stamp) {
    // TODO Auto-generated method stub

  }

}
