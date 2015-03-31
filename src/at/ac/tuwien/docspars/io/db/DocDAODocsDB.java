package at.ac.tuwien.docspars.io.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.ac.tuwien.docspars.entity.Document;
import at.ac.tuwien.docspars.io.daos.DocDAO;
import at.ac.tuwien.docspars.util.DocumentStore;

public class DocDAODocsDB implements DocDAO {
	
	private static final Logger logger = LogManager.getLogger(DocDAODocsDB.class.getName());
	DBConnectionHandler dbConnect;
	
	public DocDAODocsDB(DBConnectionHandler dbconn) {
		this.dbConnect = dbconn;
	}
	
	@Override
	public boolean writeDocs(List<Document> docs) {
		// TODO Auto-generated method stub
		
		long time = System.currentTimeMillis();
		String docinfo ="";
		try {
			PreparedStatement prepStmt = this.dbConnect.getConnection().prepareStatement("INSERT INTO DOCS (docid, added, removed, name, len) VALUES (?,?,?,?,?)");
			for (Document doc : docs) {
				docinfo = doc.getId() + " / " +  doc.getTitle();
				prepStmt.setLong(1, doc.getId());
				prepStmt.setTimestamp(2, doc.getAdded_timestamp());
				prepStmt.setTimestamp(3, null);
				prepStmt.setString(4, doc.getTitle());
				prepStmt.setInt(5, doc.getLength());
				prepStmt.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Error writing Documents to database " + docinfo);
			return false;
		}
			logger.info("All docs persisted -> took " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-time) + " seconds");	
			return true;
		}

	@Override
	public boolean updateDoc(Document doc) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<Long> readAll() {
		// TODO Auto-generated method stub
		long time = System.currentTimeMillis();
		Statement st;
		ResultSet rs = null;
		HashSet<Long> docids = new HashSet<Long>();
		try {
			st = dbConnect.getConnection().createStatement();
			rs = st.executeQuery("SELECT * FROM docs WHERE removed IS NULL");
			
			while(rs.next()) {
				docids.add(rs.getLong(2));
			}
			logger.info("Read all " + docids.size() + " documents from docs table and it took " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-time) + " seconds");		
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Cannot read documents from docs table " + e.getMessage() + " cause: " + e.getCause());
			return null;
		} 
		return docids;
	}

}
