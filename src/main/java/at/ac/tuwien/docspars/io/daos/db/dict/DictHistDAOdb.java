package at.ac.tuwien.docspars.io.daos.db.dict;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.io.daos.db.AbstractCrudOperations;
import at.ac.tuwien.docspars.io.daos.db.SQLStatements;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DictHistDAOdb extends AbstractCrudOperations<Dictionable, Map<String, Integer>>
{

  private final JdbcTemplate jdbcTemplate;
  private Timestamp time = null;
  private final Logger logger = LogManager.getLogger(this.getClass());


  public DictHistDAOdb(final JdbcTemplate template) {
    this.jdbcTemplate = template;
  }

  @Override
  public Map<String, Integer> read() {
    throw new UnsupportedOperationException("History Dictionary should never be read!");
  }

  @Override
  @PerformanceMonitored
  public boolean update(final List<Dictionable> dicts) {
    final StringBuilder idsB = new StringBuilder();
    // StringBuilder valuesB = new StringBuilder();
    // concat tid values String for sql retrieval of df values
    // final CountItemList<Dictionable> dictsl = (CountItemList<Dictionable>) dict;
    // final List<Dictionable> sl = dictsl.singleOccurrenceList();

    for (int i = 0; i < dicts.size(); i++) {
      // new dict entries need not be retrieved
      idsB.append(dicts.get(i).getTId());
      if (i + 1 < dicts.size()) {
        idsB.append(",");
      }
    }

    // retrieve current df values for all dict terms to be updated
    final ResultSetExtractor<Map<Integer, Integer>> resEx = extractDfValuesForTids(dicts);
    // get all term df values of previously inserted documents sharing the same document id and
    final Map<Integer, Integer> dfUpdatedValues =
        this.jdbcTemplate.query(SQLStatements.getString("sql.dict_hist.readupdated") + " (" + idsB.toString() + ") GROUP BY tid", resEx);
    logger.debug("{} extracted {} dict history updated df values", DictHistDAOdb.class.getName(), dfUpdatedValues.size());
    final Map<Integer, Integer> dfValues =
        this.jdbcTemplate.query(SQLStatements.getString("sql.dict_hist.read") + " (" + idsB.toString() + ")", resEx);
    logger.debug("{} extracted {} dict history df values", dfValues.size(), DictHistDAOdb.class.getName());

    // now update all former null values in removed timestamps with timestamp of current batch
    // set removed timestamp for all dict_hist elements affected
    final int nrOfTsUpds = this.jdbcTemplate.update(SQLStatements.getString("sql.dict_hist.update") + " (" + idsB.toString() + ")",
        new Object[] {getTimestamp()});
    // logger.trace(DictHistDAOdb.class.getName() + " updated " + nrOfTsUpds + " dict history timestamp values");
    logger.debug("{} updated {} dict history timestamp values", DictHistDAOdb.class.getName(), nrOfTsUpds);
    // assert(dfValues.size() == nrOfTsUpds);

    final int[] updateCountsHist =
        this.jdbcTemplate.batchUpdate(SQLStatements.getString("sql.dict_hist.insert"), new BatchPreparedStatementSetter() {
          @Override
          public void setValues(final PreparedStatement ps, final int i) throws SQLException {
            // logger.debug("writing dict hist term " + dicts.get(i).toString() + " " + i);
            ps.setInt(1, dicts.get(i).getTId());
            ps.setTimestamp(2, DictHistDAOdb.this.getTimestamp());
            // ps.setTimestamp(3, null);
            Integer df_old = dfValues.get(dicts.get(i).getTId());
            Integer df_upd = dfUpdatedValues.get(dicts.get(i).getTId());
            if (df_old == null) {
              df_old = 0;
            }
            if (df_upd == null) {
              df_upd = 0;
            }
            // add new values to old df values
            // ps.setInt(3, df_old - df_upd + dicts.get(i).getTrace());
          }

          @Override
          public int getBatchSize() {
            return dicts.size();
          }
        });
    logger.debug("{} added {} dict entries to dict history table", DictHistDAOdb.class.getName(), updateCountsHist.length);
    return true;
  }

  private ResultSetExtractor<Map<Integer, Integer>> extractDfValuesForTids(final List<Dictionable> dicts) {
    final ResultSetExtractor<Map<Integer, Integer>> resEx = new ResultSetExtractor<Map<Integer, Integer>>() {
      @Override
      public Map<Integer, Integer> extractData(final ResultSet res) throws SQLException, DataAccessException {
        final Map<Integer, Integer> dfValues = new HashMap<Integer, Integer>(dicts.size());
        while (res.next()) {
          dfValues.put(new Integer(res.getInt("tid")), new Integer(res.getInt("df")));
        }
        return dfValues;
      }
    };
    return resEx;
  }

  @Override
  @PerformanceMonitored
  public boolean add(final List<Dictionable> dicts) {

    try {
      dropIntermediateTermsTable();
    } catch (Exception ex) {
      logger.info("no intermediate table present");
    }

    createIntermediateTermsTable(dicts);
    int removed = updateAllNullValuesWithTimestampOfRemoval();
    logger.debug("{} updated {} dict history timestamp values", DictHistDAOdb.class.getName(), removed);

    int added = insertNewDictionaryTermsWithUpdatedDfValues();
    logger.debug("{} added {} dict entries to dict history table", DictHistDAOdb.class.getName(), added);

    dropIntermediateTermsTable();
    return true;
  }

  private int insertNewDictionaryTermsWithUpdatedDfValues() {
    return this.jdbcTemplate
        .update("INSERT INTO wiki.dict_hist (tid, added, df) select tid,'" + getTimestamp() + "', df from wiki.invalidate_dict");
  }

  private void createIntermediateTermsTable(List<Dictionable> dicts) {
    this.jdbcTemplate.batchUpdate("create table wiki.invalidate_dict (tid int, df int)");
    this.jdbcTemplate.batchUpdate("INSERT INTO wiki.invalidate_dict (tid, df) values (?,?)", new BatchPreparedStatementSetter() {

      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setInt(1, dicts.get(i).getTId());
        ps.setInt(2, dicts.get(i).getDf());
      }

      @Override
      public int getBatchSize() {
        return dicts.size();
      }
    });

  }

  private int updateAllNullValuesWithTimestampOfRemoval() {
    return this.jdbcTemplate.update(
        "update wiki.dict_hist set removed = '" + getTimestamp()
            + "' where exists (select * from wiki.invalidate_dict inv where dict_hist.tid = inv.tid and dict_hist.removed is null)");
  }


  private void dropIntermediateTermsTable() {
    this.jdbcTemplate.update("DROP TABLE wiki.invalidate_dict");
  }


  private String buildConcatenatedListOfTids(final List<Dictionable> dicts) {
    return "(" + dicts.stream().map(t -> String.valueOf(t.getTId())).collect(Collectors.joining(",")) + ")";
  }

  private int[] insertNewDictionaryTermsWithUpdatedDfValues(final List<Dictionable> dicts) {
    final int[] updateCountsHist =
        this.jdbcTemplate.batchUpdate(SQLStatements.getString("sql.dict_hist.insert"), new BatchPreparedStatementSetter() {
          @Override
          public void setValues(final PreparedStatement ps, final int i) throws SQLException {
            // logger.debug("writing dict hist term " + dicts.get(i).toString() + " " + i);
            ps.setInt(1, dicts.get(i).getTId());
            ps.setTimestamp(2, DictHistDAOdb.this.getTimestamp());
            // ps.setTimestamp(3, null);
            // Integer df_old = dfValues.get(dicts.get(i).getTId());
            // if (df_old == null) {
            // df_old = 0;
            // }
            // add new values to old df values
            ps.setInt(3, dicts.get(i).getDf());
          }

          @Override
          public int getBatchSize() {
            return dicts.size();
          }
        });
    return updateCountsHist;
  }

  @Override
  public boolean remove(final List<Dictionable> dicts) {
    throw new UnsupportedOperationException("not implmented yet");
  }

  @Override
  public boolean create() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean drop() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Timestamp getTimestamp() {
    return this.time;
  }

  @Override
  public void setTimestamp(final Timestamp time) {
    this.time = time;
  }

}
