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

public class Term5DAOdb extends AbstractTermDAOdb implements Timestampable {

  private Timestamp timestamp;
  private final Logger logger = LogManager.getLogger(this.getClass());


  public Term5DAOdb(final JdbcTemplate template) {
    super(template);
  }

  @Override
  @PerformanceMonitored
  public boolean add(List<Term> listOfTerms) {
    invalidatedOldTermEntries();
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

  private void invalidatedOldTermEntries() {
    getJdbcTemplate().update("UPDATE wiki.terms5 set rem_df = '" + getTimestamp()
        + "' where rem_df is null and not exists "
        + "(select sub.tid, sub.did, inv.df from (select max(did) as did, terms.tid from wiki.terms5 terms "
        + "join wiki.invalidate_dict inv on inv.tid = terms.tid where rem_df is null "
        + "group by terms.tid) as sub "
        + "join wiki.invalidate_dict inv on inv.tid = sub.tid"
        + " where terms5.tid = sub.tid and terms5.did = sub.did)");
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
