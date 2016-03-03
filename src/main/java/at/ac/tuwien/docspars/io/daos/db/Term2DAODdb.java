package at.ac.tuwien.docspars.io.daos.db;

import at.ac.tuwien.docspars.entity.impl.TrceRevTerm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Term2DAODdb extends AbstractTermDAOdb<TrceRevTerm> {

  private static final Logger logger = LogManager.getLogger("at.ac.tuwien.docspars.io.db");
  private JdbcTemplate jdbcTemplate;

  @SuppressWarnings("unused")
  @Deprecated
  private Term2DAODdb() {
    super();
  }

  public Term2DAODdb(final JdbcTemplate template) {
    this.jdbcTemplate = template;
  }

  public Term2DAODdb(final DataSource ds) {
    this.jdbcTemplate = new JdbcTemplate(ds);
  }

  @Override
  public boolean add(final List<TrceRevTerm> terms) {

    final int[] updateCounts = this.jdbcTemplate.batchUpdate(
        SQLStatements.getString("sql.terms2.insert"), new BatchPreparedStatementSetter() {
          // sql.terms2.insert=INSERT INTO terms (tid, pageid, revid, tf) VALUES (?,?,?,?)
          @Override
          public void setValues(final PreparedStatement ps, final int i) throws SQLException {
            ps.setInt(1, terms.get(i).getTId());
            ps.setInt(2, terms.get(i).getDId());
            ps.setInt(3, terms.get(i).getRevId());
            ps.setInt(4, terms.get(i).getFrequency());
          }

          @Override
          public int getBatchSize() {
            return terms.size();
          }
        });
    logger.debug(
        Term2DAODdb.class.getName() + " inserted " + updateCounts.length + " terms to terms table");
    return updateCounts.length == terms.size();
  }

  @Override
  public boolean remove(final List<TrceRevTerm> a) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean update(final List<TrceRevTerm> a) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public List<TrceRevTerm> read() {
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
