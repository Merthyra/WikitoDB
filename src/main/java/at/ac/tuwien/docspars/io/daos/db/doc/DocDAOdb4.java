package at.ac.tuwien.docspars.io.daos.db.doc;

import at.ac.tuwien.docspars.io.daos.db.SQLStatements;

import javax.sql.DataSource;

public class DocDAOdb4 extends DocDAOdb {

  public DocDAOdb4(DataSource dataSource) {
    super(dataSource);
  }

  @Override
  String getInsertStmnt() {
    return SQLStatements.getString("sql.docs4.insert");
  }


  @Override
  String getReadStmnt() {
    return SQLStatements.getString("sql.docs4.read");
  }

}