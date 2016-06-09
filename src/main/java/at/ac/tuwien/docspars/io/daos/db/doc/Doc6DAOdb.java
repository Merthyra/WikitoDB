package at.ac.tuwien.docspars.io.daos.db.doc;

import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.io.daos.db.SQLStatements;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Doc6DAOdb extends Doc5DAOdb {

  public Doc6DAOdb(final JdbcTemplate template) {
    super(template);
  }

  @Override
  @PerformanceMonitored
  public boolean add(List<Document> docs) {
    final int[] updateCounts =
        getJdbcTemplate().batchUpdate(SQLStatements.getString("sql.docs.update"), getPreparedStatementSetterForDocList(docs));
    logger.debug("{} inserted {} docs to docs table", Doc1DAOdb.class.getTypeName(), updateCounts.length);
    return docs.size() == updateCounts.length;
  }

  private BatchPreparedStatementSetter getPreparedStatementSetterForDocList(List<Document> documents) {
    return new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement stmt, int index) throws SQLException {
        stmt.setInt(1, documents.get(index).getDId());
        stmt.setInt(2, documents.get(index).getRevId());
        stmt.setString(3, documents.get(index).getName());
        stmt.setInt(4, documents.get(index).getLength());
      }

      @Override
      public int getBatchSize() {
        return documents.size();
      }
    };
  }

  @Override
  public boolean update(List<Document> docs) {
    // TODO
    return false;
  }

  @Override
  String getInsertStmnt() {
    return SQLStatements.getString("sql.docs6.insert");
  }

  @Override
  String getReadStmnt() {
    return SQLStatements.getString("sql.docs6.read");
  }

}
