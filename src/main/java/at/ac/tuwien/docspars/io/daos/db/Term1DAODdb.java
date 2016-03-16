package at.ac.tuwien.docspars.io.daos.db;

import at.ac.tuwien.docspars.entity.impl.Term;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Term1DAODdb extends AbstractTermDAOdb {

  private static final Logger logger = LogManager.getLogger("at.ac.tuwien.docspars.io.db");
  private JdbcTemplate jdbcTemplate;

  @SuppressWarnings("unused")
  @Deprecated
  private Term1DAODdb() {
    super();
  }

  public Term1DAODdb(final DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  public Term1DAODdb(final JdbcTemplate template) {
    this.jdbcTemplate = template;
  }

  @Override
  public boolean add(final List<Term> terms) {

    final int[] updateCounts = this.jdbcTemplate.batchUpdate(SQLStatements.getString("sql.terms1.insert"), new BatchPreparedStatementSetter() {
      // INSERT INTO terms (tid, pageid, pos) VALUES (?,?,?,?)
      @Override
      public void setValues(final PreparedStatement ps, final int i) throws SQLException {
        ps.setInt(1, terms.get(i).getTId());
        ps.setInt(2, terms.get(i).getDId());
        ps.setInt(3, terms.get(i).getRevId());
        ps.setInt(4, terms.get(i).getTrace());
      }

      @Override
      public int getBatchSize() {
        return terms.size();
      }
    });
    logger.debug(Term1DAODdb.class.getName() + " inserted " + updateCounts.length + " terms to terms table");
    return updateCounts.length == terms.size();
  }

  @Override
  public boolean remove(final List<Term> a) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean update(final List<Term> a) {
    // TODO Auto-generated method stub
    return false;
  }

}
