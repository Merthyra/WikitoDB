package at.ac.tuwien.docspars.util;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import at.ac.tuwien.docspars.entity.PersistVariant;
import at.ac.tuwien.docspars.io.FileProvider;

/**
 * Class Builds The Command Line Option Interface
 * 
 * @author Hannes
 */
public class CLIArgProcessor {

	private Options options;
	private CommandLine cl;
	private ProcessPropertiesHandler pH;
	private FileProvider fP;
	private static Logger logger = LogManager.getLogger(CLIArgProcessor.class.getPackage().getName());

	public CLIArgProcessor(ProcessPropertiesHandler props, FileProvider file) {
		this.pH = props;
		this.fP = file;
	}

	public void init(String[] args) {
		initOptions();
		parse(args);
		setProcessProps();
	}

	/**
	 * The Command Line Options are hard-coded within this method
	 */
	private void initOptions() {
		options = new Options();
		Option help = new Option("help", "print this message");
		Option version = new Option("version", "print build version");
		Option debug = new Option("debug", "debug mode");
		Option quiet = new Option("quiet", "no logging output");
		Option systimestamp = new Option("st","forces the system timestamp to be used for the inserts");
		@SuppressWarnings("static-access")
		Option batch = OptionBuilder.withArgName("batchsize").hasArg().withDescription("number of documents to be written in one stroke").create("b");
		@SuppressWarnings("static-access")
		Option maxpages = OptionBuilder.withArgName("maxpages").hasArg().withDescription("max number of pages to be processed").create("m");
		@SuppressWarnings("static-access")
		Option offset = OptionBuilder.withArgName("pageoffset").hasArg().withDescription("number of Documents to be scipped before starting, may be useful to continue parsing of partly processed files").create("o");
		@SuppressWarnings("static-access")
		Option inputFolder = OptionBuilder.withArgName("inputsourcefolder").hasArg().withDescription("input folder where (compressed .bz2, .gzip) .xml document files are located").create("i");
		@SuppressWarnings("static-access")
		Option variant = OptionBuilder.withArgName("v").hasArg().withDescription("which variant to use [V1 (default), V2, V3, V4, V5] ").create("v");
		@SuppressWarnings("static-access")
		Option setUpdates = OptionBuilder.withArgName("u").hasArg().withDescription("which variant to use [V1 (default), V2, V3, V4, V5] ").create("u");
		
		options.addOption(help);
		options.addOption(version);
		options.addOption(debug);
		options.addOption(quiet);
		options.addOption(inputFolder);
		options.addOption(offset);
		options.addOption(maxpages);
		options.addOption(batch);
		options.addOption(systimestamp);
		options.addOption(variant);
		options.addOption(setUpdates);	
	}

	private void parse(String[] args) {
		CommandLineParser parser = new GnuParser();
		try {
			// parse the command line arguments
			this.cl = parser.parse(options, args);
		} catch (ParseException exp) {
			// oops, something went wrong	
			System.out.println(exp.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Docs2DB", options);
			throw new CommandLineOptionException("Error Parsing Command Line Options: " + exp.getMessage());
		}
	}

	public void setProcessProps() {
		if (cl.hasOption("help")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Docs2DB", options);
			throw new CommandLineOptionException("Help Message Printed");
		}
		if (cl.hasOption("version")) {
			System.out.println(at.ac.tuwien.docspars.util.DocumentPars.class.getPackage().getImplementationVersion());
			throw new CommandLineOptionException("Version Info Printed");
		}
		if (cl.hasOption("debug")) {
			LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
			Configuration config = ctx.getConfiguration();
			LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
			loggerConfig.setLevel(Level.DEBUG);
			ctx.updateLoggers();
		}
		if (cl.hasOption("quiet")) {
			LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
			Configuration config = ctx.getConfiguration();
			LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
			loggerConfig.setLevel(Level.ERROR);
			ctx.updateLoggers();
		}
		if (cl.hasOption("i")) {
			this.fP.updateFilePath(cl.getOptionValue("i"));
			logger.info("SET INPUT SOURCE FOLDER TO PARAMETER MAX_DOCUMENTS TO " + cl.getOptionValue("i"));
		}
		if (cl.hasOption("st")) {
			this.pH.setUseSystemTimestamp(true);
			logger.info("PARSER USES SYSTEM TIMESTAMPS INSTEAD OF DOCUMENT TIMESTAMPS");
		}
		if (cl.hasOption("m")) {
			try {
				int maxfiles = Integer.parseInt(cl.getOptionValue("m"));
				if (maxfiles <= 0 || maxfiles > Integer.MAX_VALUE) {throw new CommandLineOptionException("invalid numeric parameter");};
				this.pH.setMax_Pages(maxfiles);
				logger.info("SET PROCESS PARAMETER MAX_DOCUMENTS TO " + this.pH.getMax_Pages());
			} catch (NullPointerException nex) {
				throw new CommandLineOptionException("invalid value for -(m)axpages value (empty)"); 
			} catch (NumberFormatException nex) {
				throw new CommandLineOptionException("invalid numeric parameter for -(m)axpages - must be in between 1 - " + Integer.MAX_VALUE); 
			} 
		}
		if (cl.hasOption("o")) {
			try {
				int offset = Integer.parseInt(cl.getOptionValue("o"));
				if (offset <= 0 || offset > Integer.MAX_VALUE) {throw new CommandLineOptionException("invalid numeric parameter");};
				this.pH.setStart_offset(offset);
				logger.info("SET PROCESS PARAMETER FILE_OFFSET TO " +  this.pH.getStart_offset());
			} catch (NullPointerException nex) {
				throw new CommandLineOptionException("invalid value for -(o)ffset value (empty)"); 			
			} catch (NumberFormatException nex) {
				throw new CommandLineOptionException("invalid numeric parameter for -(o)ffset - must be in between 1 - " + Integer.MAX_VALUE); 
			} 
		}
		if (cl.hasOption("b")) {
			try {
				int batch = Integer.parseInt(cl.getOptionValue("b"));
				if (batch <= 0 || batch > Integer.MAX_VALUE) {throw new CommandLineOptionException("invalid numeric parameter");};
				this.pH.setBatch_size(batch);
				logger.info("SET PROCESS PARAMETER BATCH TO " + this.pH.getBatch_size());
			} catch (NullPointerException nex) {
				throw new CommandLineOptionException("invalid value for -(b)atch value (empty)"); 			
			} catch (NumberFormatException nex) {
				throw new CommandLineOptionException("invalid numeric parameter for -(b)atch - must be in between 1 - " + Integer.MAX_VALUE); 
			} 
		}
		if (cl.hasOption("v")) {
			//DBVariant db = DBVariant.valueOf(cl.getOptionValue("v"));
			int version = 0;
			try{
			 version = Integer.parseInt(cl.getOptionValue("v"));	 
			}
			catch (NumberFormatException ex) {
				throw new CommandLineOptionException("invalid value for version value. should be integer value"); 		
			}
			PersistVariant db = PersistVariant.valueOf("V"+version);
			if (db == null) {
				throw new CommandLineOptionException("unknown variant of processing: " + cl.getOptionValue("v"));
			}
			pH.setVariant(db);
			logger.info("SET PROCESS PARAMETER DB VARIANT TO " + this.pH.getVariant());
		}
		// to be removed, not required any more
		if (cl.hasOption("u")) {
			pH.setUpdates(true);			
		}					
	}

}
