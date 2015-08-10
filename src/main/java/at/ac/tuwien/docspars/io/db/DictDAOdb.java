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

import at.ac.tuwien.docspars.entity.Batch;
import at.ac.tuwien.docspars.entity.Dict;
import at.ac.tuwien.docspars.entity.Term;
import at.ac.tuwien.docspars.io.daos.DictDAO;

public class DictDAOdb implements DictDAO {

	private static final Logger logger = LogManager.getLogger("at.ac.tuwien.docspars.io.db");
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings("unused")
	private DictDAOdb() {

	}

	public DictDAOdb(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public DictDAOdb(JdbcTemplate template) {
		this.jdbcTemplate = template;
	}

	@Override
	public Map<String, Integer> read() {
		ResultSetExtractor<Map<String, Integer>> resEx = new ResultSetExtractor<Map<String, Integer>>() {
			@Override
			public Map<String, Integer> extractData(ResultSet res) throws SQLException, DataAccessException {
				HashMap<String, Integer> dict = new HashMap<String, Integer>();
				while (res.next()) {
					dict.put(res.getString("term"), new Integer(res.getInt("tid")));
				}
				return dict;
			}
		};

		Map<String, Integer> dicts = this.jdbcTemplate.query(SQLStatements.getString("sql.dict.read"), resEx);
		logger.debug(DictDAOdb.class.getName() + " retrieved " + dicts.size() + " dict entries from dict table");
		return dicts;
	}

	@Override
	public boolean add(List<Dict> dicts) {
		int[] updateCounts = jdbcTemplate.batchUpdate(SQLStatements.getString("sql.dict.insert"),
		new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				logger.trace("writing dict term " + dicts.get(i).toString() + " " + i);
				ps.setInt(1, dicts.get(i).getId());
				ps.setString(2, dicts.get(i).getTerm());
			}
			public int getBatchSize() {
				return dicts.size();
			}
		});
		logger.debug(DictDAOdb.class.getName() + " added " + updateCounts.length + " dict entries to dict table");
		return updateCounts.length == dicts.size();
	}

	@Override
	public boolean remove(List<Dict> dicts) {
		throw new UnsupportedOperationException("not removes from dict table intended");
	}


	@Override
	public boolean update(List<Dict> dicts) {
		throw new UnsupportedOperationException("no updates on dict table intended");
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

}
