package at.ac.tuwien.docspars.util;

import at.ac.tuwien.docspars.entity.Mode;
import at.ac.tuwien.docspars.io.FileProvider;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

/**
 * Class Builds The Command Line Option Interface
 *
 * @author Hannes
 */
public class CLIArgProcessor {

  private static Logger logger = LogManager.getLogger(CLIArgProcessor.class);
  private Options options;
  private CommandLine cl;
  private final ProcessPropertiesHandler pH;
  private final FileProvider fP;

  public CLIArgProcessor(final ProcessPropertiesHandler props, final FileProvider file) {
    this.pH = props;
    this.fP = file;
  }

  public void init(final String[] args) {
    initOptions();
    parse(args);
    setProcessProps();
  }

  /**
   * The Command Line Options are hard-coded within this method
   */
  private void initOptions() {
    this.options = new Options();
    final Option help = new Option("help", "print this message");
    final Option version = new Option("version", "print build version");
    final Option debug = new Option("debug", "debug mode");
    final Option quiet = new Option("quiet", "no logging output");
    final Option systimestamp =
        new Option("st", "forces the system timestamp to be used for the inserts");
    OptionBuilder.withArgName("batchsize");
    OptionBuilder.hasArg();
    OptionBuilder.withDescription("number of documents to be written in one stroke");
    final Option batch = OptionBuilder.create("b");
    OptionBuilder.withArgName("maxpages");
    OptionBuilder.hasArg();
    OptionBuilder.withDescription("max number of pages to be processed");
    final Option maxpages = OptionBuilder.create("m");
    OptionBuilder.withArgName("pageoffset");
    OptionBuilder.hasArg();
    OptionBuilder.withDescription(
        "number of Documents to be scipped before starting, may be useful to continue parsing of partly processed files");
    final Option offset = OptionBuilder.create("o");
    OptionBuilder.withArgName("inputsourcefolder");
    OptionBuilder.hasArg();
    OptionBuilder.withDescription(
        "input folder where (compressed .bz2, .gzip) .xml document files are located");
    final Option inputFolder = OptionBuilder.create("i");
    OptionBuilder.withArgName("v");
    OptionBuilder.hasArg();
    OptionBuilder.withDescription("which variant to use [V1 (default), V2, V3, V4, V5] ");
    final Option variant = OptionBuilder.create("v");
    OptionBuilder.withArgName("u");
    OptionBuilder.withDescription("when flag is set document updates will be processed");
    final Option setUpdates = OptionBuilder.create("u");
    OptionBuilder.withArgName("on");
    OptionBuilder.withDescription("when flag is set, new documents are ommited");
    final Option ommitNewDocs = OptionBuilder.create("on");
    this.options.addOption(help);
    this.options.addOption(version);
    this.options.addOption(debug);
    this.options.addOption(quiet);
    this.options.addOption(inputFolder);
    this.options.addOption(offset);
    this.options.addOption(maxpages);
    this.options.addOption(batch);
    this.options.addOption(systimestamp);
    this.options.addOption(variant);
    this.options.addOption(setUpdates);
    this.options.addOption(ommitNewDocs);
  }

  private void parse(final String[] args) {
    final CommandLineParser parser = new GnuParser();
    try {
      // parse the command line arguments
      this.cl = parser.parse(this.options, args);
    } catch (final ParseException exp) {
      System.out.println(exp.getMessage());
      final HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("Docs2DB", this.options);
      throw new CommandLineOptionException(
          "Error Parsing Command Line Options: " + exp.getMessage());
    }
  }

