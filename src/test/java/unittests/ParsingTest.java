// FIXME
// package unittests;
//
// import static org.junit.Assert.*;
//
// import at.ac.tuwien.docspars.entity.Mode;
// import at.ac.tuwien.docspars.io.services.impl.DBPersistanceServiceFactory;
// import at.ac.tuwien.docspars.util.DocumentHandler;
// import at.ac.tuwien.docspars.util.EnvironmentService;
// import at.ac.tuwien.docspars.util.ProcessPropertiesHandler;
// import at.ac.tuwien.docspars.util.WikiPageCallBackHandler;
// import edu.jhu.nlp.wikipedia.PageCallbackHandler;
// import edu.jhu.nlp.wikipedia.WikiPage;
// import edu.jhu.nlp.wikipedia.WikiXMLParseException;
// import edu.jhu.nlp.wikipedia.WikiXMLParser;
// import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;
// import gnu.trove.set.TIntSet;
// import gnu.trove.set.hash.TIntHashSet;
// import org.apache.commons.dbcp.BasicDataSource;
// import org.junit.Before;
// import org.junit.BeforeClass;
// import org.junit.Test;
//
// import java.io.File;
// import java.io.FileInputStream;
// import java.io.IOException;
// import java.net.MalformedURLException;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Properties;
//
// public class ParsingTest {
//
//
// public static WikiXMLParser wxsp;
// public static ParsingTest.WikipageTestCallBackHandler wph;
// public List<WikiPage> pages;
// public static BasicDataSource ds;
// public static Properties props;
// public DocumentHandler handler;
//
//
// @BeforeClass
// public static void initClass() throws MalformedURLException {
// wxsp = WikiXMLParserFactory.getSAXParser("src/test/java/unittests/wiki-test.xml");
// props = new Properties();
// FileInputStream fis = null;
// try {
// final File file = new File("src/test/java/unittests/test.jdbc.properties");
// fis = new FileInputStream(file);
// props.load(fis);
// fis.close();
// } catch (final IOException io) {
// fail();
// }
// ds = new BasicDataSource();
// ds.setDriverClassName(props.getProperty("jdbc.driverClassName"));
// ds.setUrl(props.getProperty("jdbc.url"));
// ds.setUsername(props.getProperty("jdbc.user"));
// ds.setPassword(props.getProperty("jdbc.pw"));
// }
//
// @Before
// public void init() throws Exception {
// wxsp.setPageCallback(wph = new ParsingTest.WikipageTestCallBackHandler());
// wxsp.parse();
// this.pages = wph.getPages();
// // for (WikiPage page : wph.getPages()) {
// //
// // System.out.println(page.getID() + " " + page.getTitle());
// //
// // }
// }
//
//
// @Test
// public void testTroveIntSet() throws WikiXMLParseException {
// // int[] pageids = new int[pages.size()];
// final TIntSet tints = new TIntHashSet(10);
// for (final WikiPage page : wph.getPages()) {
//
// System.out.println(page.getID() + " " + page.getTitle());
// final Integer id = Integer.parseInt(page.getID());
// if (tints.contains(id)) {
// System.out.println("THIS FUCK CONTAINS " + id);
// fail();
// }
// tints.add(id);
// assertTrue(tints.contains(id));
// }
// }
//
//
// @Test
// public void testParserDocumentHandler() throws Exception {
// final EnvironmentService env = new EnvironmentService(new ProcessPropertiesHandler(), new DBPersistanceServiceFactory(ds));
// env.initialize(Mode.V1);
// wxsp.setPageCallback(new WikiPageCallBackHandler(this.handler = new DocumentHandler(env)));
// wxsp.parse();
//
// final int[] ids = env.getPersistedDocs().toArray();
//
// for (final int id : ids) {
// System.out.println("docNr." + id);
// }
//
// assertTrue(env.getPersistedDocs().contains(19736672));
// }
//
//
// public class WikipageTestCallBackHandler implements PageCallbackHandler {
//
// private final List<WikiPage> pages;
//
// public WikipageTestCallBackHandler() {
// this.pages = new ArrayList<WikiPage>();
// }
//
// @Override
// public void process(final WikiPage page) {
// this.pages.add(page);
//
// }
//
// public List<WikiPage> getPages() {
// return this.pages;
// }
// }
// }
