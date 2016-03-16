package at.ac.tuwien.docspars.io.daos.db;

import at.ac.tuwien.docspars.entity.Timestampable;
import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.io.daos.CrudOperations;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

  private static final Logger logger = LogManager.getLogger("at.ac.tuwien.docspars.io.db");
  private Timestamp timestamp = null;

  /**
   * Sets the time to given time.
   *
   * @param time the time to set
   */
  public void setTimestamp(final Timestamp time) {
    this.timestamp = time;
  }

  private JdbcTemplate jdbcTemplate;

  @SuppressWarnings("unused")
  private DocDAOdb() {

  }

  public DocDAOdb(final DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public TIntSet read() {
    final ResultSetExtractor<TIntSet> resEx = new ResultSetExtractor<TIntSet>() {
      @Override
      public TIntSet extractData(final ResultSet res) throws SQLException, DataAccessException {
        final TIntSet docids = new TIntHashSet();
        while (res.next()) {
          // Document doc = new Document(res.getInt("pageid"), res.getInt("revid"),
          // res.getString("name"),
          // res.getTimestamp("added"), res.getInt("len"));
          docids.add(res.getInt("did"));
        }
        return docids;
      }
    };
    final TIntSet retrievedDocs = this.jdbcTemplate.query(SQLStatements.getString("sql.docs.read"), resEx);
    logger.debug(DocDAOdb.class.getName() + " retrieved " + retrievedDocs.size() + " documents from docs table");
    return retrievedDocs;
  }

  @Override
  public boolean remove(final List<Document> docs) {
    final int[] updateCounts = this.jdbcTemplate.batchUpdate(SQLStatements.getString("sql.docs.update"), new BatchPreparedStatementSetter() {
      @Override
      public void setValues(final PreparedStatement ps, final int i) throws SQLException {
        // sql.docs.insert=INSERT INTO docs (pageID, revID, added, name, len) VALUES (?,?,?,?,?)
        ps.setTimestamp(1, DocDAOdb.this.getTimestamp());
        ps.setInt(2, docs.get(i).getDId());
        ps.setInt(3, docs.get(i).getDId());
      }

      @Override
      public int getBatchSize() {
        return docs.size();
      }
    });
    logger.debug(DocDAOdb.class.getName() + " removed/updated " + docs.size() + " documents from docs table");
    return docs.size() == updateCounts.length;
  }

  @Override
  public boolean add(final List<Document> docs) {
    final int[] updateCounts = this.jdbcTemplate.batchUpdate(SQLStatements.getString("sql.docs.insert"), new BatchPreparedStatementSetter() {
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
    });
    logger.debug(DocDAOdb.class.getName() + " inserted " + docs.size() + " documents in docs table");
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
}
