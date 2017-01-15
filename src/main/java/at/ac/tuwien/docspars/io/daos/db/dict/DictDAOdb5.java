package at.ac.tuwien.docspars.io.daos.db.dict;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class DictDAOdb5 extends DictDAOdb4 {

  private final static String READ_CURRENT_DF =
      "select dict.tid, dict.term, docs5.did from wiki.dict join wiki.terms5  on terms5.tid = dict.tid join wiki.docs5 on docs5.did = terms5.did";
  private final Logger logger = LogManager.getLogger(this.getClass());


  public DictDAOdb5(JdbcTemplate template) {
    super(template);
  }

  @Override
  String getLookupString() {
    return DictDAOdb5.READ_CURRENT_DF;
  }

}
