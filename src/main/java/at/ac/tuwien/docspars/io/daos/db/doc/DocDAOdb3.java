package at.ac.tuwien.docspars.io.daos.db.doc;

import at.ac.tuwien.docspars.io.daos.db.SQLStatements;

import javax.sql.DataSource;

public class DocDAOdb3 extends DocDAOdb {

  public DocDAOdb3(DataSource dataSource) {
    super(dataSource);
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
