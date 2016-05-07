package at.ac.tuwien.docspars.io.daos.db.term;

import at.ac.tuwien.docspars.entity.impl.Term;
import at.ac.tuwien.docspars.io.daos.db.SQLStatements;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import javax.sql.DataSource;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Term6DAODb extends AbstractTermDAOdb {

  private int lastVersionId = 0;

  public Term6DAODb(DataSource ds) {
    super(ds);
  }

  @Override
  public boolean add(List<Term> terms) {
    // first obtain very last element from versions table, if not already present
    // secondly write records into the tables
    writeTersmsToTermsTable(terms);
    return true;
  }

  private void writeTersmsToTermsTable(List<Term> terms) {
    this.getJdbcTemplate().batchUpdate(SQLStatements.getString("sql.terms6.insert"), getBatchPreparedStatementSetterForList(terms));

  }

  @Override
  public boolean remove(List<Term> a) {
    return false;
  }

  @Override
  public boolean update(List<Term> a) {
    return false;
  }

  public void setLatestVersion(int latestVersion) {
    this.lastVersionId = latestVersion;
  }

  private BatchPreparedStatementSetter getBatchPreparedStatementSetterForList(List<Term> terms) {
    return new BatchPreparedStatementSetter() {

      @Override
      public void setValues(PreparedStatement stmt, int index) throws SQLException {
        stmt.setInt(1, terms.get(index).getTId());
        stmt.setInt(2, terms.get(index).getDId());
        stmt.setInt(3, lastVersionId);
        stmt.setInt(4, terms.get(index).getTrace());
      }

      @Override
      public int getBatchSize() {

        return terms.size();
      }
    };
  }
}
