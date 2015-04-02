package at.ac.tuwien.docspars.io.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import at.ac.tuwien.docspars.entity.Dict;
import at.ac.tuwien.docspars.entity.SimpleDict;
import at.ac.tuwien.docspars.entity.TimestampedDict;
import at.ac.tuwien.docspars.io.IncompleteBatchProcessException;
import at.ac.tuwien.docspars.io.daos.DictDAO;
import at.ac.tuwien.docspars.util.DocumentPars;

public class SC1DictDAODocsDB implements DictDAO{
	
	@Deprecated
	private DBConnectionHandler dbConnect;
	private static final Logger logger = LogManager.getLogger(SC1DictDAODocsDB.class.getName());
	private JdbcTemplate jdbcTemplate;

	
	@SuppressWarnings("unused")
	@Deprecated
	private SC1DictDAODocsDB() {

	}
	
	public SC1DictDAODocsDB(DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}
	
//	private boolean addManualApproach(List<Dict> dicts) {
//		// TODO Auto-generated method stub
//		long time = System.currentTimeMillis();
//		PreparedStatement prepStmt = null;
//		try {
//			jdbcTemplate.getDataSource().getConnection().setAutoCommit(false);
//			prepStmt = jdbcTemplate.getDataSource().getConnection().prepareStatement(this.sql_insert, PreparedStatement.RETURN_GENERATED_KEYS);		
//			for (Dict dic : dicts) {
//				prepStmt.setString(1, dic.getTerm());
//				prepStmt.addBatch();
//			}
//			int[] updates = prepStmt.executeBatch();
//			
//			ResultSet rs = prepStmt.getGeneratedKeys();
//			// check if all inserts where successful
//			if (updates.length != dicts.size()) {
//				// commit changes if true
//				jdbcTemplate.getDataSource().getConnection().rollback();
//				throw new IncompleteBatchProcessException("error writing dict batch with expected size <" + dicts.size() + "> but has size of <" + updates.length + ">");			
//				
//			}
//			else {			
//				jdbcTemplate.getDataSource().getConnection().rollback();
//				throw new IncompleteBatchProcessException("error writing dict batch with expected size <" + dicts.size() + "> but has size of <" + updates.length + ">");			
//			}
//			
//			while (rs.next()) {
//				
//			}
//			
//			if (rs != null && rs.next()) {
//				   dict.setId(rs.getLong(1));
//				   //logger.debug("id: " + dic.getId() + " was assigned to dict entry : " + dic.getTerm() );			   
//				} else {
//					logger.error("no id for " + dic.getTerm() + "assigned");
//				}
//			logger.trace("wrote " + dicts.size() + " documents to dict table");	
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			logger.error("Cannot create Statement to write in DICT");
//			return false;
//		}
//		finally {
//			try {
//				if (prepStmt!=null) {
//					prepStmt.close();
//					logger.trace("Statement closed");
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		logger.info("All DictTerms persisted -> took " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-time) + " seconds");
//		return true;
//	}

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

}
