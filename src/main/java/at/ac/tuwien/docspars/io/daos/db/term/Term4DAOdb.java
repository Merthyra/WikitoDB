package at.ac.tuwien.docspars.io.daos.db.term;

import at.ac.tuwien.docspars.entity.impl.Term;
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
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Term4DAOdb extends AbstractTermDAOdb {

  private Timestamp timestamp;
  private final String SET_DF_TIMESTAMP_WHERE_NULL = "update wiki.terms4 set rem_df = ? where rem_df is null and tid = ?";
  private final Logger logger = LogManager.getLogger(this.getClass());


  public Term4DAOdb(final JdbcTemplate template) {
    super(template);
  }

  @Override
  @PerformanceMonitored
  public boolean add(List<Term> listOfTerms) {
    // String listOfTidsForBatchUpdates = buildConcatedTidStringFromTermList(listOfTerms);
    // Map<Integer, Integer> oldDfValues = readDfForTid(listOfTidsForBatchUpdates);
    invalidateOldTermEntries(listOfTerms);
    insertNewTermEntriesWithUpdatedDfValues(listOfTerms);
    logger.debug("inserted {} terms into {} table", listOfTerms.size(), this.getClass().getSimpleName());;
    return true;
  }

  private void insertNewTermEntriesWithUpdatedDfValues(List<Term> terms) {
    this.getJdbcTemplate().batchUpdate(SQLStatements.getString("sql.terms4.insert"), new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement stmt, int i) throws SQLException {
        stmt.setInt(1, terms.get(i).getTId());
        stmt.setInt(2, terms.get(i).getDId());
        stmt.setInt(3, terms.get(i).getRevId());
        stmt.setInt(4, terms.get(i).getTrace());
        stmt.setInt(5, terms.get(i).getDict().getDf());
      }

      @Override
      public int getBatchSize() {
        return terms.size();
      }
    });
  }

  private void invalidateOldTermEntries(List<Term> termsToInvalidateDf) {

    getJdbcTemplate().batchUpdate(SET_DF_TIMESTAMP_WHERE_NULL, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setInt(2, termsToInvalidateDf.get(i).getTId());
        ps.setTimestamp(1, getTimetstamp());

      }

      @Override
      public int getBatchSize() {
        return termsToInvalidateDf.size();
      }
    });
  }

  private Map<Integer, Integer> readDfForTid(String listOfTerms) {
    return getJdbcTemplate().query(SQLStatements.getString("sql.terms4.read") + listOfTerms, dfResultMapExtractorForTermId());
  }

  // return concatenated tid list
  private String buildConcatedTidStringFromTermList(List<Term> listOfTerms) {
    return "(" + listOfTerms.stream().map(t -> String.valueOf(t.getTId())).collect(Collectors.joining(",")) + ")";
  }

  private ResultSetExtractor<Map<Integer, Integer>> dfResultMapExtractorForTermId() {
    // retrieve current df values for all dict terms
    return new ResultSetExtractor<Map<Integer, Integer>>() {
      @Override
      public Map<Integer, Integer> extractData(final ResultSet res) throws SQLException, DataAccessException {
        final Map<Integer, Integer> dfValues = new HashMap<Integer, Integer>();
        while (res.next()) {
          dfValues.put(new Integer(res.getInt("tid")), new Integer(res.getInt("df")));
        }
        return dfValues;
      }
    };
  }

  @Override
  public boolean remove(List<Term> listOfTerms) {
    return false;
  }

  @Override
  public boolean update(List<Term> listOfTerms) {
    return false;
  }

  public Timestamp getTimetstamp() {
    return this.timestamp;
  }

  @Override
  public void setTimestamp(Timestamp stamp) {
    this.timestamp = stamp;
  }

  @Override
  public List<Term> read() {
    // TODO Auto-generated method stub
    return null;
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
