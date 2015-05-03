package at.ac.tuwien.docspars.io.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import at.ac.tuwien.docspars.entity.Dict;
import at.ac.tuwien.docspars.entity.SimpleDict;
import at.ac.tuwien.docspars.io.daos.DictDAO;
import at.ac.tuwien.docspars.io.services.PersistanceService;

public class SC1DictDAODocsDB implements DictDAO{

	private static final Logger logger = LogManager.getLogger(PersistanceService.class.getName());
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings("unused")
	private SC1DictDAODocsDB() {

	}

	public SC1DictDAODocsDB(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Map<String, Dict> getAll() {
		ResultSetExtractor<Map<String, Dict>> resEx = new ResultSetExtractor<Map<String, Dict>>() {	
			@Override
			public Map<String, Dict> extractData(ResultSet res) throws SQLException, DataAccessException {
				HashMap<String, Dict> dict = new HashMap<String,Dict>();
				while (res.next()) {
					dict.put(res.getString("term"), new SimpleDict(res.getInt("tid"), res.getString("term")));
				}
				return dict;		
			}
		};
		return (Map<String,Dict>) this.jdbcTemplate.query(SQLStatements.getString("sql.dict.read"),resEx);
	}

	@Override
	public boolean update(Dict dict) {
		throw new UnsupportedOperationException("not implmented yet");
	}

	@Override
	public boolean add(final List<Dict> dicts){
		logger.trace(SQLStatements.getString("sql.dict.insert_SC1"));
		int[] updateCounts = jdbcTemplate.batchUpdate(SQLStatements.getString("sql.dict.insert_SC1"),

		new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				logger.trace("writing dict term " + dicts.get(i).toString() +  " " + i);
				ps.setInt(1, dicts.get(i).getId());
				ps.setString(2, dicts.get(i).getTerm());
			}

			public int getBatchSize() {
				return dicts.size();
			}
		});
		return updateCounts.length == dicts.size();
	}


	@Override
	public boolean remove(List<Dict> dicts) {
		throw new UnsupportedOperationException("not implmented yet");
	}

	@Override
	public long getNextTermID() {
		throw new UnsupportedOperationException("not implmented yet");
	}

}
