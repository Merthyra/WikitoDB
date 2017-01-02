package at.ac.tuwien.docspars.io.daos.db.doc;

import at.ac.tuwien.docspars.entity.Timestampable;
import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.io.daos.db.CrudOperations;
import at.ac.tuwien.docspars.io.daos.db.SQLStatements;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Doc1DAOdb implements CrudOperations<Document, Map<Integer, Set<Integer>>>, Timestampable {

  private Timestamp timestamp = null;

  private final JdbcTemplate jdbcTemplate;

  public JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  public Doc1DAOdb(final JdbcTemplate template) {
    this.jdbcTemplate = template;
  }

  @Override
  public Map<Integer, Set<Integer>> read() {
    final ResultSetExtractor<Map<Integer, Set<Integer>>> resEx = extractDocumentDidAndRevid();
    final Map<Integer, Set<Integer>> retrievedDocs = this.jdbcTemplate.query(getReadStmnt(), resEx);
    logger.debug("{} retrieved {} documents from docs table", Doc1DAOdb.class.getName(), retrievedDocs.size());
    return retrievedDocs;
  }

  ResultSetExtractor<Map<Integer, Set<Integer>>> extractDocumentDidAndRevid() {
    final ResultSetExtractor<Map<Integer, Set<Integer>>> resEx = new ResultSetExtractor<Map<Integer, Set<Integer>>>() {
      @Override
      public Map<Integer, Set<Integer>> extractData(final ResultSet res) throws SQLException, DataAccessException {
        final Map<Integer, Set<Integer>> docids = new HashMap<>();
        while (res.next()) {
          final Integer revid = res.getInt("revId");
          docids.merge(
              res.getInt("did"),
              createSetAndAddElem(revid), (oldVal, newVal) -> {
                oldVal.addAll(newVal);
                return oldVal;
              });
        }
        return docids;
      }
    };
    return resEx;
  }

  private Set<Integer> createSetAndAddElem(Integer revId) {
    final Set<Integer> set = new HashSet<>();
    set.add(revId);
    return set;
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
    final int[] updatecounts = this.jdbcTemplate.batchUpdate(getDeleteStmnt(), preparedDocUpdateStmnt(docs));
    return updatecounts.length == docs.size();
  }

  private BatchPreparedStatementSetter preparedDocUpdateStmnt(final List<Document> docs) {
    return new BatchPreparedStatementSetter() {
      @Override
      public void setValues(final PreparedStatement ps, final int i) throws SQLException {
        // sql.docs.insert=INSERT INTO docs (pageID, added, name, len) VALUES (?,?,?,?)
        ps.setTimestamp(1, Doc1DAOdb.this.getTimestamp());
        ps.setInt(2, docs.get(i).getDId());
      }

      @Override
      public int getBatchSize() {
        return docs.size();
      }
    };
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

  String getDeleteStmnt() {
    return SQLStatements.getString("sql.docs1.update");
  }
}

