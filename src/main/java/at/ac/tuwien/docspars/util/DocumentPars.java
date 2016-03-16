package at.ac.tuwien.docspars.util;

import at.ac.tuwien.docspars.io.FileProvider;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.SQLException;

public class DocumentPars {

  private static final Logger logger = LogManager.getLogger(DocumentPars.class);

  private static WikiXMLParser wxsp;

  public static void main(final String args[]) throws SQLException {

    final ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
    final DocumentPars documentParser = (DocumentPars) context.getBean("docsPars");

    // PersistanceServiceFactory serviceFactory = (PersistanceServiceFactory)
    // context.getBean("persistanceServiceFactory");
    // DocumentHandler docHandler=null;
    File file;
    try {
      // pass command line arguments to command line arguments processor
      // and update process properties automatically
      documentParser.getCommandLineArgumentProcessor().init(args);
      documentParser.getFileProvider().init();
      documentParser.environmentService.initialize(documentParser.getProcessPropertiesHandler().getVariant());
      logger.info("Process successfully initialized:\n" + "Process Properties: offset: {} maxPages: {} batch-size: {}  db-mode: {}",
          documentParser.getProcessPropertiesHandler().getStart_offset(), documentParser.getProcessPropertiesHandler().getMax_pages(),
          documentParser.getProcessPropertiesHandler().getBatch_size(), documentParser.getProcessPropertiesHandler().getVariant());

      while ((file = documentParser.getFileProvider().getNextFile()) != null) {
        logger.info("Parsing File: {}", file.getAbsolutePath());
        DocumentPars.wxsp = WikiXMLParserFactory.getSAXParser(file.getAbsolutePath());
        wxsp.setPageCallback(new WikiPageCallBackHandler(documentParser.getDocumentHandler()));
        wxsp.parse();
      }
      documentParser.getDocumentHandler().finalize();
    } catch (final CommandLineOptionException coe) {
      System.err.println(coe.getMessage());
    } catch (final MalformedURLException e1) {
      logger.fatal("file path is not valid - terminating program", e1);
    } catch (final PersistanceException pe) {
      logger.fatal("Persistence error " + pe);
    } catch (final NullPointerException npe) {
      logger.fatal("Error acessing File, please check FilePath in Config ", npe);
      npe.printStackTrace();
    } catch (final EndOfProcessReachedException eor) {
      logger.info("Max Number Of Pages processed");
      // docHandler.flushAll();
    } catch (final Throwable e) {
      logger.fatal("Unspecified Exception/Error StackTrace: \n" + e);
      e.printStackTrace();
    } finally {
      logger.info("End of Processing:\n Wrote:\n " + documentParser.getDocumentHandler() != null ? documentParser.getEnvironmentService().getProcessMetrics()
          : "no process metrics available");
      logger.info("processed files: " + System.getProperty("line.separator") + documentParser.getFileProvider().getProcessed());
      ((ConfigurableApplicationContext) context).close();
    }
  }

  private DocumentPars documentParser;
  private final EnvironmentService environmentService;
  private final FileProvider fileProvider;
  private final CLIArgProcessor commandLineArgumentProcessor;

  private final DocumentHandler documentHandler;

  private final ProcessPropertiesHandler processPropertiesHandler;

  public DocumentPars(final EnvironmentService environmentService, final FileProvider fileProvider, final CLIArgProcessor cliArgProcessor,
      final DocumentHandler documentHandler, final ProcessPropertiesHandler processPropertiesHandler) {
    this.environmentService = environmentService;
    this.fileProvider = fileProvider;
    this.commandLineArgumentProcessor = cliArgProcessor;
    this.documentHandler = documentHandler;
    this.processPropertiesHandler = processPropertiesHandler;
  }


  /**
   * Gets the commandLineArgumentProcessor for DocumentPars.
   *
   * @return commandLineArgumentProcessor
   */
  private CLIArgProcessor getCommandLineArgumentProcessor() {
    return this.commandLineArgumentProcessor;
  }



  /**
   * Gets the documentHandler for DocumentPars.
   *
   * @return documentHandler
   */
  private DocumentHandler getDocumentHandler() {
    return this.documentHandler;
  }



  /**
   * Gets the environmentService for DocumentPars.
   *
   * @return environmentService
   */
  private EnvironmentService getEnvironmentService() {
    return this.environmentService;
  }



  /**
   * Gets the fileProvider for DocumentPars.
   *
   * @return fileProvider
   */
  private FileProvider getFileProvider() {
    return this.fileProvider;
  }



  /**
   * Gets the processPropertiesHandler for DocumentPars.
   *
   * @return processPropertiesHandler
   */
  private ProcessPropertiesHandler getProcessPropertiesHandler() {
    return this.processPropertiesHandler;
  }
}


