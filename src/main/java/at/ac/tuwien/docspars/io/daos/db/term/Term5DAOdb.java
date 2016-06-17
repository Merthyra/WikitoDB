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
import java.util.Optional;
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
    invalidatedOldTermEntries(getTimestamp(), listOfTidsForBatchUpdates);
    insertNewTerms(listOfTerms, oldDfValues);
    return true;
  }

  private void insertNewTerms(List<Term> terms, Map<Integer, Integer> oldDfValues) {
    this.getJdbcTemplate().batchUpdate(SQLStatements.getString("sql.terms5.insert"), new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement stmt, int i) throws SQLException {
        stmt.setInt(1, terms.get(i).getTId());
        stmt.setInt(2, terms.get(i).getDId());
        stmt.setInt(3, terms.get(i).getRevId());
        stmt.setTimestamp(4, getTimestamp());
        Optional<Integer> oldDf = Optional.ofNullable(oldDfValues.get(terms.get(i).getTId()));
        stmt.setInt(5, terms.get(i).getTrace());
        stmt.setInt(6, terms.get(i).getDict().getDf() + oldDf.orElse(0));
        stmt.setInt(7, terms.get(i).getLength());
      }

      @Override
      public int getBatchSize() {
        return terms.size();
      }
    });
  }

  private void invalidatedOldTermEntries(Timestamp invalidatedAt, String listOfTidsForBatchUpdates) {
    this.getJdbcTemplate().update(SQLStatements.getString("sql.terms5.update") + listOfTidsForBatchUpdates,
        new PreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement stmt) throws SQLException {
            stmt.setTimestamp(1, invalidatedAt);
          }
        });
  }

  private Map<Integer, Integer> readDfForTid(String listOfTerms) {
    return getJdbcTemplate().query(SQLStatements.getString("sql.terms4.read") + listOfTerms, resultsetExtractorMapForDfAndTermId());
  }

  // returns concatenated tid list
  private String buildConcatedTidStringFromTermList(List<Term> listOfTerms) {
    return "(" + listOfTerms.stream().map(t -> String.valueOf(t.getTId())).collect(Collectors.joining(",")) + ")";
  }

  private ResultSetExtractor<Map<Integer, Integer>> resultsetExtractorMapForDfAndTermId() {
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

  @Override
  public void setTimestamp(Timestamp stamp) {
    this.timestamp = stamp;
  }

  @Override
  public Timestamp getTimestamp() {
    return this.timestamp;
  }

}
