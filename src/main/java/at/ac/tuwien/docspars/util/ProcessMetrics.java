package at.ac.tuwien.docspars.util;

import at.ac.tuwien.docspars.entity.impl.Batch;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ProcessMetrics {

  private long numberOfNewDocuments;
  private long numberOfNewTerms;
  private long numberOfNewDictEntries;

  private long numberOfUpdateDocuments;
  private long numberOfUpdateTerms;
  private long numberOfUpdateDictEntries;

  private int skippedElemets;
  private int processedElements;

  private final long startTime;

  private int addBatch;
  private int updateBatch;


  public ProcessMetrics() {
    this.numberOfNewDictEntries = 0;
    this.numberOfNewTerms = 0;
    this.numberOfNewDocuments = 0;

    this.numberOfUpdateDictEntries = 0;
    this.numberOfUpdateDocuments = 0;
    this.numberOfUpdateTerms = 0;

    this.skippedElemets = 0;
    this.processedElements = 0;

    this.startTime = System.currentTimeMillis();
    this.addBatch = 0;
    this.updateBatch = 0;
  }

  public void addBatch(final Batch batch) {
    this.numberOfNewDocuments += batch.getSize();
    this.numberOfNewDictEntries += batch.getNewVocab().size();
    this.numberOfNewTerms += batch.getTerms().size();
    this.addBatch++;
  }

  public void updateBatch(final Batch batch) {
    this.numberOfUpdateDocuments += batch.getSize();
    this.numberOfUpdateDictEntries += batch.getNewVocab().size();
    this.numberOfUpdateTerms += batch.getTerms().size();
    this.updateBatch++;
  }

  private long getSumOfAllPersistedItems() {
    return this.numberOfNewDictEntries + this.numberOfNewDocuments + this.numberOfNewTerms + this.numberOfUpdateDictEntries + this.numberOfUpdateDocuments
        + this.numberOfUpdateTerms;
  }

  /**
   * Gets the processedElements for ProcessMetrics.
   *
   * @return processedElements
   */
  public int getProcessedElements() {
    return this.processedElements;
  }

  public int getTotalDocuments() {
    return this.processedElements + this.skippedElemets;
  }

  public void reportDocumentProcessed() {
    this.processedElements++;
  }

  public void skipDocument() {
    this.skippedElemets++;
  }

  @Override
  public String toString() {
    final long endTime = System.currentTimeMillis();
    final NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
    final DecimalFormat df = (DecimalFormat) nf;
    final String sep = System.getProperty("line.separator");
    df.applyPattern("###,###.###");
    return sep + "Process Metrics for Previous Run:" + sep + sep + "Batch-Adds:                   \t" + df.format(this.addBatch) + sep
        + "New Documents:	              \t" + df.format(this.numberOfNewDocuments) + sep + "New Dicts-Entries: 	          \t"
        + df.format(this.numberOfNewDictEntries) + sep + "New Term-Entries:             \t" + df.format(this.numberOfNewTerms) + sep
        + "New ALL Term-Entries: 	      \t" + df.format(this.numberOfNewDocuments == 0 ? 0 : this.numberOfNewTerms / this.numberOfNewDocuments) + sep
        + "avg-terms/batch:              \t" + df.format(this.addBatch == 0 ? 0 : this.numberOfNewTerms / this.addBatch) + sep
        + "avg-dicts/batch:              \t" + df.format(this.addBatch == 0 ? 0 : this.numberOfNewDictEntries / this.addBatch) + sep
        + "--------------------------------------------" + sep + "Batch-Updates:                \t" + df.format(this.updateBatch) + sep
        + "Updated New Documents:        \t" + df.format(this.numberOfUpdateDocuments) + sep + "Updated New Dicts-Entries:    \t"
        + df.format(this.numberOfUpdateDictEntries) + sep + "Updated New Term-Entries:     \t" + df.format(this.numberOfUpdateTerms) + sep
        + "Updated ALL New Term-Entries: \t" + df.format(this.numberOfUpdateDocuments == 0 ? 0 : this.numberOfUpdateTerms / this.numberOfUpdateDocuments) + sep
        + "avg-terms/batch:              \t" + df.format(this.updateBatch == 0 ? 0 : this.numberOfUpdateTerms / this.updateBatch) + sep
        + "avg-dicts/batch:              \t" + df.format(this.updateBatch == 0 ? 0 : this.numberOfUpdateDictEntries / this.updateBatch) + sep
        + "--------------------------------------------" + sep + "skipped documents:            \t" + df.format(this.skippedElemets) + sep
        + "total number of processed documents: " + df.format(this.processedElements) + sep + "total processed elements      \t" + sep
        + "++++++++++++++++++++++++++++++++++++++++++++" + sep + " -------------------- in ->   \t" + (((double) endTime - this.startTime) / 1000) + " seconds";
  }

  /**
   * @return a intermediate short String representation of the current process metrics
   */
  public String getIntermediateReport() {
    return String.format("Total docs: %s, skipped: %s, batches: %s, time elapsed: %s, avg(item-persist-ratio)[items/min]: %s", this.processedElements,
        this.skippedElemets, (this.addBatch + this.updateBatch), DateFormatUtils.format((System.currentTimeMillis() - this.startTime), "HHMMSS:sss"),
        getSumOfAllPersistedItems() / 60000 / (System.currentTimeMillis() - this.startTime));
  }


}
