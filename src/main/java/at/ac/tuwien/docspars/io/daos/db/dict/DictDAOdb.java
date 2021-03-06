package at.ac.tuwien.docspars.io.daos.db.dict;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.Timestampable;
import at.ac.tuwien.docspars.entity.impl.Dict;
import at.ac.tuwien.docspars.io.daos.db.AbstractCrudOperations;
import at.ac.tuwien.docspars.io.daos.db.SQLStatements;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DictDAOdb extends AbstractCrudOperations<Dictionable, Map<String, Dictionable>> implements Timestampable {

  protected JdbcTemplate jdbcTemplate;
  private final Logger logger = LogManager.getLogger(this.getClass());

  @SuppressWarnings("unused")
  private DictDAOdb() {}

  public DictDAOdb(final JdbcTemplate template) {
    this.jdbcTemplate = template;
  }

  @Override
  @PerformanceMonitored
  public Map<String, Dictionable> read() {
    final Map<String, Dictionable> dicts = this.jdbcTemplate.query(getLookupString(), getResultSetExtractor());
    logger.debug("{} read {} dict entries from dict table", DictDAOdb.class.getTypeName(), dicts.size());
    return dicts;
  }

  ResultSetExtractor<Map<String, Dictionable>> getResultSetExtractor() {
    final ResultSetExtractor<Map<String, Dictionable>> resEx = new ResultSetExtractor<Map<String, Dictionable>>() {
      @Override
      public Map<String, Dictionable> extractData(final ResultSet res) throws SQLException, DataAccessException {
        final Map<String, Dictionable> dict = new HashMap<>();
        while (res.next()) {
          final String term = res.getString("term");
          dict.put(term, new Dict(res.getInt("tid"), term));
        }
        return dict;
      }
    };
    return resEx;
  }

  @Override
  public void createIntermediateDictionary(List<Dictionable> batchVocab) {
    try {
      dropIntermediateTermsTable();
    } catch (Exception ex) {
      logger.info("no intermediate table present");
    }

    createIntermediateTermsTable(batchVocab);
  }

  protected void createIntermediateTermsTable(List<Dictionable> dicts) {
    this.jdbcTemplate.batchUpdate("create table wiki.invalidate_dict (tid int, df int)");
    this.jdbcTemplate.batchUpdate("INSERT INTO wiki.invalidate_dict (tid, df) values (?,?)", new BatchPreparedStatementSetter() {

      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setInt(1, dicts.get(i).getTId());
        ps.setInt(2, dicts.get(i).getDf());
      }

      @Override
      public int getBatchSize() {
        return dicts.size();
      }
    });

  }

  protected void dropIntermediateTermsTable() {
    this.jdbcTemplate.update("DROP TABLE wiki.invalidate_dict");
  }

  @Override
  @PerformanceMonitored
  public boolean add(final List<Dictionable> dicts) {
    int[] updateCounts = null;

    updateCounts = this.jdbcTemplate.batchUpdate(SQLStatements.getString("sql.dict.insert"), new BatchPreparedStatementSetter() {
      @Override
      public void setValues(final PreparedStatement ps, final int i) throws SQLException {

        ps.setInt(1, dicts.get(i).getTId());
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

  String getLookupString() {
    return SQLStatements.getString("sql.dict.read");
  }

  @Override
  public boolean remove(List<Dictionable> a) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean update(List<Dictionable> a) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean create() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean drop() {
    // TODO Auto-generated method stub
    return false;
  }


}
