package at.ac.tuwien.docspars.io.daos.db.dict;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.impl.Dict;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.HashMap;
import java.util.Map;

public class DictDAOdb4 extends DictDAOdb {

  private final String READ_CURRENT_DF =
      "select dict.tid, dict.term, docs4.did from dict join terms4 on terms4.tid = dict.tid join docs4 on docs4.did = terms4.did";

  public DictDAOdb4(JdbcTemplate template) {
    super(template);
  }

  @Override
  ResultSetExtractor<Map<String, Dictionable>> getResultSetExtractor() {
    return rs -> {
      final Map<String, Dictionable> dictionary = new HashMap<>();
      while (rs.next()) {
        final String term = rs.getString("term");
        final int tid = rs.getInt("tid");
        final int did = rs.getInt("did");
        dictionary.merge(term, new Dict(tid, term, did), (o, n) -> o.registerDocument(did));
      }
      return dictionary;
    };
  }

  @Override
  String getLookupString() {
    return READ_CURRENT_DF;
  }

}
