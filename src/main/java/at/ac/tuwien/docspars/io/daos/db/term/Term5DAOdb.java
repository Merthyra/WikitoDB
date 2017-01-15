package at.ac.tuwien.docspars.io.daos.db.term;

import at.ac.tuwien.docspars.entity.Timestampable;
import at.ac.tuwien.docspars.entity.impl.Term;
import at.ac.tuwien.docspars.io.daos.db.SQLStatements;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class Term5DAOdb extends AbstractTermDAOdb implements Timestampable {

  private Timestamp timestamp;
  private final Logger logger = LogManager.getLogger(this.getClass());


  public Term5DAOdb(final JdbcTemplate template) {
    super(template);
  }

  @Override
  @PerformanceMonitored
  public boolean add(List<Term> listOfTerms) {
    invalidatedOldTermEntries(listOfTerms);
    insertNewTerms(listOfTerms);
    return true;
  }

  private void insertNewTerms(List<Term> terms) {
    this.getJdbcTemplate().batchUpdate(SQLStatements.getString("sql.terms5.insert"), new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement stmt, int i) throws SQLException {
        stmt.setInt(1, terms.get(i).getTId());
        stmt.setInt(2, terms.get(i).getDId());
        stmt.setInt(3, terms.get(i).getRevId());
        stmt.setTimestamp(4, getTimestamp());
        stmt.setInt(5, terms.get(i).getTrace());
        stmt.setInt(6, terms.get(i).getDict().getDf());
        stmt.setInt(7, terms.get(i).getLength());
      }

      @Override
      public int getBatchSize() {
        return terms.size();
      }
    });
  }

  private void invalidatedOldTermEntries(List<Term> listOfTidsForBatchUpdates) {
    final List<Term> distinctTerms = listOfTidsForBatchUpdates.stream().distinct().collect(Collectors.toList());
    this.getJdbcTemplate().batchUpdate("UPDATE wiki.terms5 SET rem_df = null WHERE rem_df IS null AND tid = ?",
        new BatchPreparedStatementSetter() {

          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setInt(1, distinctTerms.get(i).getDf());
          }

          @Override
          public int getBatchSize() {
            return listOfTidsForBatchUpdates.size();
          }
        });
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
