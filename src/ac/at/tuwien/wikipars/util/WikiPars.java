package ac.at.tuwien.wikipars.util;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
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
	
	private static final Logger logger = LogManager.getLogger(WikiPars.class.getName());
	
	public static void main (String args[]) {
		
		logger.trace("Trying to establish connection to DB");
		DBConnectionHandler dbConnect = new DBConnectionHandler();
		try {
			dbConnect.connect();
		}
		catch (SQLException sqlex) {
			logger.fatal("Cannot Access Database: " + sqlex.getMessage() + " : cause > " + sqlex.getCause().getMessage());
			return;
		}
		catch (IOException ioex) {
			logger.fatal("Error Accesssing Properties File: " + ioex.getMessage() + " : cause '> " + ioex.getCause().getMessage());
			return;
		}
		catch (ClassNotFoundException clex) {
			logger.fatal("ClassLoader Error: " + clex.getMessage() + " : cause '> " + clex.getCause().getMessage());
			return;
		}
		logger.trace("Successfully connected to DB");
		DictDAO dict = new DictDAOWikiDB(dbConnect);
		
		Set<String> dicttable =  dict.readAll();
		
		//logger.trace("Start Parsing Documents");
		
		FileProvider files = new FileProvider();
		
		File file = files.getNextFile();
		
		
		logger.debug("parsing "+ file.getAbsolutePath());
		
        
        try {
        	WikiXMLParser wxsp = WikiXMLParserFactory.getSAXParser(file.getAbsolutePath());   
            wxsp.setPageCallback(new PageCallbackHandler() { 

						@Override
						public void process(WikiPage page) {
										
							logger.debug("processing wiki-page " + page.getID() + " title: "+ page.getTitle() + " timestamp: " + page.getTimestamp());
							String text = page.getTitle() + page.getWikiText();
							text = text.replaceAll("[^\\p{L}\\p{Z}]"," ").replaceAll("\\s{2,}", " ");
							//System.out.println(text);
							
							String[] textArray = text.split(" ");
							for (int i = 0; i < textArray.length; i++) {
								dicttable.add(textArray[i]);
							}
							
							
								
						}
            });

                
           wxsp.parse();
        }catch(Exception e) {
                e.printStackTrace();
        }
		
		

		
		
		
		
		
		
		
		
		
	}

}
