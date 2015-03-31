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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.ac.tuwien.docspars.entity.Dict;
import at.ac.tuwien.docspars.io.daos.DictDAO;
import at.ac.tuwien.docspars.util.DocumentPars;

public class DictDAODocsDB implements DictDAO{

	private DBConnectionHandler dbConnect;
	private long tID = 0;
	private static final Logger logger = LogManager.getLogger(DictDAODocsDB.class.getName());
	private PreparedStatement instertStmt;
	private PreparedStatement updateStmt;
	
	public DictDAODocsDB() {
		
	}

	public DictDAODocsDB(DBConnectionHandler dbConnect) throws SQLException {
		this.dbConnect = dbConnect;
	}
	
	public void setConnectionHandler(DBConnectionHandler conn) {
		this.dbConnect=conn;
	}	
	
	@Override
	public boolean update(Dict dict) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean writeDict(List<Dict> dicts) {
		// TODO Auto-generated method stub
		long time = System.currentTimeMillis();
		PreparedStatement prepStmt =null;
		try {
			 prepStmt = dbConnect.getConnection().prepareStatement("INSERT INTO DICT (term) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS);		
			for (Dict dic : dicts) {
				prepStmt.setString(1, dic.getTerm());
				prepStmt.executeUpdate();
				ResultSet rs = prepStmt.getGeneratedKeys();
				if (rs != null && rs.next()) {
				   dic.setId(rs.getLong(1));
				   //logger.debug("id: " + dic.getId() + " was assigned to dict entry : " + dic.getTerm() );			   
				} else {
					logger.error("no id for " + dic.getTerm() + "assigned");
				}
				
			}
			logger.trace("wrote " + dicts.size() + " documents to dict table");	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Cannot create Statement to write in DICT");
			return false;
		}
		finally {
			try {
				if (prepStmt!=null) {
					prepStmt.close();
					logger.trace("Statement closed");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		logger.info("All DictTerms persisted -> took " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-time) + " seconds");
		return true;
	}

	@Override
	public HashMap<String, Dict> readAll() {
		
		long time = System.currentTimeMillis();
		HashMap<String, Dict> dictTable = new HashMap<String, Dict>();
		
		Statement st = null;
		ResultSet rs;
		try {
			st = this.dbConnect.getConnection().createStatement();
			rs = st.executeQuery("SELECT * FROM dict");
			
			while (rs.next()) {
				dictTable.put(rs.getString(2), new Dict(rs.getInt(1), rs.getString(2)));
			}
			logger.debug("Entire Dict Loaded and Stored in Java Map");
			logger.info("All DictTerms read from db -> took " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-time) + " seconds");	
			return dictTable;
		
		}
		catch (SQLException sqlex) {
			logger.error("Error Reading Dict Table: " + sqlex.getMessage() + " : cause > " + sqlex.getCause());
			return null;
		}
		
		finally {
			try {
				if (st!=null) {
					st.close();
					logger.trace("Statement closed");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
