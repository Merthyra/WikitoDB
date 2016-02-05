package at.ac.tuwien.docspars.util;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import at.ac.tuwien.docspars.entity.PersistVariant;

public class ProcessPropertiesHandler {

//	private static final Logger logger = LogManager.getLogger(ProcessPropertiesHandler.class.getPackage().getName());
	private PersistVariant variant;
	private int batch_size;
	private int start_offset;
	private int max_pages;
	private int processed_Page_Count;
	private boolean use_systemTimestamp;
	private String date_format;
	private final String lan;
	private final int maxTermLength;
	private int dictsCached;
	private boolean updates;
	private final int reportLimit;

	public ProcessPropertiesHandler(int batch_size, int start_offset, int max_pages, String date_format, String language, String sc, int maxLength, int dictsCached, int reportLimit) {
		this.batch_size = batch_size > Integer.MAX_VALUE ? Integer.MAX_VALUE : batch_size;
		this.start_offset = start_offset > Integer.MAX_VALUE ? Integer.MAX_VALUE : start_offset;
		this.max_pages = max_pages > Integer.MAX_VALUE ? Integer.MAX_VALUE : max_pages;
		this.processed_Page_Count = 0;
		this.date_format = date_format;
		this.lan = language;
		this.variant = PersistVariant.valueOf(sc);
		if (variant == null) {
			throw new RuntimeException("db variant unnkown: " + sc);
		}
		this.maxTermLength = maxLength;
		this.updates=false;
		this.dictsCached = dictsCached;
		this.reportLimit = reportLimit;
		//logger.info("Process Properties " + " BATCH SIZE = " + this.batch_size + ", DOCUMENT START OFFSET = " + this.start_offset + ", MAX PROCESSED PAGES = " + this.max_pages + ", LANGUAGE = " + this.lan);
	}
	
	/**
	 * Init ProcessProperties Handler with preconfigured default settings (according to the project settings file)
	 */
	public ProcessPropertiesHandler() {
		this.batch_size = 1000;
		this.start_offset = 0;
		this.max_pages = 1000;
		this.processed_Page_Count = 0;
		this.date_format = "yyyy-MM-dd'T'hh:mm:ss'Z'";
		this.lan = "en";
		this.variant = PersistVariant.V1;
		this.maxTermLength = 100;
		this.updates=false;
		this.dictsCached = 0;
		this.reportLimit = 20;
	}

	public boolean skipPageDueToOffset() {
		return --this.start_offset >= 0;
	}

	public boolean allowPage() {
		return processed_Page_Count <= this.max_pages;
	}

	/**
	 * increments processed page count and compares it to max pages to processes
	 * 
	 * @return true if one more page allowed for processing
	 */
	public boolean allowNextPage() {
		return this.countDocument() <= this.max_pages;
	}

	/**
	 * increments the process execution counter
	 * 
	 * @return returns updated execution counter
	 */
	public int countDocument() {
		return ++this.processed_Page_Count;
	}

	/**
	 * @return the batch_size
	 */
	public int getBatch_size() {
		return batch_size;
	}

	/**
	 * @param batch_size
	 *            the batch_size to set
	 */
	protected void setBatch_size(int batch_size) {
		this.batch_size = batch_size;
	}

	/**
	 * @return the start_offset
	 */
	public long getStart_offset() {
		return start_offset;
	}

	/**
	 * @param start_offset
	 *            the start_offset to set
	 */
	protected void setStart_offset(int start_offset) {
		this.start_offset = start_offset;
	}

	/**
	 * @return the max_Pages
	 */
	public long getMax_Pages() {
		return max_pages;
	}

	/**
	 * @param max_Pages
	 *            the max_Pages to set
	 */
	protected void setMax_Pages(int max_Pages) {
		this.max_pages = max_Pages;
	}

	/**
	 * @return the processed_Page
	 */
	public int getProcessed_Page_Count() {
		return processed_Page_Count;
	}

	/**
	 * @param processed_Page
	 *            the processed_Page to set
	 */
	protected void setProcessed_Page_Count(int processed_Page) {
		this.processed_Page_Count = processed_Page;
	}

	/**
	 * @return the date_format
	 */
	public String getDate_format() {
		return date_format;
	}

	/**
	 * @param date_format
	 *            the date_format to set
	 */
	protected void setDate_format(String date_format) {
		this.date_format = date_format;
	}

	/**
	 * @return the scenario
	 */
	public Enum<PersistVariant> getScenario() {
		return variant;
	}

	/**
	 * @return the lan
	 */
	public String getLan() {
		return lan;
	}

	public int getMaxTermLength() {
		return this.maxTermLength;
	}

	/**
	 * @return the max_pages
	 */
	public int getMax_pages() {
		return max_pages;
	}

	/**
	 * @param max_pages
	 *            the max_pages to set
	 */
	protected void setMax_pages(int max_pages) {
		this.max_pages = max_pages;
	}
	
	protected void setUseSystemTimestamp(boolean bol) {
		this.use_systemTimestamp = bol;
	}
	protected boolean isSystemTimeStampUsed() {
		return this.use_systemTimestamp;
	}
	
	/**
	 * @return the variant
	 */
	public PersistVariant getVariant() {
		return variant;
	}

	/**
	 * @param variant the variant to set
	 */
	public void setVariant(PersistVariant variant) {
		this.variant = variant;
	}

	/**
	 * @return the dictsCached
	 */
	public int getDictsCached() {
		return dictsCached;
	}

	/**
	 * @param dictsCached the dictsCached to set
	 */
	public void setDictsCached(int dictsCached) {
		this.dictsCached = dictsCached;
	}

	/**
	 * @return the updates
	 */
	public boolean isUpdates() {
		return updates;
	}

	/**
	 * @param updates the updates to set
	 */
	public void setUpdates(boolean updates) {
		this.updates = updates;
	}

	public int getReportLimit() {
		return reportLimit;
	}
	
	
}
