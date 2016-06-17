package at.ac.tuwien.docspars.util;

import at.ac.tuwien.docspars.entity.impl.Batch;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
  private Map<String, Duration> operationTimes = new HashMap<>();

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
    return this.numberOfNewDictEntries + this.numberOfNewDocuments + this.numberOfNewTerms + this.numberOfUpdateDictEntries
        + this.numberOfUpdateDocuments + this.numberOfUpdateTerms;
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

  public void addOperationTime(String target, Duration duration) {
    this.operationTimes.merge(target, duration, Duration::plus);
  }

  @Override
  public String toString() {
    final long endTime = System.currentTimeMillis();
    final NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
    final DecimalFormat df = (DecimalFormat) nf;
    final String newLine = System.getProperty("line.separator");
    df.applyPattern("###,###.###");
    String horizontalLine = IntStream.rangeClosed(1, 50).mapToObj(i -> "-").collect(Collectors.joining("*")).toString();
    String horizontalCrosses = IntStream.rangeClosed(1, 50).mapToObj(i -> "+").collect(Collectors.joining("*")).toString();
    // @formatter:off
    return newLine+newLine + "Process Metrics for Previous Run:" + newLine
        + horizontalLine + newLine
        + "Batch-Adds:                   \t" + df.format(this.addBatch) + newLine
        + "New Documents:                \t" + df.format(this.numberOfNewDocuments) + newLine
        + "New Dicts-Entries:            \t" + df.format(this.numberOfNewDictEntries) + newLine
        + "New Term-Entries:             \t" + df.format(this.numberOfNewTerms) + newLine
        + "New ALL Term-Entries: 	     \t" + df.format(this.numberOfNewDocuments == 0 ? 0 : this.numberOfNewTerms / this.numberOfNewDocuments) + newLine
        + "avg-terms/batch:              \t" + df.format(this.addBatch == 0 ? 0 : this.numberOfNewTerms / this.addBatch) + newLine
        + "avg-dicts/batch:              \t" + df.format(this.addBatch == 0 ? 0 : this.numberOfNewDictEntries / this.addBatch) + newLine
        + horizontalLine + newLine
        + "Batch-Updates:                \t" + df.format(this.updateBatch) + newLine
        + "Updated New Documents:        \t" + df.format(this.numberOfUpdateDocuments) + newLine
        + "Updated New Dicts-Entries:    \t" + df.format(this.numberOfUpdateDictEntries) + newLine + "Updated New Term-Entries:     \t" + df.format(this.numberOfUpdateTerms) + newLine
        + "Updated ALL New Term-Entries: \t" + df.format(this.numberOfUpdateDocuments == 0 ? 0 : this.numberOfUpdateTerms / this.numberOfUpdateDocuments) + newLine
        + "avg-terms/batch:              \t" + df.format(this.updateBatch == 0 ? 0 : this.numberOfUpdateTerms / this.updateBatch) + newLine
        + "avg-dicts/batch:              \t" + df.format(this.updateBatch == 0 ? 0 : this.numberOfUpdateDictEntries / this.updateBatch) + newLine
        + horizontalLine + newLine
        + "skipped documents:            \t" + df.format(this.skippedElemets) + newLine
        + "total number of processed documents: " + df.format(this.processedElements) + newLine
        + "total processed elements      \t" + newLine + newLine
        + "total duration of intercepted operations [s]: " + newLine
        + horizontalCrosses + newLine
        + getListOfInterceptedOperations() + "\t" + newLine
        + horizontalCrosses + newLine
        + " in ->   \t" + DurationFormatUtils.formatDuration((endTime-startTime) , "HH:mm:ss.SSS");
        // @formatter:on
  }

  private Duration elapsedTime() {
    return Duration.ofNanos(System.nanoTime() - startTime);
  }

  private String getListOfInterceptedOperations() {
    return operationTimes.entrySet().stream()
        .map(es -> es.getKey() + "::" + formatDurationHHMMSSsss(es.getValue())).sorted()
        .collect(Collectors.joining("\n"));
  }

  /**
   * @return a intermediate short String representation of the current process metrics
   */
  public String getIntermediateReport() {
    final Duration elapsed = elapsedTime();
    return String.format("Total docs: %s, skipped: %s, batches: %s, time elapsed: %s, avg(item-persist-ratio)[items/min]: %s",
        this.processedElements, this.skippedElemets, (this.addBatch + this.updateBatch),
        formatDurationHHMMSSsss(elapsed),
        (getSumOfAllPersistedItems() / (elapsed.getSeconds() / 60)));
  }

  public static String formatDurationHHMMSSsss(Duration duration) {
    // final long hours = duration.toHours();
    // final long minutes = duration.minusHours(hours).toMinutes();
    // final long seconds = duration.minusHours(hours).minusMinutes(minutes).getSeconds();
    // final long millis = duration.minusHours(hours).minusMinutes(minutes).minusSeconds(seconds).toMillis();
    // return String.format("%02d:%02d:%02d.%03d hh:mm:SS.sss", hours, minutes, seconds, millis);
    return DurationFormatUtils.formatDuration(duration.toMillis(), "HH:mm:ss.SSS");
  }
}
