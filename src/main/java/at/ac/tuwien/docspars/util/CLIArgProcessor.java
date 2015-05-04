package at.ac.tuwien.docspars.util;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import at.ac.tuwien.docspars.io.FileProvider;

/**
 * Class Builds The Command Line Option Interface
 * 
 * @author Hannes
 */
public class CLIArgProcessor {

	private Options options;
	private CommandLine cl;
	@SuppressWarnings("unused")
	private ProcessPropertiesHandler pH;
	private FileProvider fP;

	private static Logger logger = LogManager.getLogger(CLIArgProcessor.class.getName());

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
		@SuppressWarnings("static-access")
		Option inputFolder = OptionBuilder.withArgName("p").hasArg().withDescription("input folder where (compressed .bz2, .gzip) .xml document files are located").create("inputsourcefolder");
		options.addOption(help);
		options.addOption(version);
		options.addOption(debug);
		options.addOption(quiet);
		options.addOption(inputFolder);
	}

	private void parse(String[] args) {
		CommandLineParser parser = new GnuParser();
		try {
			// parse the command line arguments
			this.cl = parser.parse(options, args);
		} catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
			logger.error("Encountred problem with parsing command line options");
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
		if (cl.hasOption("p")) {
			this.fP.setFilePath(options.getOption("p").getValue());
		}

	}

}
