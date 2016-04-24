package unittests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.factories.DictCreationable;
import at.ac.tuwien.docspars.entity.factories.DocumentCreationable;
import at.ac.tuwien.docspars.entity.factories.TermCreationable;
import at.ac.tuwien.docspars.entity.impl.Dict;
import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.entity.impl.Term;
import at.ac.tuwien.docspars.io.daos.db.AbstractTermDAOdb;
import at.ac.tuwien.docspars.io.daos.db.DictDAOdb;
import at.ac.tuwien.docspars.io.daos.db.DictHistDAOdb;
import at.ac.tuwien.docspars.io.daos.db.DocDAOdb;
import at.ac.tuwien.docspars.io.daos.db.SQLStatements;
import at.ac.tuwien.docspars.io.daos.db.Term1DAODdb;
import at.ac.tuwien.docspars.io.daos.db.Term2DAODdb;
import at.ac.tuwien.docspars.io.services.PersistanceService;
import at.ac.tuwien.docspars.io.services.impl.DBPersistanceServiceV1;
import at.ac.tuwien.docspars.util.DocumentHandler;
import at.ac.tuwien.docspars.util.ProcessPropertiesHandler;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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

public class DAOTests {

  private static PersistanceService persSer;
  public static Logger logger = LogManager.getLogger(Package.class);
  public static BasicDataSource ds = new BasicDataSource();
  public static Properties props;
  public static ProcessPropertiesHandler processProperties;
  private static List<Document> docList = new ArrayList<Document>();
  private static List<Dictionable> dictList = new ArrayList<Dictionable>();
  private static List<Term> termList = new ArrayList<Term>();
  private DocumentCreationable docFactory;
  private DictCreationable dictFactory;
  private TermCreationable termFactory;

  private DocumentHandler docHandler;

  @BeforeClass
  public static void initClass() throws SQLException {
    props = new Properties();
    FileInputStream fis = null;
    try {
      final File file = new File("src/test/java/unittests/test.jdbc.properties");
      fis = new FileInputStream(file);
      props.load(fis);
      fis.close();
    } catch (final IOException io) {
      logger.error("Could not read jdbc properties test file");
      fail();
    }
    ds.setDriverClassName(props.getProperty("jdbc.driverClassName"));
    ds.setUrl(props.getProperty("jdbc.url"));
    ds.setUsername(props.getProperty("jdbc.user"));
    ds.setPassword(props.getProperty("jdbc.pw"));
    persSer = new DBPersistanceServiceV1(ds);
    // (int batch_size, int start_offset, int max_pages, String date_format, String language, String
    // sc, int maxLength, int dictsCached)
    processProperties = new ProcessPropertiesHandler(2, 0, 2, "yyyy-MM-dd'T'hh:mm:ss'Z'", "en", "V1", 100, 1000, 0,null);

    final Connection con = ds.getConnection();
    final Statement st = con.createStatement();
    try {
      st.executeUpdate(SQLStatements.getString("sql.terms1.drop"));
    } catch (final SQLException ex) {
    }
    try {
      st.executeUpdate(SQLStatements.getString("sql.terms2.drop"));
    } catch (final SQLException ex) {
    }
    // try {
    // st.executeUpdate(SQLStatements.getString("sql.terms3.drop"));
    // }
    // catch (SQLException ex) {}
    try {
      st.executeUpdate(SQLStatements.getString("sql.terms4.drop"));
    } catch (final SQLException ex) {
    }
    try {
      st.executeUpdate(SQLStatements.getString("sql.terms5.drop"));
    } catch (final SQLException ex) {
    }
    try {
      st.executeUpdate(SQLStatements.getString("sql.dict.drop"));
    } catch (final SQLException ex) {
    }
    try {
      st.executeUpdate(SQLStatements.getString("sql.dict_hist.drop"));
    } catch (final SQLException ex) {
    }
    try {
      st.executeUpdate(SQLStatements.getString("sql.docs.drop"));
    } catch (final SQLException ex) {
    }
    try {
      st.executeUpdate(SQLStatements.getString("sql.docs5.drop"));
    } catch (final SQLException ex) {
    }
    st.executeUpdate(SQLStatements.getString("sql.dict.create"));
    st.executeUpdate(SQLStatements.getString("sql.dict_hist.create"));
    st.executeUpdate(SQLStatements.getString("sql.docs.create"));
    st.executeUpdate(SQLStatements.getString("sql.docs5.create"));
    st.executeUpdate(SQLStatements.getString("sql.terms1.create"));
    st.executeUpdate(SQLStatements.getString("sql.terms2.create"));
    st.executeUpdate(SQLStatements.getString("sql.terms4.create"));
    st.executeUpdate(SQLStatements.getString("sql.terms5.create"));
    st.close();
    con.close();
  }

  @Before
  public void prepareTables() throws SQLException {
    final Connection con = ds.getConnection();
    final Statement st = con.createStatement();
    st.executeUpdate("delete from dict");
    st.executeUpdate("delete from dict_hist");
    st.executeUpdate("delete from docs");
    st.executeUpdate("delete from docs5");
    st.executeUpdate("delete from terms");
    st.executeUpdate("delete from terms2");
    st.executeUpdate("delete from terms4");
    st.executeUpdate("delete from terms5");
  }

