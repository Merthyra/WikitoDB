package at.ac.tuwien.docspars.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;

public class WikiPageCallBackHandler implements PageCallbackHandler {
	
	private static final Logger logger = LogManager.getLogger(WikiPageCallBackHandler.class.getName());

	ProcessPropertiesHandler processProperties;
	DocumentHandler docHandler;
	
	
	public WikiPageCallBackHandler(ProcessPropertiesHandler processProperties, DocumentHandler docHandler) {
		this.processProperties = processProperties;
		this.docHandler = docHandler;
	}


	@Override
	public void process(WikiPage page) {
		if (!processProperties.skipPageDueToOffset() && processProperties.allowNextPage()) {
			
			// using title and text values for IR retrieval
			String text = page.getText() + " " + page.getTitle();
			List<String> cleanTerms = null;
			try {
				// now using the Lucene Analyzer to tokenize the String with the englishanalyzer by default
				cleanTerms = new DocumentTextProcessor().clearUpText(text);
			} catch (ParsingDocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
			// converting String to Timestamp Datatype 	
			SimpleDateFormat dateFormat = new SimpleDateFormat(processProperties.getDate_format());
			Timestamp tstmp = null;
		    try {
				tstmp = new java.sql.Timestamp(dateFormat.parse(page.getTimestamp()).getTime());
			} catch (ParseException e) {
				logger.error("Cannot convert to Timeestamp " + page.getTimestamp());
				tstmp = new Timestamp(System.currentTimeMillis());
			}
		    docHandler.addPage(Long.parseLong(page.getID()), page.getTitle(), tstmp, cleanTerms);
		    
		    
		}	
		else {
			throw new EndOfProcessParameterReachedException();
		}
		 
	}
		
}
	