  public void setProcessProps() {
    if (this.cl.hasOption("help")) {
      final HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("Docs2DB", this.options);
      throw new CommandLineOptionException("Help Message Printed");
    }
    if (this.cl.hasOption("version")) {
      System.out.println(
          at.ac.tuwien.docspars.util.DocumentPars.class.getPackage().getImplementationVersion());
      throw new CommandLineOptionException("Version Info Printed");
    }
    if (this.cl.hasOption("debug")) {
      final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
      final Configuration config = ctx.getConfiguration();
      final LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
      loggerConfig.setLevel(Level.DEBUG);
      ctx.updateLoggers();
    }
    if (this.cl.hasOption("quiet")) {
      final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
      final Configuration config = ctx.getConfiguration();
      final LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
      loggerConfig.setLevel(Level.ERROR);
      ctx.updateLoggers();
    }
    if (this.cl.hasOption("i")) {
      this.fP.setFilePath(this.cl.getOptionValue("i"));
      logger
          .info("SET INPUT SOURCE FOLDER TO PARAMETER MAX_DOCUMENTS TO " + this.cl.getOptionValue("i"));
    }
    if (this.cl.hasOption("st")) {
      this.pH.setUseSystemTimestamp(true);
      logger.info("PARSER USES SYSTEM TIMESTAMPS INSTEAD OF DOCUMENT TIMESTAMPS");
    }
    if (this.cl.hasOption("m")) {
      try {
        final int maxfiles = Integer.parseInt(this.cl.getOptionValue("m"));
        if (maxfiles <= 0 || maxfiles > Integer.MAX_VALUE) {
          throw new CommandLineOptionException("invalid numeric parameter");
        };
        this.pH.setMax_Pages(maxfiles);
        logger.info("SET PROCESS PARAMETER MAX_DOCUMENTS TO " + this.pH.getMax_Pages());
      } catch (final NullPointerException nex) {
        throw new CommandLineOptionException("invalid value for -(m)axpages value (empty)");
      } catch (final NumberFormatException nex) {
        throw new CommandLineOptionException(
            "invalid numeric parameter for -(m)axpages - must be in between 1 - "
                + Integer.MAX_VALUE);
      }
    }
    if (this.cl.hasOption("o")) {
      try {
        final int offset = Integer.parseInt(this.cl.getOptionValue("o"));
        if (offset <= 0 || offset > Integer.MAX_VALUE) {
          throw new CommandLineOptionException("invalid numeric parameter");
        };
        this.pH.setStart_offset(offset);
        logger.info("SET PROCESS PARAMETER FILE_OFFSET TO " + this.pH.getStart_offset());
      } catch (final NullPointerException nex) {
        throw new CommandLineOptionException("invalid value for -(o)ffset value (empty)");
      } catch (final NumberFormatException nex) {
        throw new CommandLineOptionException(
            "invalid numeric parameter for -(o)ffset - must be in between 1 - "
                + Integer.MAX_VALUE);
      }
    }
    if (this.cl.hasOption("b")) {
      try {
        final int batch = Integer.parseInt(this.cl.getOptionValue("b"));
        if (batch <= 0 || batch > Integer.MAX_VALUE) {
          throw new CommandLineOptionException("invalid numeric parameter");
        };
        this.pH.setBatch_size(batch);
        logger.info("SET PROCESS PARAMETER BATCH TO " + this.pH.getBatch_size());
      } catch (final NullPointerException nex) {
        throw new CommandLineOptionException("invalid value for -(b)atch value (empty)");
      } catch (final NumberFormatException nex) {
        throw new CommandLineOptionException(
            "invalid numeric parameter for -(b)atch - must be in between 1 - " + Integer.MAX_VALUE);
      }
    }
    if (this.cl.hasOption("v")) {
      int version = 0;
      try {
        version = Integer.parseInt(this.cl.getOptionValue("v"));
      } catch (final NumberFormatException ex) {
        throw new CommandLineOptionException(
            "invalid value for version value. should be integer value");
      }
      final Mode db = Mode.valueOf("V" + version);
      if (db == null) {
        throw new CommandLineOptionException(
            "unknown variant of processing: " + this.cl.getOptionValue("v"));
      }
      this.pH.setVariant(db);
      logger.info("SET PROCESS PARAMETER DB VARIANT TO " + this.pH.getVariant());
    }
    if (this.cl.hasOption("u")) {
      this.pH.setUpdatesProcessed(true);
      logger.info("Enabled update mode");
    }
    if (this.cl.hasOption("on")) {
      this.pH.setOmittNewDocs(true);
      logger.info("Enabled update mode");
    }
  }

}
