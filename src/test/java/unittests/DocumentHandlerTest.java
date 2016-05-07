package unittests;


import static org.junit.Assert.*;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.io.services.PersistanceService;
import at.ac.tuwien.docspars.io.services.impl.DBPersistanceServiceFactory;
import at.ac.tuwien.docspars.io.services.impl.DBPersistanceServiceV1;
import at.ac.tuwien.docspars.util.DocumentHandler;
import at.ac.tuwien.docspars.util.EnvironmentService;
import at.ac.tuwien.docspars.util.ProcessPropertiesHandler;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class DocumentHandlerTest {


  private static PersistanceService persSer;
  public static Logger logger = LogManager.getLogger(Package.class);
  private static BasicDataSource ds = new BasicDataSource();
  private static ProcessPropertiesHandler propsHandler;

  private static Properties props;

  @BeforeClass
  public static void initClass() {

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
    // int batch_size, int start_offset, int max_pages, String date_format, String language, String
    // sc, int maxLength, int dictsCached
    propsHandler = new ProcessPropertiesHandler(10, 0, 20, "yyyy-MM-dd'T'hh:mm:ss'Z'", "en", "V1", 100, 1000, 20, null);
  }


  @Test
  public void testDocumentHandler() {
    // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
    final String term1 = "AB";
    final String term2 = "AC";
    final String term3 = "BC";
    final String term4 = "ABD";
    final String term5 = "AC";
    final String term6 = "C";
    final String term7 = "CD";
    final String term8 = "BCD";
    final String term9 = "D";
    final EnvironmentService env = new EnvironmentService(propsHandler, new DBPersistanceServiceFactory(ds));
    final DocumentHandler docHandler = new DocumentHandler(env);
    final List<String> terms = new ArrayList<String>();
    terms.add(term1); // 0
    terms.add(term2); // 1
    terms.add(term4); // 2
    docHandler.addDocument(10, 1, "DOCUMENT A", new Timestamp(4000), terms);
    List<Dictionable> newDictEntries = env.getActiveBatch().getNewVocab();
    // assertTrue(docHandler.getSize() == 1);
    assertEquals(newDictEntries.get(1).getTerm(), term2);

    terms.clear();
    terms.add(term8); // 3
    terms.add(term3); // 4
    terms.add(term1); // 0
    terms.add(term4); // 2
    terms.add(term8); // 3
    terms.add(term8); // 3
    terms.add(term8); // 3
    terms.add(term8); // 3
    terms.add(term8); // 3

    docHandler.addDocument(20, 1, "DOCUMENT B", new Timestamp(2000), terms);
    newDictEntries = env.getActiveBatch().getNewVocab();
    assertSame(env.getActiveBatch().getSize(), 2);
    assertSame(env.getActiveBatch().getUniqueTermsForBatch().size(), 7);
    assertSame(env.getActiveBatch().getTerms().size(), 12);
    assertSame(env.getActiveBatch().getNewVocab().size(), 5);
    assertSame(env.getActiveBatch().getSize(), 0);
    assertEquals(newDictEntries.get(0).getTerm(), term1);
    assertEquals(newDictEntries.get(4).getTerm(), term3);


    terms.clear();
    terms.add(term6); // 5
    terms.add(term8); // 3
    terms.add(term3); // 4
    terms.add(term5); // 6
    terms.add(term7); // 7
    terms.add(term2); // 1
    docHandler.addDocument(30, 1, "DOCUMENT C", new Timestamp(3000), terms);


    terms.clear();
    terms.add(term7); // 7
    terms.add(term4); // 2
    terms.add(term8); // 3
    terms.add(term9); // 8

    docHandler.addDocument(40, 1, "DOCUMENT D", new Timestamp(5000), terms);
    newDictEntries = env.getActiveBatch().getNewVocab();
    assertSame(env.getActiveBatch().getDocs().size(), 4);
    // adding same document again - size of documents must not change
    docHandler.addDocument(40, 1, "DOCUMENT D", new Timestamp(5000), terms);
    assertSame(env.getActiveBatch().getDocs().size(), 4);

  }

  @Test
  public void testDocsPersistance() {
    final EnvironmentService env = new EnvironmentService(propsHandler, new DBPersistanceServiceFactory(ds));
    final DocumentHandler docHandler = new DocumentHandler(env);
    final List<String> terms = new ArrayList<String>();
    docHandler.addDocument(10, 1, "DOCUMENT A", new Timestamp(4000), terms);
    assertTrue(env.getPersistedDocs().contains(10));
    docHandler.addDocument(10, 2, "DOCUMENT A", new Timestamp(4000), terms);
    assertTrue(env.getActiveBatch().getSize() == 1);
  }



  private String getStringWithLengthAndFilledWithCharacter(final int length, final char charToFill) {
    if (length > 0) {
      final char[] array = new char[length];
      Arrays.fill(array, charToFill);
      return new String(array);
    }
    return "";
  }



}
