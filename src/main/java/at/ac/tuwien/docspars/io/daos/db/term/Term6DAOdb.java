package at.ac.tuwien.docspars.io.daos.db.term;

import at.ac.tuwien.docspars.entity.impl.Term;
import at.ac.tuwien.docspars.io.daos.db.SQLStatements;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Term6DAOdb extends AbstractTermDAOdb {

  private Integer lastVersionId;

  final String QUERY_LAST_VERSION_ID = "SELECT max(revid) FROM VERSIONS";


  public Term6DAOdb(final JdbcTemplate template) {
    super(template);
    this.lastVersionId = getCurrentVersionId().orElse(0);
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

  void setLastVersionId(int versionId) {
    this.lastVersionId = versionId;
  }

  void incrementVersionId() {
    this.lastVersionId++;
  }

  public Optional<Integer> getCurrentVersionId() {
    return Optional.ofNullable(this.getJdbcTemplate().queryForObject(QUERY_LAST_VERSION_ID, Integer.class));
  }

}
