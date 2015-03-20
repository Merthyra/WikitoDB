package ac.at.tuwien.wikipars.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ac.at.tuwien.wikipars.entity.Dict;
import ac.at.tuwien.wikipars.io.DictDAO;
import ac.at.tuwien.wikipars.util.WikiPars;

public class DictDAOWikiDB implements DictDAO{

	private DBConnectionHandler dbConnect;
	private long tID = 0;
	private static final Logger logger = LogManager.getLogger(DictDAOWikiDB.class.getName());
	private PreparedStatement instertStmt;
	private PreparedStatement updateStmt;
	
	public DictDAOWikiDB() {
		
	}

	public DictDAOWikiDB(DBConnectionHandler dbConnect) throws SQLException {
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
		PreparedStatement prepStmt;
		try {
			 prepStmt = dbConnect.getConnection().prepareStatement("INSERT INTO DICT (term) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS);		
			for (Dict dic : dicts) {
				prepStmt.setString(1, dic.getTerm());
				prepStmt.executeUpdate();				
				ResultSet rs = prepStmt.getGeneratedKeys();
				if (rs != null && rs.next()) {
				   dic.setId(rs.getLong(1));
				}					
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Cannot create Statement to write in DICT");
		}
		return false;
	}

	@Override
	public HashMap<String, Dict> readAll() {
		
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
			
			st.close();
			return dictTable;
		
		}
		catch (SQLException sqlex) {
			logger.error("Error Reading Dict Table: " + sqlex.getMessage() + " : cause > " + sqlex.getCause());
			return null;
		}
		
		finally {
			try {
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
