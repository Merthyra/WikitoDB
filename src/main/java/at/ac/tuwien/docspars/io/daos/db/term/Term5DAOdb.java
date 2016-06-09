package at.ac.tuwien.docspars.io.daos.db.term;

import at.ac.tuwien.docspars.entity.Timestampable;
import at.ac.tuwien.docspars.entity.impl.Term;
import at.ac.tuwien.docspars.io.daos.db.SQLStatements;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Term5DAOdb extends AbstractTermDAOdb implements Timestampable {

  private Timestamp timestamp;

  public Term5DAOdb(final JdbcTemplate template) {
    super(template);
  }

  @Override
  public boolean add(List<Term> listOfTerms) {
    String listOfTidsForBatchUpdates = buildConcatedTidStringFromTermList(listOfTerms);
    Map<Integer, Integer> oldDfValues = readDfForTid(listOfTidsForBatchUpdates);
    invalidatedOldTermEntries(listOfTerms.get(0).getTimestamp(), listOfTidsForBatchUpdates);
    insertNewTerms(listOfTerms, oldDfValues);
    return true;
  }

  private void insertNewTerms(List<Term> listOfTerms, Map<Integer, Integer> oldDfValues) {
    this.getJdbcTemplate().batchUpdate(SQLStatements.getString("sql.terms5.insert"), new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement stmt, int i) throws SQLException {
        stmt.setInt(1, listOfTerms.get(i).getTId());
        stmt.setInt(2, listOfTerms.get(i).getDId());
        stmt.setInt(3, listOfTerms.get(i).getRevId());
        stmt.setTimestamp(4, listOfTerms.get(i).getTimestamp());
        stmt.setInt(5, listOfTerms.get(i).getTrace() + oldDfValues.get(listOfTerms.get(i)));
        stmt.setInt(6, listOfTerms.get(i).getLength());
      }

      @Override
      public int getBatchSize() {
        return listOfTerms.size();
      }
    });
  }

  private void invalidatedOldTermEntries(Timestamp invalidatedAt, String listOfTidsForBatchUpdates) {
    this.getJdbcTemplate().update(SQLStatements.getString("sql.terms5.invalidateOldDf") + listOfTidsForBatchUpdates,
        new PreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement stmt) throws SQLException {
            stmt.setTimestamp(1, invalidatedAt);
          }
        });
  }

  private Map<Integer, Integer> readDfForTid(String listOfTerms) {
    return getJdbcTemplate().query(SQLStatements.getString("sql.terms4.read") + listOfTerms, createResultSetExtractor());
  }

  // returns concatenated tid list
  private String buildConcatedTidStringFromTermList(List<Term> listOfTerms) {
    return "(" + listOfTerms.stream().map(t -> String.valueOf(t.getTId())).collect(Collectors.joining(",")) + ")";
  }

  private ResultSetExtractor<Map<Integer, Integer>> createResultSetExtractor() {
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
    throw new UnsupportedOperationException("Updates are not supported for PersistanceMode 5");
  }

  public void setTimestamp(Timestamp stamp) {
    this.timestamp = stamp;
  }

  @Override
  public Timestamp getTimestamp() {
    return this.timestamp;
  }

}
