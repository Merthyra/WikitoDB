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
	PreparedStatement prepStmt = null;
			try {
				prepStmt = this.dbConnect.getConnection().prepareStatement("INSERT INTO terms (tid, did, pos) VALUES (?,?,?)");
			} catch (SQLException e) {
				logger.error("Cannot prepare statement for terms table");
			}
				for (Term term : terms) {
					try{
						prepStmt.setLong(1, term.getDict().getId());
						prepStmt.setLong(2, term.getDocid());
						prepStmt.setInt(3, term.getPosition());
						prepStmt.executeUpdate();
					}
					catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						logger.error("Error writing Term with docid " + term.getDocid() + " & termid: "+ term.getDict().getId() +"/"+ term.getDict().getTerm() + " to database");

					}
				}
			try {
				if (prepStmt== null) {
					prepStmt.closeOnCompletion();
				}
			}catch (SQLException ex) {
				logger.trace("Term Statement Closed");
			}
				logger.info("All Terms persisted -> took " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()-time) + " seconds");	
				return true;
	}
	
	
	
}
