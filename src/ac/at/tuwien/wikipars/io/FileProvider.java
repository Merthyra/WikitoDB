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
	private int max_Files;
	private ArrayList<File> files = null;
	private Iterator<File> it;
	
	private static final Logger logger = LogManager.getLogger(FileProvider.class.getName());
	
	public FileProvider() {
		Properties prop = new Properties();
		InputStream input = null;
		
		logger.trace("accessing properties file");
		try {
			input = new FileInputStream("project-settings.properties");
			prop.load(input);			
		} catch (FileNotFoundException e) {
			logger.fatal("Cannot Finde Properties for IO Files" + " " + e.getMessage() + " cause >" + e.getCause());
		} catch (IOException ioex) {
			logger.fatal("Error Reading File" + " " + ioex.getMessage() + " cause >" + ioex.getCause());
		}
		this.file_path = prop.getProperty("file_location").toLowerCase();
		this.file_type = prop.getProperty("file_type").toLowerCase();

		
		String maxfiles = prop.getProperty("max_files");		
		if (maxfiles.equals("all")) {
			this.max_Files = Integer.MAX_VALUE;	
		}
		else {
			this.max_Files = Integer.parseInt(maxfiles);
		}
		logger.debug("file-location = " + this.file_path);

		logger.trace("propterties file loaded");
		File[] files = new File(this.file_path).listFiles();
		
		this.files = new ArrayList<File>(Arrays.asList(files));		
		this.it = this.files.iterator();
		logger.trace("Found " + this.files.size() + " Files in directory " + this.file_path);
		
		
	}
	
	
	public File getNextFile() {
		if (it.hasNext() && this.max_Files > 0) {
			this.max_Files--;
			return (File) this.it.next();
		}
		return null;
	}

}
