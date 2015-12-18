package at.ac.tuwien.docspars.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import at.ac.tuwien.docspars.entity.Batch;

public class ProcessMetrics {

	private long numberOfNewDocuments;
	private long numberOfNewTerms;
	private long numberOfAllNewTerms;
	private long numberOfNewDictEntries;
	
	private long numberOfUpdateDocuments;
	private long numberOfUpdateTerms;
	private long numberOfUpdateDictEntries;
	private long numberOfAllUpdateTerms;
	
	private long skippedElemets;
	
	private long startTime;
	private int addBatch;
	private int updateBatch;

	public ProcessMetrics() {
		this.numberOfNewDictEntries=0;
		this.numberOfNewTerms=0;
		this.numberOfAllNewTerms = 0;
		this.numberOfNewDocuments=0;
		
		this.numberOfUpdateDictEntries = 0;
		this.numberOfUpdateDocuments = 0;
		this.numberOfUpdateTerms = 0;
		this.numberOfAllUpdateTerms = 0;
		
		this.skippedElemets = 0;
		
		this.startTime = System.currentTimeMillis();
		this.addBatch = 0;
		this.updateBatch = 0;
	}

	public void addUpdateBatch(Batch batch) {
		this.numberOfUpdateDocuments+=batch.getBatchSize();
		this.numberOfUpdateDictEntries+=batch.getNrOfNewDictEntries();
		this.numberOfUpdateTerms+=batch.getNrOfUniqueTerms();
		this.numberOfAllUpdateTerms+= batch.getNrOfTerms();
		this.updateBatch++;
	}

	public void addNewBatch(Batch batch) {
		this.numberOfNewDocuments+=batch.getBatchSize();
		this.numberOfNewDictEntries+=batch.getNrOfNewDictEntries();
		this.numberOfNewTerms+=batch.getNrOfUniqueTerms();
		this.numberOfAllNewTerms+= batch.getNrOfTerms();
		this.addBatch++;
	}
	
	public void skipElement() {
		this.skippedElemets++;
	}

	@Override
	public String toString() {
		long endTime = System.currentTimeMillis();
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
		DecimalFormat df = (DecimalFormat)nf;
		String sep = System.getProperty("line.separator");
		df.applyPattern("###,###.###");
		return "\nProcess Metrics for Previous Run:" + sep + sep +
				"Batch-Adds:                   \t" + df.format(this.addBatch) + sep + 
				"New Documents:	               \t" + df.format(this.numberOfNewDocuments) + sep + 
				"New Dicts-Entries: 	       \t" + df.format(this.numberOfNewDictEntries) + sep + 
				"New Term-Entries:             \t" + df.format(this.numberOfNewTerms) + sep + 
				"New ALL Term-Entries: 	       \t" + df.format(this.numberOfAllNewTerms) + sep + 
				"avg-unique-terms/document:    \t" + df.format(this.numberOfNewDocuments == 0 ? 0 : this.numberOfNewTerms/this.numberOfNewDocuments) + sep +
				"avg-terms/batch:              \t" + df.format(this.addBatch == 0 ? 0 : this.numberOfNewTerms/this.addBatch) + sep +
				"avg-dicts/batch:              \t" + df.format(this.addBatch == 0 ? 0 : this.numberOfNewDictEntries/this.addBatch) + sep + 		
				"--------------------------------------------" + sep + 
				"Batch-Updates:                \t" + df.format(this.updateBatch) + sep + 
				"Updated New Documents:        \t" + df.format(this.numberOfUpdateDocuments) + sep + 
				"Updated New Dicts-Entries:    \t" + df.format(this.numberOfUpdateDictEntries) + sep + 
				"Updated New Term-Entries:     \t" + df.format(this.numberOfUpdateTerms) + sep + 
				"Updated ALL New Term-Entries: \t" + df.format(this.numberOfAllUpdateTerms) + sep + 
				"avg-unique-terms/document:    \t" + df.format(this.numberOfUpdateDocuments == 0 ? 0 : this.numberOfUpdateTerms/this.numberOfUpdateDocuments) + sep +
				"avg-terms/batch:              \t" + df.format(this.updateBatch == 0 ? 0 : this.numberOfUpdateTerms/this.updateBatch) + sep +
				"avg-dicts/batch:              \t" + df.format(this.updateBatch == 0 ? 0 : this.numberOfUpdateDictEntries/this.updateBatch) + sep + 
				"--------------------------------------------" + sep +
				"skipped documents:            \t" + df.format(this.skippedElemets) + sep + 
				"++++++++++++++++++++++++++++++++++++++++++++" + sep +
 				" -------------------- in ->   \t" + (((double)endTime-this.startTime)/1000) + " seconds";
	}
}
