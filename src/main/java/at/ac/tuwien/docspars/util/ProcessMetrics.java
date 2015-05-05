package at.ac.tuwien.docspars.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ProcessMetrics {

	private long numberOfDocuments;
	private long numberOfTerms;
	private long numberOfDictEntries;
	private long startTime;
	private int batchupdates;

	public ProcessMetrics() {
		this.numberOfDictEntries=0;
		this.numberOfTerms=0;
		this.numberOfDocuments=0;
		this.startTime = System.currentTimeMillis();
		this.batchupdates = 0;
	}

	/**
	 * @return the numberOfDocuments
	 */
	public long getNumberOfDocuments() {
		return numberOfDocuments;
	}

	/**
	 * @param numberOfDocuments the numberOfDocuments to set
	 */
	public void addNumberOfDocuments(long numberOfDocuments) {
		this.numberOfDocuments+=numberOfDocuments;
	}

	/**
	 * @return the numberOfTerms
	 */
	public long getNumberOfTerms() {
		return numberOfTerms;
	}

	/**
	 * @param numberOfTerms the numberOfTerms to set
	 */
	public void addNumberOfTerms(long numberOfTerms) {
		this.numberOfTerms+=numberOfTerms;
	}

	/**
	 * @return the numberOfDictEntries
	 */
	public long getNumberOfDictEntries() {
		return numberOfDictEntries;
	}

	/**
	 * @param numberOfDictEntries the numberOfDictEntries to set
	 */
	public void addNumberOfDictEntries(long numberOfDictEntries) {
		this.numberOfDictEntries+=numberOfDictEntries;
	}
	
	public void updateBatch() {
		this.batchupdates++;
	}
	
	public int getBatchSize() {
		return this.batchupdates++;
	}


	@Override
	public String toString() {
		long endTime = System.currentTimeMillis();
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
		DecimalFormat df = (DecimalFormat)nf;
		df.applyPattern("###,###.###");
		if (this.batchupdates == 0) this.batchupdates = 1;
		return "\nProcess Metrics for Previous Run:\n" + 
				"Documents: " + df.format(this.numberOfDocuments) + " \n" + 
				"Dicts-Entries: " + df.format(this.numberOfDictEntries) + " \n" + 
				"Term-Entries: " + df.format(this.numberOfTerms) + " \n" + 
				"in " + (((double)endTime-this.startTime)/1000) + " seconds\n" + 
				"Batch-Updates: " + df.format(this.batchupdates) + " \n" + 
				"avg-terms/batch: " + df.format(this.numberOfTerms/this.batchupdates) + "\n" +
				"avg-dicts/batch: " + df.format(this.numberOfDictEntries/this.batchupdates);
	}

}
