package unittests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.*;
import at.ac.tuwien.docspars.entity.Dict;
import at.ac.tuwien.docspars.entity.Document;
import at.ac.tuwien.docspars.entity.SimpleDict;
import at.ac.tuwien.docspars.entity.Term;
import at.ac.tuwien.docspars.io.db.DocDAOdb;
import at.ac.tuwien.docspars.io.db.DictDAOdb;
import at.ac.tuwien.docspars.io.db.Term1DAODdb;
import at.ac.tuwien.docspars.io.db.SQLStatements;
import at.ac.tuwien.docspars.io.services.PersistanceService;
import at.ac.tuwien.docspars.io.services.impl.DBPersistanceService;
import at.ac.tuwien.docspars.io.services.impl.DBPersistanceServiceV1;

public class DAOTestsSC1 {

	private static PersistanceService persSer;
	public static Logger logger = LogManager.getLogger(Package.class);
	public static BasicDataSource ds = new BasicDataSource();
	public static Properties props;

	@BeforeClass
	public static void initClass(){

		props = new Properties();
		FileInputStream fis = null;
		try { 
			File file = new File("src/test/java/unittests/test.jdbc.properties");
			fis = new FileInputStream(file);
			props.load(fis);
			fis.close();
		}
		catch (IOException io) {
			logger.error("Could not read jdbc properties test file");
			fail();
		}
        ds.setDriverClassName(props.getProperty("jdbc.driverClassName"));
        ds.setUrl(props.getProperty("jdbc.url"));
        ds.setUsername(props.getProperty("jdbc.user"));
        ds.setPassword(props.getProperty("jdbc.pw"));    
        persSer = new DBPersistanceServiceV1(ds);	        
	}
	
	@Before
	public void reCreateTables() throws SQLException {
		
		Connection con = ds.getConnection();
		Statement st = con.createStatement();
		try {
		st.executeUpdate(SQLStatements.getString("sql.terms.drop"));
		}
		catch (SQLException ex) {}
		try {
		st.executeUpdate(SQLStatements.getString("sql.dict.drop"));
		}
		catch (SQLException ex) {}
		try {
		st.executeUpdate(SQLStatements.getString("sql.docs.drop"));
		}
		catch (SQLException ex) {}
		st.executeUpdate(SQLStatements.getString("sql.dict.create_SC1"));
		st.executeUpdate(SQLStatements.getString("sql.docs.create"));
		st.executeUpdate(SQLStatements.getString("sql.terms.create_SC1"));
		st.close();
		con.close();
		
	}
	
	@Test
	public void testAddDocument() {
//		List<Document> docList = new ArrayList<Document>();
//		docList.add(new Document(10, 20, "one", new Timestamp(1000), 30));
//		docList.add(new Document(20, 30, "two", new Timestamp(2000), 35));
//		docList.add(new Document(30, 50, "three", new Timestamp(3000), 45));
//		docList.add(new Document(40, 50, "four", new Timestamp(3000), 45));
//		docList.add(new Document(30, 50, "five", new Timestamp(5000), 85));
//		List<Dict> dictList = new ArrayList<Dict>();
//		dictList.add(new SimpleDict(10, "AA"));
//		dictList.add(new SimpleDict(20, "AB"));
//		dictList.add(new SimpleDict(30, "AC"));
//		dictList.add(new SimpleDict(40, "AD"));
//		dictList.add(new SimpleDict(50, "AE"));
//		dictList.add(new SimpleDict(60, "AF"));
//		dictList.add(new SimpleDict(70, "AG"));
//		dictList.add(new SimpleDict(80, "AH"));
//		dictList.add(new SimpleDict(90, "AI"));
//		List<Term> termList = new ArrayList<Term>();
//		//(Dict dic, int pageID, int revID, int pos)
//		termList.add(new Term(dictList.get(0), docList.get(0), 5));
//		termList.add(new Term(dictList.get(1), docList.get(1), 15));
//		termList.add(new Term(dictList.get(2), docList.get(2), 32));
//		termList.add(new Term(dictList.get(3), docList.get(3), 5876));
//		termList.add(new Term(dictList.get(5), docList.get(3), 56));
//		termList.add(new Term(dictList.get(4), docList.get(4), 7448));
//		termList.add(new Term(dictList.get(6), docList.get(4), 478));
//		termList.add(new Term(dictList.get(7), docList.get(4), 788));
//		termList.add(new Term(dictList.get(8), docList.get(4), 789));
//		persSer.addBatch(docList, dictList, termList);
		
		try {
			Connection con = ds.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM TERMS");
			assertTrue(rs.next());
			assertThat(rs.getInt(1), is(9));
			rs.close();
			rs = st.executeQuery("SELECT * FROM docs WHERE pageid >=30");
			assertTrue(rs.next());
			assertThat(rs.getInt(5), is(45));
			rs.close();
			rs = st.executeQuery("SELECT * FROM terms WHERE pageid = 40");
			assertTrue(rs.next());
			assertThat(rs.getInt(4), is(5876));
			rs.close();
			con.close();
			
		} catch (SQLException e) {
			fail();
			logger.error("DB Connection Error");
		}	
	}
}
