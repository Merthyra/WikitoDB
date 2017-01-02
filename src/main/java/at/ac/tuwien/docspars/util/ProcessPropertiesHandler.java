package at.ac.tuwien.docspars.util;

// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;

import at.ac.tuwien.docspars.entity.Mode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProcessPropertiesHandler {
  private static final Logger logger = LogManager.getLogger(ProcessPropertiesHandler.class);
  private Mode variant;
  private int batch_size;
  private int start_offset;
  private int max_pages;
  private boolean use_systemTimestamp;
  private String date_format;
  private final String lan;
  private final int maxTermLength;
  private int dictsCached;
  private boolean updates;
  private final int reportLimit;
  private boolean newDocsOmitted;

  public ProcessPropertiesHandler(final int batch_size, final int start_offset, final int max_pages, final String date_format,
      final String language, final String sc, final int maxLength, final int dictsCached, final int reportLimit,
      final String newDocsOmitted, final String updates) {
    this.batch_size = batch_size > Integer.MAX_VALUE ? Integer.MAX_VALUE : batch_size;
    this.start_offset = start_offset > Integer.MAX_VALUE ? Integer.MAX_VALUE : start_offset;
    this.max_pages = max_pages > Integer.MAX_VALUE ? Integer.MAX_VALUE : max_pages;
    this.date_format = date_format;
    this.lan = language;
    this.variant = Mode.valueOf(sc);
    if (this.variant == null) {
      throw new RuntimeException("db variant unnkown: " + sc);
    }
    this.maxTermLength = maxLength;
    this.updates = Boolean.valueOf(updates);
    this.dictsCached = dictsCached;
    this.reportLimit = reportLimit;
    this.newDocsOmitted = Boolean.valueOf(newDocsOmitted);
    logger.info("Properties successfully initialized");
  }

  /**
   * Init ProcessProperties Handler with preconfigured default settings (according to the project settings file)
   */
  public ProcessPropertiesHandler() {
    this.batch_size = 1000;
    this.start_offset = 0;
    this.max_pages = 1000;
    this.date_format = "yyyy-MM-dd'T'hh:mm:ss'Z'";
    this.lan = "en";
    this.variant = Mode.V1;
    this.maxTermLength = 100;
    this.updates = false;
    this.dictsCached = 0;
    this.reportLimit = 20;
    this.newDocsOmitted = false;
  }

  public boolean skipPageDueToOffset() {
    return --this.start_offset >= 0;
  }

  /**
   * @return the batch_size
   */
  public int getBatch_size() {
    return this.batch_size;
  }

  /**
   * @param batch_size the batch_size to set
   */
  protected void setBatch_size(final int batch_size) {
    this.batch_size = batch_size;
  }

  /**
   * @return the start_offset
   */
  public int getStart_offset() {
    return this.start_offset;
  }

  /**
   * @param start_offset the start_offset to set
   */
  protected void setStart_offset(final int start_offset) {
    this.start_offset = start_offset;
  }

  /**
   * @return the max_Pages
   */
  public int getMax_Pages() {
    return this.max_pages;
  }

  /**
   * @param max_Pages the max_Pages to set
   */
  protected void setMax_Pages(final int max_Pages) {
    this.max_pages = max_Pages;
  }

  /**
   * @return the date_format
   */
  public String getDate_format() {
    return this.date_format;
  }

  /**
   * @param date_format the date_format to set
   */
  protected void setDate_format(final String date_format) {
    this.date_format = date_format;
  }

  /**
   * @return the scenario
   */
  public Enum<Mode> getScenario() {
    return this.variant;
  }

  /**
   * @return the lan
   */
  public String getLan() {
    return this.lan;
  }

  public int getMaxTermLength() {
    return this.maxTermLength;
  }

  boolean isDocumentFrequencyStored() {
    return (this.variant == Mode.V3 || this.variant == Mode.V4 || this.variant == Mode.V5);
  }

  /**
   * @return the max_pages
   */
  public int getMax_pages() {
    return this.max_pages;
  }

  /**
   * @param max_pages the max_pages to set
   */
  protected void setMax_pages(final int max_pages) {
    this.max_pages = max_pages;
  }

  protected void setUseSystemTimestamp(final boolean bol) {
    this.use_systemTimestamp = bol;
  }

  protected boolean isSystemTimeStampUsed() {
    return this.use_systemTimestamp;
  }

  /**
   * @return the variant
   */
  public Mode getVariant() {
    return this.variant;
  }

  /**
   * @param variant the variant to set
   */
  public void setVariant(final Mode variant) {
    this.variant = variant;
  }

  /**
   * @return the dictsCached
   */
  public int getDictsCached() {
    return this.dictsCached;
  }

  /**
   * @param dictsCached the dictsCached to set
   */
  public void setDictsCached(final int dictsCached) {
    this.dictsCached = dictsCached;
  }

  /**
   * @param updates the updates to set
   */
  public void setUpdatesProcessed(final boolean updates) {
    this.updates = updates;
  }

  public boolean isReportConfirmed(final int processedItemCount) {
    return processedItemCount % this.reportLimit == 0;
  }

  public boolean isUpdateProcessed() {
    return this.updates;
  }

  public void setOmittNewDocs(boolean b) {
    this.newDocsOmitted = true;
  }

  public boolean isNewDocOmitted() {
    return this.newDocsOmitted;
  }

}