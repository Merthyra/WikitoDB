package at.ac.tuwien.docspars.io.daos.db.doc;

import at.ac.tuwien.docspars.entity.Timestampable;
import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.io.daos.db.CrudOperations;
import at.ac.tuwien.docspars.io.daos.db.SQLStatements;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class Doc1DAOdb implements CrudOperations<Document, TIntSet>, Timestampable {

  private Timestamp timestamp = null;

  private final JdbcTemplate jdbcTemplate;

  public JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  public Doc1DAOdb(final JdbcTemplate template) {
    this.jdbcTemplate = template;
  }

  @Override
  @PerformanceMonitored
  public TIntSet read() {
    final TIntSet retrievedDocs = this.jdbcTemplate.query(getReadStmnt(), getResultsetExtractor());
    logger.debug("{} retrieved {} documents from docs table", Doc1DAOdb.class.getName(), retrievedDocs.size());
    return retrievedDocs;
  }

  ResultSetExtractor<TIntSet> getResultsetExtractor() {
    return res -> {
      final TIntSet docids = new TIntHashSet();
      while (res.next()) {
        docids.add(res.getInt("did"));
      }
      return docids;
    };
  }

  @Override
  @PerformanceMonitored
  public boolean add(final List<Document> docs) {
    final int[] updateCounts = this.jdbcTemplate.batchUpdate(getInsertStmnt(), getBatchPreparedStatementSetterForList(docs));
    logger.debug("{} inserted {} docs to docs table", Doc1DAOdb.class.getTypeName(), updateCounts.length);
    return docs.size() == updateCounts.length;
  }

  BatchPreparedStatementSetter getBatchPreparedStatementSetterForList(List<Document> docs) {
    return new BatchPreparedStatementSetter() {
      @Override
      public void setValues(final PreparedStatement ps, final int i) throws SQLException {
        // sql.docs.insert=INSERT INTO docs (pageID, added, name, len) VALUES (?,?,?,?)
        ps.setInt(1, docs.get(i).getDId());
        ps.setInt(2, docs.get(i).getRevId());
        ps.setTimestamp(3, Doc1DAOdb.this.getTimestamp());
        ps.setString(4, docs.get(i).getName());
        ps.setInt(5, docs.get(i).getLength());
      }

      @Override
      public int getBatchSize() {
        return docs.size();
      }
    };
  }

  @Override
  public boolean remove(final List<Document> docs) {
    return false;
  }

  @Override
  public boolean update(final List<Document> docs) {
    return false;
  }

  @Override
  public boolean create() {
    return false;
  }

  @Override
  public boolean drop() {
    return false;
  }

  @Override
  public Timestamp getTimestamp() {
    return this.timestamp;
  }

  @Override
  public void setTimestamp(final Timestamp time) {
    this.timestamp = time;
  }

  String getInsertStmnt() {
    return SQLStatements.getString("sql.docs1.insert");
  }

  String getReadStmnt() {
    return SQLStatements.getString("sql.docs1.read");
  }
}

