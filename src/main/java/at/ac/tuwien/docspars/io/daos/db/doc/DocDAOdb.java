package at.ac.tuwien.docspars.io.daos.db.doc;

import at.ac.tuwien.docspars.entity.Timestampable;
import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.io.daos.CrudOperations;
import at.ac.tuwien.docspars.io.daos.db.SQLStatements;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.sql.DataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class DocDAOdb implements CrudOperations<Document, TIntSet>, Timestampable {

  private Timestamp timestamp = null;

  private final JdbcTemplate jdbcTemplate;

  public JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  public DocDAOdb(final DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public TIntSet read() {
    final ResultSetExtractor<TIntSet> resEx = getResultsetExtractor();
    final TIntSet retrievedDocs = this.jdbcTemplate.query(getReadStmnt(), resEx);
    logger.debug("{} retrieved {} documents from docs table", DocDAOdb.class.getName(), retrievedDocs.size());
    return retrievedDocs;
  }

  ResultSetExtractor<TIntSet> getResultsetExtractor() {
    final ResultSetExtractor<TIntSet> resEx = new ResultSetExtractor<TIntSet>() {
      @Override
      public TIntSet extractData(final ResultSet res) throws SQLException, DataAccessException {
        final TIntSet docids = new TIntHashSet();
        while (res.next()) {
          docids.add(res.getInt("did"));
        }
        return docids;
      }
    };
    return resEx;
  }

  @Override
  public boolean add(final List<Document> docs) {
    final int[] updateCounts = this.jdbcTemplate.batchUpdate(getInsertStmnt(), getBatchPreparedStatementSetterForList(docs));
    logger.debug("{} inserted {} docs to docs table", DocDAOdb.class.getTypeName(), updateCounts.length);
    return docs.size() == updateCounts.length;
  }

  BatchPreparedStatementSetter getBatchPreparedStatementSetterForList(List<Document> docs) {
    return new BatchPreparedStatementSetter() {
      @Override
      public void setValues(final PreparedStatement ps, final int i) throws SQLException {
        // sql.docs.insert=INSERT INTO docs (pageID, added, name, len) VALUES (?,?,?,?)
        ps.setInt(1, docs.get(i).getDId());
        ps.setInt(2, docs.get(i).getRevId());
        ps.setTimestamp(3, DocDAOdb.this.getTimestamp());
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

