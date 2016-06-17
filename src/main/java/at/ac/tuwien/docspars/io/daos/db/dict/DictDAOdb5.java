package at.ac.tuwien.docspars.io.daos.db.dict;

import org.springframework.jdbc.core.JdbcTemplate;

public class DictDAOdb5 extends DictDAOdb4 {

  private final static String READ_CURRENT_DF =
      "select dict.tid, dict.term, docs4.did from dict join terms4  on terms4.tid = dict.tid join docs4 on docs4.did = terms4.did";

  public DictDAOdb5(JdbcTemplate template) {
    super(template);
  }

  @Override
  String getLookupString() {
    return DictDAOdb5.READ_CURRENT_DF;
  }

}
