package at.ac.tuwien.docspars.io.daos.db.term;

import at.ac.tuwien.docspars.entity.impl.Term;
import at.ac.tuwien.docspars.io.daos.db.SQLStatements;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class Term1DAOdb extends AbstractTermDAOdb {

  public Term1DAOdb(final JdbcTemplate template) {
    super(template);
  }

  @Override
  @PerformanceMonitored
  public boolean add(final List<Term> terms) {

    final int[] updateCounts =
        getJdbcTemplate().batchUpdate(SQLStatements.getString("sql.terms1.insert"), new BatchPreparedStatementSetter() {
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
    logger.debug("{} inserted {} terms to terms table", Term1DAOdb.class.getTypeName(), updateCounts.length);
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

  @Override
  public void setTimestamp(Timestamp stamp) {
    // TODO Auto-generated method stub

  }

}
