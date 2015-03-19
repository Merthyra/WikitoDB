package ac.at.tuwien.wikipars.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import ac.at.tuwien.wikipars.entity.*;
import ac.at.tuwien.wikipars.io.FileProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;
import ac.at.tuwien.wikipars.db.DBConnectionHandler;
import ac.at.tuwien.wikipars.db.DictDAO;
import ac.at.tuwien.wikipars.db.DictDAOWikiDB;

public class WikiPars {

	private static final Logger logger = LogManager.getLogger(WikiPars.class
			.getName());

	public static void main(String args[]) throws SQLException {

		logger.trace("Trying to establish connection to DB");
		DBConnectionHandler dbConnect = new DBConnectionHandler();
		try {
			dbConnect.connect();
		} catch (SQLException sqlex) {
			logger.fatal("Cannot Access Database: " + sqlex.getMessage()
					+ " : cause > " + sqlex.getCause().getMessage());
			return;
		} catch (IOException ioex) {
			logger.fatal("Error Accesssing Properties File: "
					+ ioex.getMessage() + " : cause '> "
					+ ioex.getCause().getMessage());
			return;
		} catch (ClassNotFoundException clex) {
			logger.fatal("ClassLoader Error: " + clex.getMessage()
					+ " : cause '> " + clex.getCause().getMessage());
			return;
		}
		
		logger.trace("Successfully connected to DB");
		DictDAO dict = new DictDAOWikiDB(dbConnect);

		// logger.trace("Start Parsing Documents");

		FileProvider files = new FileProvider();

		File file = files.getNextFile();

		logger.debug("parsing " + file.getAbsolutePath());

		// table containing all dict values from db to prevent infinite access
		// to db
		HashMap<String, Dict> dicttable = dict.readAll();
		// list of docs and terms to be written in one batch update
		ArrayList<Document> docList = new ArrayList<Document>();
		ArrayList<Term> termList = new ArrayList<Term>();

		
		try {
			WikiXMLParser wxsp = WikiXMLParserFactory.getSAXParser(file.getAbsolutePath());
			
				wxsp.setPageCallback(new PageCallbackHandler() {

					@Override
					public void process(WikiPage page) {
						logger.debug("processing wiki-page " + page.getID()+ " title: " + page.getTitle() + " timestamp: "+ page.getTimestamp());
						String text = page.getTitle() + page.getWikiText();
						text = text.replaceAll("[^\\p{L}\\p{Z}]", " ").replaceAll("\\s{2,}", " ");
						 // System.out.println(text);
						 
						 String[] textArray = text.split(" ");
						 
						 PreparedStatement dictinsertstmt = null;
						 PreparedStatement docinsertstmt = null;
						 PreparedStatement terminsertstmt = null;
						 
						 long docid = Integer.parseInt(page.getID());
						 
						 try {
							dictinsertstmt = dbConnect.getConnection().prepareStatement("INSERT INTO dict (term) VALUES (?)",Statement.RETURN_GENERATED_KEYS);
							docinsertstmt = dbConnect.getConnection().prepareStatement("INSERT INTO docs (docid, added, removed, name, leng) VALUES (?,?,?,?,?)");
							terminsertstmt = dbConnect.getConnection().prepareStatement("INSERT INTO terms (tid, did, pos) VALUES (?,?,?)");
							
							for (int i = 0; i < textArray.length; i++) {
								if (!dicttable.containsKey(textArray[i])) {
									dictinsertstmt.setString(1, textArray[i]);
									long id = dictinsertstmt.executeUpdate();
									dicttable.put(textArray[i], new Dict(id,textArray[i]));
								}
								
								terminsertstmt.setLong(1, dicttable.get(textArray[i]).getId());		
								terminsertstmt.setLong(2, docid);
								terminsertstmt.setInt(3, i);
								terminsertstmt.execute();
								
							}
							docList.add(new Document(docid, page.getTitle(), page.getTimestamp(),textArray.length));
						 }
						 catch (SQLException ex) {
							 logger.error("Error executing Statement " + ex.getMessage() + " cause > " + ex.getCause());
						 }

					}		
				});
			 
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SQLException sqlex) {
			
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
			