  @Before
  public void setTestData() {
    docList.clear();
    dictList.clear();
    termList.clear();
    docList.add(new Document(10, 1, "one", new Timestamp(1000), 30, false));
    docList.add(new Document(20, 1, "two", new Timestamp(2000), 35, false));
    docList.add(new Document(30, 1, "three", new Timestamp(3000), 45, false));
    docList.add(new Document(40, 5876, "four", new Timestamp(3000), 45, false));
    docList.add(new Document(30, 1, "five", new Timestamp(5000), 85, false));

    dictList.add(new Dict(10, "AA"));
    dictList.add(new Dict(20, "AB"));
    dictList.add(new Dict(30, "AC"));
    dictList.add(new Dict(40, "AD"));
    dictList.add(new Dict(50, "AE"));
    dictList.add(new Dict(60, "AF"));
    dictList.add(new Dict(70, "AG"));
    dictList.add(new Dict(80, "AH"));
    dictList.add(new Dict(90, "AI"));

    // (Dict dic, int pageID, int pos)
    termList.add(new Term(docList.get(0), dictList.get(0), 5));
    termList.add(new Term(docList.get(1), dictList.get(1), 15));
    termList.add(new Term(docList.get(2), dictList.get(2), 32));
    termList.add(new Term(docList.get(3), dictList.get(3), 5876));
    termList.add(new Term(docList.get(3), dictList.get(5), 56));
    termList.add(new Term(docList.get(4), dictList.get(4), 7448));
    termList.add(new Term(docList.get(4), dictList.get(6), 478));
    termList.add(new Term(docList.get(4), dictList.get(7), 788));
    termList.add(new Term(docList.get(4), dictList.get(8), 789));
    termList.add(new Term(docList.get(0), dictList.get(0), 7));
    termList.add(new Term(docList.get(0), dictList.get(0), 8));
    termList.add(new Term(docList.get(0), dictList.get(5), 9));

  }


  @Test
  public void testDAOV1() {
    final DictDAOdb dictdao = new DictDAOdb(ds);
    final DocDAOdb docDAOdb = new DocDAOdb(ds);
    final AbstractTermDAOdb term1dao = new Term1DAODdb(ds);
    dictdao.add(dictList);
    docDAOdb.add(docList);
    term1dao.add(termList);
    try {
      final Connection con = ds.getConnection();
      final Statement st = con.createStatement();
      ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM TERMS");
      assertTrue(rs.next());
      assertThat(rs.getInt(1), is(12));
      rs.close();
      rs = st.executeQuery("SELECT * FROM docs WHERE did >=30");
      assertTrue(rs.next());
      assertThat(rs.getInt(6), is(45));
      rs.close();
      rs = st.executeQuery("SELECT * FROM terms WHERE did = 40");
      assertTrue(rs.next());
      assertThat(rs.getInt(3), is(5876));
      rs.close();
      rs = st.executeQuery("SELECT * FROM dict");
      for (int i = 0; i < 9; i++) {
        assertTrue(rs.next());
      }
      assertThat(rs.getInt(1), is(90));
      assertThat(rs.getString(2), is("AI"));
      rs.close();
      con.close();

    } catch (final SQLException e) {
      e.printStackTrace();
      fail();
      logger.error("DB Connection Error");
    }
  }

  @Test
  public void testDAOV2() {

    final AbstractTermDAOdb termdao = new Term2DAODdb(ds);
    termdao.add(termList);

    try {
      final Connection con = ds.getConnection();
      final Statement st = con.createStatement();
      ResultSet rs;

      rs = st.executeQuery("SELECT * FROM terms2 WHERE did = 10");
      assertTrue(rs.next());
      assertThat(rs.getInt(4), is(2));

      rs.close();
      con.close();

    } catch (final SQLException e) {
      e.printStackTrace();
      fail();
      logger.error("DB Connection Error");
    }
  }


  @Test
  public void testDAOV3() {
    final DictHistDAOdb dictdao = new DictHistDAOdb(ds);
    final List<Term> list = termList.subList(0, 5);
    try {
      dictdao.setTimestamp(new Timestamp(50000000000L));
      dictdao.add(list);
      final Connection con = ds.getConnection();
      final Statement st = con.createStatement();
      ResultSet rs;
      rs = st.executeQuery("SELECT count(*) FROM dict_hist");
      assertTrue(rs.next());
      assertThat(rs.getInt(1), is(5));
      dictdao.setTimestamp(new Timestamp(100000000000L));
      dictdao.add(list.subList(0, 2));
      rs = st.executeQuery("SELECT * FROM dict_hist WHERE tid = " + ((Dictionable) termList.get(0)).getTId() + " order by df asc");

      assertTrue(rs.next());
      assertThat(rs.getInt("df"), is(1));
      assertThat(rs.getTimestamp(2), is(new Timestamp(50000000000L)));
      assertThat(rs.getTimestamp(3), is(new Timestamp(100000000000L)));
      assertTrue(rs.next());
      assertThat(rs.getInt("df"), is(2));
      assertThat(rs.getTimestamp(2), is(new Timestamp(100000000000L)));
      assertThat(rs.getTimestamp(3), is(nullValue()));
      rs.close();
      con.close();

    } catch (final SQLException e) {
      e.printStackTrace();
      fail();
      logger.error("DB Connection Error");
    }
  }

}
