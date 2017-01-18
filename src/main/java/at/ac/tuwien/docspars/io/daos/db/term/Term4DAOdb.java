package at.ac.tuwien.docspars.io.daos.db.term;

import at.ac.tuwien.docspars.entity.impl.Term;
import at.ac.tuwien.docspars.io.daos.db.SQLStatements;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Term4DAOdb extends AbstractTermDAOdb {

  private final Logger logger = LogManager.getLogger(this.getClass());


  public Term4DAOdb(final JdbcTemplate template) {
    super(template);
  }

  @Override
  @PerformanceMonitored
  public boolean add(List<Term> listOfTerms) {
    invalidateOldTermEntries();
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

  private void invalidateOldTermEntries() {
    // set rem_df for all term entries except of those being of max(did) to the current selection, to have limited size
    getJdbcTemplate().update("UPDATE wiki.terms4 set rem_df = '" + getTimestamp()
        + "' where rem_df is null and not exists "
        + "(select sub.tid, sub.did, inv.df from (select max(did) as did, terms.tid from wiki.terms4 terms "
        + "join wiki.invalidate_dict inv on inv.tid = terms.tid where rem_df is null "
        + "group by terms.tid) as sub "
        + "join wiki.invalidate_dict inv on inv.tid = sub.tid"
        + " where terms4.tid = sub.tid and terms4.did = sub.did)");
  }

  @Override
  public boolean remove(List<Term> listOfTerms) {
    return false;
  }

  @Override
  public boolean update(List<Term> listOfTerms) {
    return false;
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
