package unittests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ac.tuwien.docspars.entity.PersistVariant;
import at.ac.tuwien.docspars.io.services.impl.DBPersistanceServiceFactory;
import at.ac.tuwien.docspars.util.DocumentHandler;
import at.ac.tuwien.docspars.util.ProcessPropertiesHandler;
import at.ac.tuwien.docspars.util.WikiPageCallBackHandler;
import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import edu.jhu.nlp.wikipedia.WikiXMLParseException;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

public class ParsingTest {


	public static WikiXMLParser wxsp;
	public static ParsingTest.WikipageTestCallBackHandler wph;
	public List<WikiPage> pages;
	public static BasicDataSource ds;
	public static Properties props;
	public DocumentHandler handler;

	
	@BeforeClass
	public static void initClass() throws MalformedURLException {
		wxsp = WikiXMLParserFactory.getSAXParser("src/test/java/unittests/wiki-test.xml");	
		props = new Properties();
		FileInputStream fis = null;
		try { 
			File file = new File("src/test/java/unittests/test.jdbc.properties");
			fis = new FileInputStream(file);
			props.load(fis);
			fis.close();
		}
		catch (IOException io) {
			fail();
		}
		ds = new BasicDataSource();
		ds.setDriverClassName(props.getProperty("jdbc.driverClassName"));
		ds.setUrl(props.getProperty("jdbc.url"));
		ds.setUsername(props.getProperty("jdbc.user"));
		ds.setPassword(props.getProperty("jdbc.pw"));    
	}
	
	@Before
	public void init() throws Exception {
		wxsp.setPageCallback(wph = new ParsingTest.WikipageTestCallBackHandler());	
		wxsp.parse();
		this.pages = wph.getPages();
//		for (WikiPage page : wph.getPages()) {
//			
//			System.out.println(page.getID() + " " + page.getTitle());
//			
//		}
	}
	
	
	@Test
	public void testTroveIntSet() throws WikiXMLParseException {
//		int[] pageids = new int[pages.size()];
		TIntSet tints = new TIntHashSet(10); 
		for (WikiPage page : wph.getPages()) {
		
		System.out.println(page.getID() + " " + page.getTitle());
		Integer id = Integer.parseInt(page.getID());
		if (tints.contains(id)) {
			System.out.println("THIS FUCK CONTAINS " + id);
			fail();
		}
		tints.add(id);
		assertTrue (tints.contains(id));
		}
	}
	
	
	@Test
	public void testParserDocumentHandler() throws Exception {
		wxsp.setPageCallback(new WikiPageCallBackHandler(handler = new DocumentHandler(new DBPersistanceServiceFactory(ds).createInstance(PersistVariant.V1), new ProcessPropertiesHandler())));
		wxsp.parse();
		
		int[] ids = handler.getPersistedDocs().toArray();
		
		for (int i = 0; i < ids.length; i++) {
			System.out.println("docNr." + ids[i]);
		}
		
		assertTrue(handler.getPersistedDocs().contains(19736672));
	}
	

	public class WikipageTestCallBackHandler implements PageCallbackHandler {

		private List<WikiPage> pages;
		
		public WikipageTestCallBackHandler() {
			pages = new ArrayList<WikiPage>();
		}
		@Override
		public void process(WikiPage page) {
			pages.add(page);
			
		} 
		
		public List<WikiPage> getPages() {
			return this.pages;
		}	
	}	
}
