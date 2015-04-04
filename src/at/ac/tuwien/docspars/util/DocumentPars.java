package at.ac.tuwien.docspars.util;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.ac.tuwien.docspars.io.FileProvider;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;

public class DocumentPars {

	private static final Logger logger = LogManager.getLogger(DocumentPars.class.getName());
	private static WikiXMLParser wxsp;

	public static void main(String args[]) throws SQLException {

		ApplicationContext context =  new ClassPathXmlApplicationContext("META-INF/application-context.xml", DocumentPars.class);
		FileProvider files = (FileProvider) context.getBean("fileProvider");
		ProcessPropertiesHandler props = (ProcessPropertiesHandler) context.getBean("processProperties");
		
		File file = files.getNextFile();
		if (file == null) {
			logger.fatal("no file provided -> exiting");
			return;
		}
		logger.debug("parsing " + file.getAbsolutePath());
		
		DocumentHandler docHandler = (DocumentHandler) context.getBean("documentHandler"); 
				
		try {
		while (files.getNextFile()!=null) {
			DocumentPars.wxsp = WikiXMLParserFactory.getSAXParser(file.getAbsolutePath());
			wxsp.setPageCallback(new WikiPageCallBackHandler(props, docHandler));
			wxsp.parse();
		}
		} catch (MalformedURLException e1) {
			logger.fatal("file path is not valid - terminating program" + e1.getMessage());
			return;
		} catch (EndOfProcessParameterReachedException eor) {
			logger.debug("End of Processing");
		}	
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
}
			