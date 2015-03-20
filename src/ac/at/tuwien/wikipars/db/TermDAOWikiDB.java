package ac.at.tuwien.wikipars.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ac.at.tuwien.wikipars.entity.Document;
import ac.at.tuwien.wikipars.entity.Term;
import ac.at.tuwien.wikipars.io.TermDAO;

public class TermDAOWikiDB implements TermDAO{

	private static final Logger logger = LogManager.getLogger(TermDAOWikiDB.class.getName());
	DBConnectionHandler dbConnect;
	
	public TermDAOWikiDB(DBConnectionHandler dbconn) {
		this.dbConnect = dbconn;
	}

	@Override
	public boolean writeTerms(List<Term> terms) {

	long time = System.currentTimeMillis();			
			try {
				PreparedStatement prepStmt = this.dbConnect.getConnection().prepareStatement("INSERT INTO terms (tid, did, pos) VALUES (?,?,?);");
				for (Term term : terms) {
					prepStmt.setLong(1, term.getDict().getId());
					prepStmt.setLong(2, term.getDocid());
					prepStmt.setInt(3, term.getPosition());
					prepStmt.executeUpdate();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Error writing Terms to database");
				return false;
			}
				logger.info("All Terms persisted -> took " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-time) + " seconds");	
				return true;
	}
	
	
	
}
