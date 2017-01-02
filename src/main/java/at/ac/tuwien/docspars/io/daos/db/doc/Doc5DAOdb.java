package at.ac.tuwien.docspars.io.daos.db.doc;

import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.io.daos.db.SQLStatements;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class Doc5DAOdb extends Doc1DAOdb {

  private Timestamp timestamp = null;

  /**
   * Sets the time to given time.
   *
   * @param time the time to set
   */
  @Override
  public void setTimestamp(final Timestamp time) {
    this.timestamp = time;
  }

  private JdbcTemplate jdbcTemplate;


  public Doc5DAOdb(final JdbcTemplate template) {
    super(template);
  }

  @Override
  public boolean remove(final List<Document> docs) {
    // TODO
    return false;
  }

  @Override
  @PerformanceMonitored
  public boolean add(final List<Document> docs) {
    final int[] updateCounts = this.jdbcTemplate.batchUpdate(getInsertStmnt(), new BatchPreparedStatementSetter() {
      @Override
      public void setValues(final PreparedStatement ps, final int i) throws SQLException {
        // sql.docs.insert=INSERT INTO docs (pageID, added, name, len) VALUES (?,?,?,?)
        ps.setInt(1, docs.get(i).getDId());
        ps.setInt(2, docs.get(i).getRevId());
        // ps.setTimestamp(3, DocReducedDAOdb.this.getTimestamp());
        ps.setString(3, docs.get(i).getName());
        // ps.setInt(5, docs.get(i).getLength());
      }

      @Override
      public int getBatchSize() {
        return docs.size();
      }
    });
    logger.debug("{} inserted {} docs to docs table", Doc5DAOdb.class.getTypeName(), updateCounts.length);
    return docs.size() == updateCounts.length;
  }

  @Override
  @PerformanceMonitored
  public boolean update(final List<Document> docs) {
    final int updateCounts[] =
        this.jdbcTemplate.batchUpdate(SQLStatements.getString("sql.docs.update"), new BatchPreparedStatementSetter() {
          @Override
          public void setValues(final PreparedStatement ps, final int i) throws SQLException {
            // (docid, added, removed, name, len)
            // UPDATE docs SET removed = ? WHERE pageid = ? AND revid = ?
            ps.setTimestamp(1, docs.get(i).getTimestamp());
            ps.setInt(2, docs.get(i).getDId());
            ps.setInt(3, docs.get(i).getDId());
          }

          @Override
          public int getBatchSize() {
            return docs.size();
          }
        });
    final boolean add = add(docs);
    return add && docs.size() == updateCounts.length;
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

  @Override
  public Timestamp getTimestamp() {
    return this.timestamp;
  }

  @Override
  String getInsertStmnt() {
    return SQLStatements.getString("sql.docs5.insert");
  }

  @Override
  String getReadStmnt() {
    return SQLStatements.getString("sql.docs5.read");
  }
}
