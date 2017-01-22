package at.ac.tuwien.docspars.io.daos.db.term;

import static at.ac.tuwien.docspars.io.daos.db.SQLStatements.*;

import at.ac.tuwien.docspars.entity.impl.Term;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Term6DAOdb extends AbstractTermDAOdb {

  private Integer vid;
  private final Logger logger = LogManager.getLogger(this.getClass());


  public Term6DAOdb(final JdbcTemplate template) {
    super(template);
  }

  @Override
  @PerformanceMonitored
  public boolean add(List<Term> terms) {
    writeTersmsToTermsTable(terms);
    return true;
  }

  private void writeTersmsToTermsTable(List<Term> terms) {
    this.getJdbcTemplate().batchUpdate(getString("sql.terms6.insert"), getBatchPreparedStatementSetterForList(terms));

  }

  @Override
  public boolean remove(List<Term> a) {
    return false;
  }

  @Override
  public boolean update(List<Term> a) {
    return false;
  }

  private BatchPreparedStatementSetter getBatchPreparedStatementSetterForList(List<Term> terms) {
    final Integer currentVid = (Integer) getParameter("vid");
    return new BatchPreparedStatementSetter() {

      @Override
      public void setValues(PreparedStatement stmt, int index) throws SQLException {
        stmt.setInt(1, terms.get(index).getTId());
        stmt.setInt(2, terms.get(index).getDId());
        stmt.setInt(3, currentVid);
        stmt.setInt(4, terms.get(index).getTrace());
      }

      @Override
      public int getBatchSize() {

        return terms.size();
      }
    };
  }

  public Integer getVid() {
    return vid;
  }

  public void setVid(Integer vid) {
    this.vid = vid;
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
