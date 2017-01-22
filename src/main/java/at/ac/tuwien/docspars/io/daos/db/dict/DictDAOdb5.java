package at.ac.tuwien.docspars.io.daos.db.dict;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class DictDAOdb5 extends DictDAOdb4 {

  private final static String READ_CURRENT_DF =
      "select dict.tid, dict.term, terms5.df from wiki.dict join wiki.terms5  on terms5.tid = dict.tid where rem_df is null";
  private final Logger logger = LogManager.getLogger(this.getClass());


  public DictDAOdb5(JdbcTemplate template) {
    super(template);
  }

  @Override
  @PerformanceMonitored
  public boolean add(final List<Dictionable> dicts) {
    createIntermediateDictionary(dicts);
    return super.add(dicts);
  }

  @Override
  String getLookupString() {
    return DictDAOdb5.READ_CURRENT_DF;
  }

}
