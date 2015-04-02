package at.ac.tuwien.docspars.io.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import at.ac.tuwien.docspars.entity.Dict;
import at.ac.tuwien.docspars.entity.TimestampedDict;
import at.ac.tuwien.docspars.io.daos.DictDAO;

public class SC2DictDAODocsDB implements DictDAO {

	private JdbcTemplate jdbcTemplate;
	
	public SC2DictDAODocsDB(DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}
	
	@Override
	public Map<String, Dict> getAll() {
		ResultSetExtractor<Map<String, Dict>> resEx = new ResultSetExtractor<Map<String, Dict>>() {	
			@Override
			public Map<String, Dict> extractData(ResultSet res) throws SQLException, DataAccessException {
				HashMap<String, Dict> dict = new HashMap<String,Dict>();
				while (res.next()) {
					dict.put(res.getString("term"), new TimestampedDict(res.getLong("tid"), res.getString("term"), res.getTimestamp("added"), res.getTimestamp("removed"), res.getInt("df"), res.getInt("tf")));
				}
			return dict;		
			}

		};
		return (Map<String,Dict>) this.jdbcTemplate.query(SQLStatements.getString("sql.dict.read_SC2"),resEx);
	}

}
