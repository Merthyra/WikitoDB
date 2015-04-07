package at.ac.tuwien.docspars.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.ac.tuwien.docspars.entity.Scenario;

public class ProcessPropertiesHandler {
	
	private static final Logger logger = LogManager.getLogger(ProcessPropertiesHandler.class.getName());
	public static Enum<Scenario> scenario;
	
	private int batch_size;
	private long start_offset;
	private long max_pages;
	private int processed_Page;
	private String date_format;
	private final String lan;
	
	
	public ProcessPropertiesHandler(int batch_size, long start_offset, long max_pages, String date_format, String language, String sc) {
		this.batch_size = batch_size;
		this.start_offset = start_offset;
		this.max_pages = max_pages;
		this.processed_Page = 0;
		this.date_format = date_format;
		this.lan = language;
		if (sc.equals("sc1")) 
			{ ProcessPropertiesHandler.scenario = Scenario.sc1; } 
		else 
			{ ProcessPropertiesHandler.scenario = Scenario.sc2;}
	}
	
	@SuppressWarnings("unused")
	private ProcessPropertiesHandler(){
		this.lan = "en";
		
	};

	public boolean skipPageDueToOffset() {
		return this.start_offset-- > 0;
	}

	/**
	 * increments processed page count and compares it to max pages to processes
	 * @return true if one more page allowed for processing
	 */
	public boolean allowPage() {
		return processed_Page < this.max_pages;
	}
	
	public boolean allowNextPage() {
		return ++processed_Page < this.max_pages;
	}

	/**
	 * @return the batch_size
	 */
	public int getBatch_size() {
		return batch_size;
	}

	/**
	 * @param batch_size the batch_size to set
	 */
	public void setBatch_size(int batch_size) {
		this.batch_size = batch_size;
	}

	/**
	 * @return the start_offset
	 */
	public long getStart_offset() {
		return start_offset;
	}

	/**
	 * @param start_offset the start_offset to set
	 */
	public void setStart_offset(long start_offset) {
		this.start_offset = start_offset;
	}

	/**
	 * @return the max_Pages
	 */
	public long getMax_Pages() {
		return max_pages;
	}

	/**
	 * @param max_Pages the max_Pages to set
	 */
	public void setMax_Pages(long max_Pages) {
		this.max_pages = max_Pages;
	}

	/**
	 * @return the processed_Page
	 */
	public int getProcessed_Page() {
		return processed_Page;
	}

	/**
	 * @param processed_Page the processed_Page to set
	 */
	public void setProcessed_Page(int processed_Page) {
		this.processed_Page = processed_Page;
	}

	/**
	 * @return the date_format
	 */
	public String getDate_format() {
		return date_format;
	}

	/**
	 * @param date_format the date_format to set
	 */
	public void setDate_format(String date_format) {
		this.date_format = date_format;
	}

	/**
	 * @return the scenario
	 */
	public Enum<Scenario> getScenario() {
		return scenario;
	}

	/**
	 * @return the lan
	 */
	public String getLan() {
		return lan;
	}

	
}
