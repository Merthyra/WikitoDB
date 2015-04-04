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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import at.ac.tuwien.docspars.entity.Dict;
import at.ac.tuwien.docspars.entity.SimpleDict;
import at.ac.tuwien.docspars.io.daos.DictDAO;

public class SC1DictDAODocsDB implements DictDAO{
	
	private static final Logger logger = LogManager.getLogger(SC1DictDAODocsDB.class.getName());
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings("unused")
	private SC1DictDAODocsDB() {
		
	}
	
	public SC1DictDAODocsDB(DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}
	
	@Override
	public Map<String, Dict> getAll() {
		
		ResultSetExtractor<Map<String, Dict>> resEx = new ResultSetExtractor<Map<String, Dict>>() {	
			@Override
			public Map<String, Dict> extractData(ResultSet res) throws SQLException, DataAccessException {
				HashMap<String, Dict> dict = new HashMap<String,Dict>();
				while (res.next()) {
					dict.put(res.getString("term"), new SimpleDict(res.getLong("tid"), res.getString("term")));
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
	public boolean add(List<Dict> dicts){		
		long time = System.currentTimeMillis();
		try {	
		PreparedStatement prepStmt = jdbcTemplate.getDataSource().getConnection().prepareStatement(SQLStatements.getString("sql.dict.insert_SC1"), PreparedStatement.RETURN_GENERATED_KEYS);		
		prepStmt.getConnection().setAutoCommit(false);
		for (Dict dic : dicts) {
			prepStmt.setString(1, dic.getTerm());
			prepStmt.addBatch();
		}
	
		int[] updates = prepStmt.executeBatch();
		
		ResultSet rs = prepStmt.getGeneratedKeys();
		// check if all inserts where successful
		if (updates.length != dicts.size()) {
			// all rows have to be inserted, consistency cannot be assured otherwise			
			throw new SQLException("Error writing dict batch with expected size <" + dicts.size() + "> but has size of <" + updates.length + ">");					
		}
		//commit insert statements
		prepStmt.getConnection().commit();
		prepStmt.getConnection().setAutoCommit(true);
		// loop through generated keyset (might not work with all jdbc drivers - there is no confirmed standard) for updating dict entries with corresponding database keys
		int dictEntry = 0;
		while (rs.next()) {
			// setting dict primary key for consistency
			dicts.get(dictEntry++).setId(rs.getLong(1));
		}
		}
		catch (SQLException e) {
			throw new RuntimeException("Error writing all dict entries -> rolling back transaction : " + e.getCause());
		}
		return true;
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
