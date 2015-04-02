package at.ac.tuwien.docspars.io.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import at.ac.tuwien.docspars.entity.Document;
import at.ac.tuwien.docspars.entity.Term;
import at.ac.tuwien.docspars.io.daos.TermDAO;

public class TermDAODocsDB implements TermDAO{

	private static final Logger logger = LogManager.getLogger(TermDAODocsDB.class.getName());
	DBConnectionHandler dbConnect;
	private PreparedStatement prepStmt;
	private JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings("unused")
	@Deprecated
	private TermDAODocsDB() {
		super();
	}
	
	public TermDAODocsDB(DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}
	
}
