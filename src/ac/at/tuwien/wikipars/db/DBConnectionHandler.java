package ac.at.tuwien.wikipars.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;


import org.apache.logging.log4j.Logger;

import ac.at.tuwien.wikipars.util.WikiPars;

public class DBConnectionHandler {
	
	private static final Logger logger = LogManager.getLogger(DBConnectionHandler.class.getName());
	
	private Connection con;
	private Statement st;
	private ResultSet rs;
	
	public DBConnectionHandler() {

	}	
	
	public void connect() throws IOException, SQLException, ClassNotFoundException {
		
		if (this.con == null) {
			Properties prop = new Properties();
			InputStream input = null;
			
			logger.debug("accessing properties file");
			input = new FileInputStream("dbprops.properties");
			prop.load(input);
			logger.debug("propterties file loaded");
			String dbdriver = prop.getProperty("db_driver");
			String dbloc = prop.getProperty("db_location");
			String dbuser= prop.getProperty("db_user");
			String dbpw = prop.getProperty("db_pw");
			String dbname = prop.getProperty("db_name");
			logger.debug("propterties loaded");
				
			Class.forName(dbdriver);
	
			logger.debug("establishing connection to monetdb db");
			this.con = DriverManager.getConnection(dbloc +"/"+dbname , dbuser, dbpw);
			this.st = con.createStatement();
			logger.trace("connection to monetdb and jdbc driver loaded");
		}

	}
	
	
	public Statement getStatement() { 
		return this.st;
	}
	
	public Connection getConnection() {
		return this.con;
	}
	
	public void closeConnection() {
		try {
			st.close();
			con.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
