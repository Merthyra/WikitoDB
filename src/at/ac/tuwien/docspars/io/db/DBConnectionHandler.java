package at.ac.tuwien.docspars.io.db;

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

import at.ac.tuwien.docspars.util.DocumentPars;

@Deprecated
public class DBConnectionHandler {
	
	private static final Logger logger = LogManager.getLogger(DBConnectionHandler.class.getName());
	
	private Connection con;
	private ResultSet rs;
	private int bufferSize;
	
	public DBConnectionHandler() {

	}	
	
	public void connect() throws IOException, SQLException, ClassNotFoundException {
		
		if (this.con == null) {
			Properties prop = new Properties();
			InputStream input = null;
			
			logger.debug("accessing properties file");
			input = new FileInputStream("res/dbprops.properties");
			prop.load(input);
			logger.debug("propterties file loaded");
			String dbdriver = prop.getProperty("db_driver");
			String dbloc = prop.getProperty("db_location");
			String dbuser= prop.getProperty("db_user");
			String dbpw = prop.getProperty("db_pw");
			String dbname = prop.getProperty("db_name");
			try {
				this.bufferSize = Integer.parseInt(prop.getProperty("db_batchsize"));
			} catch (NumberFormatException nfex) {
				logger.error("Wrong Number in DataBase Properties File for BatchSize");
				this.bufferSize = 1;
			}
			
			logger.debug("propterties loaded");
				
			Class.forName(dbdriver);
	
			logger.debug("establishing connection to monetdb db");
			this.con = DriverManager.getConnection(dbloc +"/"+dbname , dbuser, dbpw);
			logger.trace("connection to monetdb and jdbc driver loaded");
		}

	}
	public int getDBBatchSize() {
		return this.bufferSize;
	}
	
	public Connection getConnection() {
		return this.con;
	}
	
	public void closeConnection() {
		try {
			con.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void setAutoCommit(boolean flag) {
		try {
			this.con.setAutoCommit(flag);
		} catch (SQLException e) {
			logger.error("AutoCommit Cannot be Set to " + flag);
		}
	}
	
	public void commit() {
		try {
			con.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("AutoCommit Cannot be Set to ");
		}
	}

}
