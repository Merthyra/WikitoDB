package ac.at.tuwien.wikipars.db;

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
import ac.at.tuwien.wikipars.util.WikiPars;

public class DictDAOWikiDB implements DictDAO{

	private DBConnectionHandler dbConnect;
	private long tID = 0;
	private static final Logger logger = LogManager.getLogger(DictDAOWikiDB.class.getName());
	
	public DictDAOWikiDB() {
		
	}

	public DictDAOWikiDB(DBConnectionHandler dbConnect) {
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
	public boolean add(List<Dict> dicts) {
		// TODO Auto-generated method stub

		return true;
	}


	@Override
	public Set<String> readAll() {
		
		HashSet<String> dictTable = new HashSet<String>();
		
		Statement st = this.dbConnect.getStatement();
		ResultSet rs;
		try {
			rs = st.executeQuery("SELECT * FROM dict");
			
			while (rs.next()) {
				dictTable.add(rs.getString(2));
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
