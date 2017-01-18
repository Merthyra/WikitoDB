package at.ac.tuwien.docspars.io.daos.db.dict;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.impl.Dict;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DictDAOdb4 extends DictDAOdb {

  private final String READ_CURRENT_DF =
      "SELECT dict.tid, dict.term, terms.df from "
          + "(SELECT tid, count(*) as df FROM wiki.terms4 group by tid) as terms"
          + " JOIN wiki.dict dict ON terms.tid = dict.tid";
  // "select dict.tid, dict.term, docs4.did from "
  // + "wiki.dict join wiki.terms4 on terms4.tid = dict.tid"
  // + " join wiki.docs4 on docs4.did = terms4.did";
  private final Logger logger = LogManager.getLogger(this.getClass());


  public DictDAOdb4(JdbcTemplate template) {
    super(template);
  }

  @Override
  @PerformanceMonitored
  public boolean add(final List<Dictionable> dicts) {
    createIntermediateDictionary(dicts);
    return super.add(dicts);
  }

  @Override
  ResultSetExtractor<Map<String, Dictionable>> getResultSetExtractor() {
    return rs -> {
      final Map<String, Dictionable> dictionary = new HashMap<>();
      while (rs.next()) {
        final String term = rs.getString("term");
        final int tid = rs.getInt("tid");
        final int df = rs.getInt("df");
        dictionary.put(term, new Dict(tid, term, df));
      }
      return dictionary;
    };
  }

  @Override
  String getLookupString() {
    return READ_CURRENT_DF;
  }

}
