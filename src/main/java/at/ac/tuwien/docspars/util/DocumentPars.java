package at.ac.tuwien.docspars.util;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.ac.tuwien.docspars.io.FileProvider;
import at.ac.tuwien.docspars.io.services.PersistanceServiceFactory;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;

public class DocumentPars {

	private static final Logger logger = LogManager.getLogger(DocumentPars.class.getPackage().getName());
	private static WikiXMLParser wxsp;

	public static void main(String args[]) throws SQLException {

		ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
		FileProvider files = (FileProvider) context.getBean("fileProvider");
		ProcessPropertiesHandler props = (ProcessPropertiesHandler) context.getBean("processProperties");
		//DocumentHandler docHandler = (DocumentHandler) context.getBean("documentHandler");
		CLIArgProcessor cliProc = (CLIArgProcessor) context.getBean("cliArgProcessor");
		PersistanceServiceFactory serviceFactory = (PersistanceServiceFactory) context.getBean("persistanceServiceFactory");	
		DocumentHandler docHandler=null;
		File file;
		try {
			// pass command line arguments to command line arguments processor
			// and update process properties automatically
			cliProc.init(args);
			docHandler = new DocumentHandler(serviceFactory.createInstance(props.getVariant()), props);
			logger.info("Process successfully initialized:\nProcess Properties: maxPages: " + props.getMax_pages() + " / batch size: " + props.getBatch_size() + " / start offset: " + props.getStart_offset() + " / db mode: " + props.getVariant());
			while ((file = files.getNextFile()) != null) {
				logger.info("Parsing File: " + file.getAbsolutePath());
				DocumentPars.wxsp = WikiXMLParserFactory.getSAXParser(file.getAbsolutePath());
				wxsp.setPageCallback(new WikiPageCallBackHandler(docHandler));
				wxsp.parse();
			}
			docHandler.flushAll();
		} catch (CommandLineOptionException coe) {
			System.err.println(coe.getMessage());
		} catch (MalformedURLException e1) {	
			logger.fatal("file path is not valid - terminating program" + e1.getMessage());
		} catch (PersistanceException pe) {
			logger.fatal(pe.getMessage() + " caused by " + pe.getCause());
		} catch (NullPointerException npe) {
			logger.fatal("Error acessing File, please check FilePath in Config " + npe.getMessage() + " " + npe.getClass());
			npe.printStackTrace();
		} catch (EndOfProcessReachedException eor) {
			logger.info("Max Number Of Pages processed");
//			docHandler.flushAll();
		} catch (Throwable e) {
			logger.fatal("Unspecified Exception/Error " + e.getMessage() + " cause " + e.getCause() + " StackTrace: \n" + e.getStackTrace().toString());
			e.printStackTrace();
		} finally {
			System.out.println("End of Processing:\n Wrote:\n " + docHandler.getMetrics());
//			System.out.println("skipped: " + (props.getProcessed_Page_Count()-docHandler.getMetrics().getNumberOfDocuments()) + " documents, because they were already in the db");
			System.out.println("processed files: " + System.getProperty("line.separator") + files.getProcessed());
			((ConfigurableApplicationContext) context).close();
		}
	}

}
