package at.ac.tuwien.docspars.util;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import at.ac.tuwien.docspars.io.FileProvider;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;

public class DocumentPars {

	private static final Logger logger = LogManager.getLogger(DocumentPars.class.getName());
	private static WikiXMLParser wxsp;

	public static void main(String args[]) throws SQLException {

		ApplicationContext context =  new FileSystemXmlApplicationContext("./META-INF/application-context.xml");
		FileProvider files = (FileProvider) context.getBean("fileProvider");
		ProcessPropertiesHandler props = (ProcessPropertiesHandler) context.getBean("processProperties");
		DocumentHandler docHandler = (DocumentHandler) context.getBean("documentHandler");
		CLIArgProcessor cliProc = (CLIArgProcessor) context.getBean("cliArgProcessor");

		try {
			// pass command line arguments to command line arguments processor and update process properties automatically
			cliProc.init(args);	
			File file;
			while ((file = files.getNextFile()) != null) {
				logger.debug("Parsing Document: " + file.getAbsolutePath());		
				DocumentPars.wxsp = WikiXMLParserFactory.getSAXParser(file.getAbsolutePath());
				wxsp.setPageCallback(new WikiPageCallBackHandler(props, docHandler));
				wxsp.parse();
			}
		} catch (CommandLineOptionException coe) {
			logger.debug(coe.getMessage());
		} catch (MalformedURLException e1) {
			logger.fatal("file path is not valid - terminating program" + e1.getMessage());
		} catch (NullPointerException npe) {
			logger.fatal("Error acessing File, please check FilePath in Config " + npe.getMessage() + " " + npe.getClass());
			npe.printStackTrace();
		} catch (EndOfProcessParameterReachedException eor) {
			docHandler.flushInsert();
			logger.info("End of Processing");
		} catch (Exception e) {
			logger.fatal("Unspecified Exception (most likely from WikiXMLParser Library) caught " + e.getMessage());
			e.printStackTrace();
		} finally {
			((ConfigurableApplicationContext)context).close();
		}	
	}

}
