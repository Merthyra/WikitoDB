package at.ac.tuwien.docspars.io.daos.db.doc;

import at.ac.tuwien.docspars.io.daos.db.SQLStatements;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class Doc2DAOdb extends Doc1DAOdb {

  private final Logger logger = LogManager.getLogger(this.getClass());

  public Doc2DAOdb(final JdbcTemplate template) {
    super(template);
  }


  @Override
  String getInsertStmnt() {
    return SQLStatements.getString("sql.docs2.insert");
  }

  @Override
  String getReadStmnt() {
    return SQLStatements.getString("sql.docs2.read");
  }

}
