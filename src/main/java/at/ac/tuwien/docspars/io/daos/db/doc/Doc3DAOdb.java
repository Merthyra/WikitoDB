package at.ac.tuwien.docspars.io.daos.db.doc;

import at.ac.tuwien.docspars.io.daos.db.SQLStatements;
import org.springframework.jdbc.core.JdbcTemplate;

public class Doc3DAOdb extends Doc1DAOdb {

  public Doc3DAOdb(final JdbcTemplate template) {
    super(template);
  }

  @Override
  String getInsertStmnt() {
    return SQLStatements.getString("sql.docs3.insert");
  }


  @Override
  String getReadStmnt() {
    return SQLStatements.getString("sql.docs3.read");
  }

}
