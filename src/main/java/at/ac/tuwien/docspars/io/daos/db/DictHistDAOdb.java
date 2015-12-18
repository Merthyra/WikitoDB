package at.ac.tuwien.docspars.io.daos.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.io.daos.DictHistDAO;
import at.ac.tuwien.docspars.util.CountItemList;

public class DictHistDAOdb implements DictHistDAO {

	private JdbcTemplate jdbcTemplate;
	private static final Logger logger = LogManager.getLogger("at.ac.tuwien.docspars.io.db");
	private Timestamp time = null;

	public DictHistDAOdb(JdbcTemplate template) {
		this.jdbcTemplate = template;
	}

	public DictHistDAOdb(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Map<String, Integer> read() {
		throw new UnsupportedOperationException("History Dictionary should never be read!");
	}

	@Override
	public boolean update(List<Dictionable> dict) {
		StringBuilder idsB = new StringBuilder();
		// StringBuilder valuesB = new StringBuilder();
		// concat tid values String for sql retrieval of df values
		CountItemList<Dictionable> dictsl = (CountItemList<Dictionable>) dict;
		List<Dictionable> sl =  dictsl.singleOccurrenceList(); 	
		
		for (int i = 0; i<sl.size(); i++) {
			// new dict entries need not be retrieved
			idsB.append(sl.get(i).getTid());
			if (i+1 < sl.size()) idsB.append(",");
		}
		
		// retrieve current df values for all dict terms to be updated
		ResultSetExtractor<Map<Integer, Integer>> resEx = new ResultSetExtractor<Map<Integer, Integer>>() {
			@Override
			public Map<Integer, Integer> extractData(ResultSet res) throws SQLException, DataAccessException {
				Map<Integer, Integer> dfValues = new HashMap<Integer, Integer>(dict.size());
				while (res.next()) {
					dfValues.put(new Integer(res.getInt("tid")), new Integer(res.getInt("df")));
				}
				return dfValues;
			}
		};
		// get all term df values of previously inserted documents sharing the same document id and 
		Map<Integer, Integer> dfUpdatedValues = this.jdbcTemplate.query(SQLStatements.getString("sql.dict_hist.readupdated") + " (" + idsB.toString() + ") GROUP BY tid", resEx);
		logger.debug(DictHistDAOdb.class.getName() + " extracted " + dfUpdatedValues.size() + " dict history updated df values");

		Map<Integer, Integer> dfValues = this.jdbcTemplate.query(SQLStatements.getString("sql.dict_hist.read") + " (" + idsB.toString() + ")", resEx);
		logger.debug(DictHistDAOdb.class.getName() + " extracted " + dfValues.size() + " dict history df values");
		
		// now update all former null values in removed timestamps with timestamp of current batch
		// set removed timestamp for all dict_hist elements affected
		int nrOfTsUpds = jdbcTemplate.update(SQLStatements.getString("sql.dict_hist.update") + " (" + idsB.toString() + ")", new Object[] { getTimestamp()});
		logger.trace(DictHistDAOdb.class.getName() + " updated " + nrOfTsUpds + " dict history timestamp values");
		// assert(dfValues.size() == nrOfTsUpds);

		int[] updateCountsHist = jdbcTemplate.batchUpdate(SQLStatements.getString("sql.dict_hist.insert"), new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				//logger.debug("writing dict hist term " + dicts.get(i).toString() + " " + i);
				ps.setInt(1, sl.get(i).getTid());
				ps.setTimestamp(2, getTimestamp());
//				ps.setTimestamp(3, null);
				Integer df_old = dfValues.get(dict.get(i).getTid());
				Integer df_upd = dfUpdatedValues.get(dict.get(i).getTid());
				if (df_old == null) df_old = 0;
				if (df_upd == null) df_upd = 0;
				// add new values to old df values
				ps.setInt(3, df_old - df_upd + dictsl.getElementCount(sl.get(i)));
			}

			public int getBatchSize() {
				return sl.size();
			}
		});
		logger.debug(DictHistDAOdb.class.getName() + " added " + updateCountsHist.length + " dict entries to dict history table");
		return true;
	}

	@Override
	public boolean add(List<Dictionable> dicts) {
		StringBuilder idsB = new StringBuilder();
		// StringBuilder valuesB = new StringBuilder();
		// concat tid values String for sql retrieval of df values
		CountItemList<Dictionable> dictsl = (CountItemList<Dictionable>) dicts;
		List<Dictionable> sl =  dictsl.singleOccurrenceList(); 	
		
		for (int i = 0; i<sl.size(); i++) {
			// new dict entries need not be retrieved
			idsB.append(sl.get(i).getTid());
			if (i+1 < sl.size()) idsB.append(",");
		}
		
		// retrieve current df values for all dict terms
		ResultSetExtractor<Map<Integer, Integer>> resEx = new ResultSetExtractor<Map<Integer, Integer>>() {
			@Override
			public Map<Integer, Integer> extractData(ResultSet res) throws SQLException, DataAccessException {
				Map<Integer, Integer> dfValues = new HashMap<Integer, Integer>(dicts.size());
				while (res.next()) {
					dfValues.put(new Integer(res.getInt("tid")), new Integer(res.getInt("df")));
				}
				return dfValues;
			}
		};

		Map<Integer, Integer> dfValues = this.jdbcTemplate.query(SQLStatements.getString("sql.dict_hist.read") + " (" + idsB.toString() + ")", resEx);
		logger.debug(DictHistDAOdb.class.getName() + " extracted " + dfValues.size() + " dict history df values");

		// now update all former null values in removed timestamps with timestamp of current batch
		// set removed timestamp for all dict_hist elements affected
		int nrOfTsUpds = jdbcTemplate.update(SQLStatements.getString("sql.dict_hist.update") + " (" + idsB.toString() + ")", new Object[] { getTimestamp()});
		logger.trace(DictHistDAOdb.class.getName() + " updated " + nrOfTsUpds + " dict history timestamp values");
		// assert(dfValues.size() == nrOfTsUpds);

		int[] updateCountsHist = jdbcTemplate.batchUpdate(SQLStatements.getString("sql.dict_hist.insert"), new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				//logger.debug("writing dict hist term " + dicts.get(i).toString() + " " + i);
				ps.setInt(1, sl.get(i).getTid());
				ps.setTimestamp(2, getTimestamp());
//				ps.setTimestamp(3, null);
				Integer df_old = dfValues.get(dicts.get(i).getTid());
				if (df_old == null)
					df_old = 0;
				// add new values to old df values
				ps.setInt(3, df_old + dictsl.getElementCount(sl.get(i)));
			}

			public int getBatchSize() {
				return sl.size();
			}
		});
		logger.debug(DictHistDAOdb.class.getName() + " added " + updateCountsHist.length + " dict entries to dict history table");
		return true;
	}

	@Override
	public boolean remove(List<Dictionable> dicts) {
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
	public void setTimestamp(Timestamp time) {
		this.time = time;
		
	}

	@Override
	public Timestamp getTimestamp() {
		return this.time;
	}

}
