package ac.at.tuwien.wikipars.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProcessProperties {
	
	private static final Logger logger = LogManager.getLogger(ProcessProperties.class.getName());
	
	private final int batch_size;
	private final long start_offset;
	private long max_Pages;
	private int processed_Page = 0;
	private String date_format;
	
	public ProcessProperties () {
		
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
		this.batch_size = Integer.parseInt(prop.getProperty("batch_size"));
		logger.debug("batch_size = " + this.batch_size);
		this.start_offset = Long.parseLong(prop.getProperty("start_pages_offset"));
		logger.debug("start_offset = " + this.start_offset);
		this.date_format = prop.getProperty("date_format");
		
		String max_Pages = prop.getProperty("max_pages");
		if (max_Pages.equals("all")) {
			this.max_Pages = Integer.MAX_VALUE;
			logger.trace("reading all pages available according to provided files");
		}
		else {
			try {
				this.max_Pages = Long.parseLong(max_Pages);
				logger.debug("Reading " + this.max_Pages + " Files");
			}
			catch (NumberFormatException ioex) {
				logger.error("Wrong parameter in properties file : load_value must be either a number or 'max'");
				
			}
		}	
		
	}
	
	public String getDateFormatPattern() {
		return this.date_format;
	}
	
	public int getBatchSize() {
		return this.batch_size;
	}
	
	public long getStartPageOffset() {
		return this.start_offset;
	}
	
	public long getMaxPages() {
		return this.max_Pages;
	}
	
	public boolean isBatchSeries() {
		return this.processed_Page%this.batch_size==0;
	}
	
	/**
	 * increments processed page count and compares it to max pages to processes
	 * @return true if one more page allowed for processing
	 */
	public boolean allowPage() {
		return ++processed_Page > this.max_Pages;
	}

}
