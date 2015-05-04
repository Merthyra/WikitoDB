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
import at.ac.tuwien.docspars.entity.TimestampedDict;
import at.ac.tuwien.docspars.io.daos.DictDAO;

public class SC2DictDAODocsDB implements DictDAO {

	private JdbcTemplate jdbcTemplate;
	private static final Logger logger = LogManager.getLogger("at.ac.tuwien.docspars.io.db");

	public SC2DictDAODocsDB(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Map<String, Dict> getAll() {
		ResultSetExtractor<Map<String, Dict>> resEx = new ResultSetExtractor<Map<String, Dict>>() {
			@Override
			public Map<String, Dict> extractData(ResultSet res) throws SQLException, DataAccessException {
				HashMap<String, Dict> dict = new HashMap<String, Dict>();
				while (res.next()) {
					dict.put(res.getString("term"), new TimestampedDict(res.getInt("tid"), res.getString("term"), res.getTimestamp("added"), res.getInt("df")));
				}
				return dict;
			}

		};
		Map<String, Dict> dicts = this.jdbcTemplate.query(SQLStatements.getString("sql.dict.read"), resEx);
		logger.debug(SC2DictDAODocsDB.class.getName() + " retrieved " + dicts.size() + " dict entries from dict table");
		return dicts;
	}

	@Override
	public boolean update(Dict dict) {
		throw new UnsupportedOperationException("not implmented yet");
	}

	@Override
	public boolean add(List<Dict> dicts) {
		int[] updateCounts = jdbcTemplate.batchUpdate(SQLStatements.getString("sql.dict.insert_SC2"),

		new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				logger.trace("writing dict term " + dicts.get(i).toString() + " " + i);
				ps.setInt(1, dicts.get(i).getId());
				ps.setString(2, dicts.get(i).getTerm());
				ps.setTimestamp(3, dicts.get(i).getAddedTimeStamp());
				ps.setInt(4, dicts.get(i).getDocFQ());
			}

			public int getBatchSize() {
				return dicts.size();
			}
		});
		logger.debug(SC2DictDAODocsDB.class.getName() + " added " + updateCounts.length + " dict entries to dict table");
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
