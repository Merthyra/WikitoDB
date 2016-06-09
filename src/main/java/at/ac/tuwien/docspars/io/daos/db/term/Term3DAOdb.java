package at.ac.tuwien.docspars.io.daos.db.term;

import at.ac.tuwien.docspars.entity.impl.Term;
import at.ac.tuwien.docspars.io.daos.db.SQLStatements;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Term3DAOdb extends AbstractTermDAOdb {

  public Term3DAOdb(final JdbcTemplate template) {
    super(template);
  }

  @Override
  public boolean add(final List<Term> terms) {

    final int[] updateCounts =
        getJdbcTemplate().batchUpdate(SQLStatements.getString("sql.terms3.insert"), new BatchPreparedStatementSetter() {
          // sql.terms2.insert=INSERT INTO terms (tid, pageid, revid, tf) VALUES (?,?,?,?)
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
    logger.debug("{} inserted {} terms to terms table", Term2DAOdb.class.getTypeName(), updateCounts.length);
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
