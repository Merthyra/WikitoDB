package ac.at.tuwien.wikipars.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ac.at.tuwien.wikipars.db.DBConnectionHandler;

public class FileProvider {
	
	private final String file_path;
	private final String file_type;
	private final String file_load_mode;
	private int maxFilesRead = 1;
	private ArrayList<File> files = null;
	private Iterator<File> it;
	
	private static final Logger logger = LogManager.getLogger(FileProvider.class.getName());
	
	public FileProvider() {
		Properties prop = new Properties();
		InputStream input = null;
		
		logger.trace("accessing properties file");
		try {
			input = new FileInputStream("files.properties");
			prop.load(input);			
		} catch (FileNotFoundException e) {
			logger.fatal("Cannot Finde Properties for IO Files" + " " + e.getMessage() + " cause >" + e.getCause());
		} catch (IOException ioex) {
			logger.fatal("Error Reading File" + " " + ioex.getMessage() + " cause >" + ioex.getCause());
		}
		this.file_path = prop.getProperty("file_location").toLowerCase();
		this.file_type = prop.getProperty("file_type").toLowerCase();
		this.file_load_mode = prop.getProperty("file_load_mode").toLowerCase();
		logger.debug("file-location = " + this.file_path);
		logger.debug("load-files = " + this.file_load_mode);
		logger.trace("propterties file loaded");
		File[] files = new File(this.file_path).listFiles();
		
		this.files = new ArrayList<File>(Arrays.asList(files));		
		this.it = this.files.iterator();
		logger.trace("Found " + this.files.size() + " Files in directory " + this.file_path);
		
		if (this.file_load_mode.equals("all")) {
			this.maxFilesRead = Integer.MAX_VALUE;
			logger.trace("reading all files in directory");
		}
		else {
			try {
				this.maxFilesRead = Integer.parseInt(this.file_load_mode);
				logger.trace("Reading " + this.maxFilesRead + " Files");
			}
			catch (NumberFormatException ioex) {
				logger.trace("Wrong parameter in properties file : load_value must be either a number or 'max'");
			}
		}	
	}
	
	
	public File getNextFile() {
		if (it.hasNext() && this.maxFilesRead > 0) {
			this.maxFilesRead--;
			return (File) this.it.next();
		}
		return null;
	}

}
