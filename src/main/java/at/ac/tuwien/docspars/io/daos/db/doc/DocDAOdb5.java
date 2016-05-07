package at.ac.tuwien.docspars.io.daos.db.doc;

import at.ac.tuwien.docspars.entity.impl.Document;
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

public class DocDAOdb5 extends DocDAOdb {

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


  public DocDAOdb5(final DataSource dataSource) {
    super(dataSource);
  }

  @Override
  public TIntSet read() {
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
    final TIntSet retrievedDocs = this.jdbcTemplate.query(SQLStatements.getString("sql.docs.read"), resEx);
    logger.debug(DocDAOdb5.class.getName() + " retrieved " + retrievedDocs.size() + " documents from docs table");
    return retrievedDocs;
  }

  @Override
  public boolean remove(final List<Document> docs) {
    // TODO
    return false;
  }

  @Override
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
    logger.debug("{} inserted {} docs to docs table", DocDAOdb5.class.getTypeName(), updateCounts.length);
    return docs.size() == updateCounts.length;
  }

  @Override
  public boolean update(final List<Document> docs) {
    final int updateCounts[] = this.jdbcTemplate.batchUpdate(SQLStatements.getString("sql.docs.update"), new BatchPreparedStatementSetter() {
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