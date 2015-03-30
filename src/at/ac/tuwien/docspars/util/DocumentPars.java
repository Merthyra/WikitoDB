package at.ac.tuwien.docspars.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.ac.tuwien.docspars.db.DBConnectionHandler;
import at.ac.tuwien.docspars.db.DictDAOWikiDB;
import at.ac.tuwien.docspars.db.DocDAOWikiDB;
import at.ac.tuwien.docspars.db.TermDAOWikiDB;
import at.ac.tuwien.docspars.io.DictDAO;
import at.ac.tuwien.docspars.io.FileProvider;
import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;

public class DocumentPars {

	private static final Logger logger = LogManager.getLogger(DocumentPars.class.getName());
	private static WikiXMLParser wxsp;

	public static void main(String args[]) throws SQLException {

		logger.trace("Trying to establish connection to DB");
		DBConnectionHandler dbConnect = new DBConnectionHandler();
		try {
			dbConnect.connect();
		} catch (SQLException sqlex) {
			logger.fatal("Cannot Access Database: " + sqlex.getMessage() + " : cause > " + sqlex.getCause().getMessage());
			return;
		} catch (IOException ioex) {
			logger.fatal("Error Accesssing Properties File: " + ioex.getMessage() + " : cause '> " + ioex.getCause().getMessage());
			return;
		} catch (ClassNotFoundException clex) {
			logger.fatal("ClassLoader Error: " + clex.getMessage() + " : cause '> " + clex.getCause().getMessage());
			return;
		}
		
		logger.trace("Successfully connected to DB");
		DictDAO dict = new DictDAOWikiDB(dbConnect);

		FileProvider files = new FileProvider();
		ProcessProperties props = new ProcessProperties();

		File file = files.getNextFile();
		if (file == null) {
			logger.fatal("no file provided -> exiting");
			return;
		}
		logger.debug("parsing " + file.getAbsolutePath());
		
		DocumentStore pageStore = new DocumentStore(new DictDAOWikiDB(dbConnect), new DocDAOWikiDB(dbConnect), new TermDAOWikiDB(dbConnect));
		
		try {
			wxsp = WikiXMLParserFactory.getSAXParser(file.getAbsolutePath());
		} catch (MalformedURLException e1) {
			logger.fatal("file path is not valid " + e1.getMessage());
			return;
		}
		try {
			wxsp.setPageCallback(new PageCallbackHandler() {
						
						@Override
						public void process(WikiPage page) {
							
							if (!props.skipPageDueToOffset() && props.allowNextPage()) {
								if (props.getMaxPages() <10)
								logger.debug("processing wiki-page " + page.getID()+ " title: " + page.getTitle() + " timestamp: "+ page.getTimestamp());
								String text = page.getText() + " " + page.getTitle();
//								byte[] bytetext = (page.getTitle() + " " + page.getText()).getBytes(Charset.forName("US-ASCII"));
//								String text = new String(bytetext, Charset.forName("US-ASCII"));
								text = text.replaceAll("[[^\\p{L}\\p{Z}]]", " ").replaceAll("(?<=\\s|^).{1,2}?(?=\\s|$)", "").replaceAll("\\s{2,}", " ").toLowerCase().trim();

								String[] textArray = text.split(" ");				 
								long docid = Integer.parseInt(page.getID());		
								
								// Timestamp conversion
								SimpleDateFormat dateFormat = new SimpleDateFormat(props.getDateFormatPattern());
								Timestamp tstmp = null;
							    try {
									tstmp = new java.sql.Timestamp(dateFormat.parse(page.getTimestamp()).getTime());
								} catch (ParseException e) {
									logger.error("Cannot convert to Timeestamp " + page.getTimestamp());
									tstmp = new Timestamp(System.currentTimeMillis());
								}
								
								pageStore.addPage(docid, page.getTitle(), tstmp, textArray);						
								
								if (props.isBatchSeries()) {
									logger.debug("flushing "+ props.getBatchSize() + " pages");
									pageStore.flush();
								}
							}
							else {
								logger.trace("All " +  props.getMaxPages() +  " pages processed, exiting program");
								pageStore.flush();
								dbConnect.closeConnection();
								try {
									wxsp.setPageCallback(null);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								wxsp = null;
								return;
							}		 
							 
						}		
					});
					
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.fatal(e.getMessage() + " caused by " + e.getMessage());
				return;
			}
		
		while (props.allowPage() && file!=null){

			file = files.getNextFile();
			try {
				wxsp.parse();
			} catch (Exception e) {
				if (e instanceof NullPointerException) {
					logger.trace("exiting program");
				}
				else e.printStackTrace();
				file = null;
				break;
			}		
			
			
			if (file != null) {
				try {
					wxsp = WikiXMLParserFactory.getSAXParser(file.getAbsolutePath());
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} 
		

	}
}
			