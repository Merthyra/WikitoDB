package at.ac.tuwien.docspars.io.daos.db;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.Timestampable;
import at.ac.tuwien.docspars.io.daos.CrudOperations;
import at.ac.tuwien.docspars.util.CountItemList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.sql.DataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DictHistDAOdb
    implements CrudOperations<Dictionable, Map<String, Integer>>, Timestampable {

  private final JdbcTemplate jdbcTemplate;
  private static final Logger logger = LogManager.getLogger("at.ac.tuwien.docspars.io.db");
  private Timestamp time = null;

  public DictHistDAOdb(final JdbcTemplate template) {
    this.jdbcTemplate = template;
  }

  public DictHistDAOdb(final DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public Map<String, Integer> read() {
    throw new UnsupportedOperationException("History Dictionary should never be read!");
  }

  @Override
  public boolean update(final List<Dictionable> dict) {
    final StringBuilder idsB = new StringBuilder();
    // StringBuilder valuesB = new StringBuilder();
    // concat tid values String for sql retrieval of df values
    final CountItemList<Dictionable> dictsl = (CountItemList<Dictionable>) dict;
    final List<Dictionable> sl = dictsl.singleOccurrenceList();

    for (int i = 0; i < sl.size(); i++) {
      // new dict entries need not be retrieved
      idsB.append(sl.get(i).getTId());
      if (i + 1 < sl.size()) {
        idsB.append(",");
      }
    }

    // retrieve current df values for all dict terms to be updated
    final ResultSetExtractor<Map<Integer, Integer>> resEx =
        new ResultSetExtractor<Map<Integer, Integer>>() {
          @Override
          public Map<Integer, Integer> extractData(final ResultSet res)
              throws SQLException, DataAccessException {
            final Map<Integer, Integer> dfValues = new HashMap<Integer, Integer>(dict.size());
            while (res.next()) {
              dfValues.put(new Integer(res.getInt("tid")), new Integer(res.getInt("df")));
            }
            return dfValues;
          }
        };
    // get all term df values of previously inserted documents sharing the same document id and
    final Map<Integer, Integer> dfUpdatedValues =
        this.jdbcTemplate.query(SQLStatements.getString("sql.dict_hist.readupdated") + " ("
            + idsB.toString() + ") GROUP BY tid", resEx);
    logger.debug(DictHistDAOdb.class.getName() + " extracted " + dfUpdatedValues.size()
        + " dict history updated df values");

    final Map<Integer, Integer> dfValues = this.jdbcTemplate
        .query(SQLStatements.getString("sql.dict_hist.read") + " (" + idsB.toString() + ")", resEx);
    logger.debug(DictHistDAOdb.class.getName() + " extracted " + dfValues.size()
        + " dict history df values");

    // now update all former null values in removed timestamps with timestamp of current batch
    // set removed timestamp for all dict_hist elements affected
    final int nrOfTsUpds = this.jdbcTemplate.update(
        SQLStatements.getString("sql.dict_hist.update") + " (" + idsB.toString() + ")",
        new Object[] {this.getTimestamp()});
    logger.trace(DictHistDAOdb.class.getName() + " updated " + nrOfTsUpds
        + " dict history timestamp values");
    // assert(dfValues.size() == nrOfTsUpds);

    final int[] updateCountsHist = this.jdbcTemplate.batchUpdate(
        SQLStatements.getString("sql.dict_hist.insert"), new BatchPreparedStatementSetter() {
          @Override
          public void setValues(final PreparedStatement ps, final int i) throws SQLException {
            // logger.debug("writing dict hist term " + dicts.get(i).toString() + " " + i);
            ps.setInt(1, sl.get(i).getTId());
            ps.setTimestamp(2, DictHistDAOdb.this.getTimestamp());
            // ps.setTimestamp(3, null);
            Integer df_old = dfValues.get(dict.get(i).getTId());
            Integer df_upd = dfUpdatedValues.get(dict.get(i).getTId());
            if (df_old == null) {
              df_old = 0;
            }
            if (df_upd == null) {
              df_upd = 0;
            }
            // add new values to old df values
            ps.setInt(3, df_old - df_upd + dictsl.getElementCount(sl.get(i)));
          }

          @Override
          public int getBatchSize() {
            return sl.size();
          }
        });
    logger.debug(DictHistDAOdb.class.getName() + " added " + updateCountsHist.length
        + " dict entries to dict history table");
    return true;
  }

  @Override
  public boolean add(final List<Dictionable> dicts) {
    final StringBuilder idsB = new StringBuilder();
    // StringBuilder valuesB = new StringBuilder();
    // concat tid values String for sql retrieval of df values
    final CountItemList<Dictionable> dictsl = (CountItemList<Dictionable>) dicts;
    final List<Dictionable> sl = dictsl.singleOccurrenceList();

    for (int i = 0; i < sl.size(); i++) {
      // new dict entries need not be retrieved
      idsB.append(sl.get(i).getTId());
      if (i + 1 < sl.size()) {
        idsB.append(",");
      }
    }

    // retrieve current df values for all dict terms
    final ResultSetExtractor<Map<Integer, Integer>> resEx =
        new ResultSetExtractor<Map<Integer, Integer>>() {
          @Override
          public Map<Integer, Integer> extractData(final ResultSet res)
              throws SQLException, DataAccessException {
            final Map<Integer, Integer> dfValues = new HashMap<Integer, Integer>(dicts.size());
            while (res.next()) {
              dfValues.put(new Integer(res.getInt("tid")), new Integer(res.getInt("df")));
            }
            return dfValues;
          }
        };

    final Map<Integer, Integer> dfValues = this.jdbcTemplate
        .query(SQLStatements.getString("sql.dict_hist.read") + " (" + idsB.toString() + ")", resEx);
    logger.debug(DictHistDAOdb.class.getName() + " extracted " + dfValues.size()
        + " dict history df values");

    // now update all former null values in removed timestamps with timestamp of current batch
    // set removed timestamp for all dict_hist elements affected
    final int nrOfTsUpds = this.jdbcTemplate.update(
        SQLStatements.getString("sql.dict_hist.update") + " (" + idsB.toString() + ")",
        new Object[] {this.getTimestamp()});
    logger.trace(DictHistDAOdb.class.getName() + " updated " + nrOfTsUpds
        + " dict history timestamp values");
    // assert(dfValues.size() == nrOfTsUpds);

    final int[] updateCountsHist = this.jdbcTemplate.batchUpdate(
        SQLStatements.getString("sql.dict_hist.insert"), new BatchPreparedStatementSetter() {
          @Override
          public void setValues(final PreparedStatement ps, final int i) throws SQLException {
            // logger.debug("writing dict hist term " + dicts.get(i).toString() + " " + i);
            ps.setInt(1, sl.get(i).getTId());
            ps.setTimestamp(2, DictHistDAOdb.this.getTimestamp());
            // ps.setTimestamp(3, null);
            Integer df_old = dfValues.get(dicts.get(i).getTId());
            if (df_old == null) {
              df_old = 0;
            }
            // add new values to old df values
            ps.setInt(3, df_old + dictsl.getElementCount(sl.get(i)));
          }

          @Override
          public int getBatchSize() {
            return sl.size();
          }
        });
    logger.debug(DictHistDAOdb.class.getName() + " added " + updateCountsHist.length
        + " dict entries to dict history table");
    return true;
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

  public void setTimestamp(Timestamp time) {
    this.time = time;
  }
}
