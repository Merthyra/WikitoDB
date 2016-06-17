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

public class DocumentPars {

  private static final Logger logger = LogManager.getLogger(DocumentPars.class);
  private static WikiXMLParser wxsp;

  private final EnvironmentService environmentService;
  private final FileProvider fileProvider;
  private final DocumentHandler documentHandler;
  private final ProcessPropertiesHandler processPropertiesHandler;

  public DocumentPars(final EnvironmentService environmentService, final FileProvider fileProvider, final DocumentHandler documentHandler,
      final ProcessPropertiesHandler processPropertiesHandler) {
    this.environmentService = environmentService;
    this.fileProvider = fileProvider;
    this.documentHandler = documentHandler;
    this.processPropertiesHandler = processPropertiesHandler;
  }

  public static void main(final String args[]) {

    final ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
    final DocumentPars documentParser = (DocumentPars) context.getBean("docsPars");
    new CLIArgProcessor(documentParser.processPropertiesHandler, documentParser.fileProvider).init(args);
    File file;
    try {
      // pass command line arguments to command line arguments processor
      // and update process properties automatically
      documentParser.fileProvider.init();
      documentParser.environmentService.initialize(documentParser.processPropertiesHandler.getVariant());
      documentParser.logSetupMsg();
      documentParser.registerShutdownHook();

      while ((file = documentParser.fileProvider.getNextFile()) != null) {
        logger.info("Parsing File: {}", file.getAbsolutePath());
        DocumentPars.wxsp = WikiXMLParserFactory.getSAXParser(file.getAbsolutePath());
        wxsp.setPageCallback(new WikiPageCallBackHandler(documentParser.documentHandler));
        wxsp.parse();
      }
      documentParser.documentHandler.finalize();
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
    } catch (final Throwable e) {
      logger.fatal("Unspecified Exception/Error StackTrace: \n" + e);
      e.printStackTrace();
    } finally {
      logger.info("End of Processing:\n Wrote:\n "
          + documentParser.documentHandler != null ? documentParser.environmentService.getProcessMetrics()
              : "no process metrics available");
      logger.info("processed files: " + System.getProperty("line.separator") + documentParser.fileProvider.getProcessed());
      ((ConfigurableApplicationContext) context).close();
    }
  }

  private void registerShutdownHook() {
    Thread mainThread = Thread.currentThread();
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        logger.info("Requesting graceful shutdown, waiting for already processed elements to be persisted");
        EnvironmentService.terminationRequested = true;
        try {
          mainThread.join();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
  }

  void logSetupMsg() {
    logger.info("Process successfully initialized:\nProcess Properties: offset: {} maxPages: {} batch-size: {}  db-mode: {}",
        processPropertiesHandler.getStart_offset(), processPropertiesHandler.getMax_pages(), processPropertiesHandler.getBatch_size(),
        processPropertiesHandler.getVariant());
  }
}
