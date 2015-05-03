package at.ac.tuwien.docspars.util;

import java.sql.Timestamp;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;

public class WikiPageCallBackHandler implements PageCallbackHandler {

	private static final Logger logger = LogManager.getLogger(WikiPageCallBackHandler.class.getName());
	private final ProcessPropertiesHandler processProperties;
	private final DocumentHandler docHandler;


	public WikiPageCallBackHandler(ProcessPropertiesHandler processProperties, DocumentHandler docHandler) {
		this.processProperties = processProperties;
		this.docHandler = docHandler;
	}

	@Override
	public void process(WikiPage page) {		
		if (!processProperties.skipPageDueToOffset() && processProperties.allowNextPage()) {
			logger.debug("parsing " + page.getTitle());
			// using title and text values for IR retrieval
			String text = page.getText() + " " + page.getTitle();
			List<String> cleanTerms = null;
			Timestamp tstmp = null;
			String title = page.getTitle();
			try {
				// now using the Lucene EnglishAnalyzer to tokenize the String
				cleanTerms = DocumentTextProcessor.clearUpText(text);
				title = DocumentTextProcessor.deAccent(title);
				title = DocumentTextProcessor.trimTextTitle(title);
				// converting String to Timestamp Datatype 	
				tstmp = DocumentTextProcessor.convertStringToSQLTimestamp(page.getTimestamp(), processProperties.getDate_format());
			}	catch (ParsingDocumentException e1) {
				logger.error("Error tokenizing Text Stream " + e1.getLocalizedMessage());
			}	
			docHandler.addPage(Integer.parseInt(page.getID()), Integer.parseInt(page.getRevid()), title, tstmp, cleanTerms); 
			// if document handler store is as large as batch_size, the documents are being stored in the db
			if ((processProperties.getProcessed_Page_Count() % processProperties.getBatch_size()) == 0) {
				logger.debug("inserting " + this.processProperties.getBatch_size() + " docs up to " + this.processProperties.getProcessed_Page_Count());
				this.docHandler.flushInsert();
				this.docHandler.reset();
			}
		}	
		else if(!processProperties.allowPage()) {
			logger.debug("inserting last documetns");
			this.docHandler.flushInsert();
			this.docHandler.reset();
			throw new EndOfProcessParameterReachedException();
		}
	}
}


