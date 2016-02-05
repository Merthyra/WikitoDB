package unittests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import at.ac.tuwien.docspars.entity.Batch;
import at.ac.tuwien.docspars.entity.Dict;
import at.ac.tuwien.docspars.entity.TimestampedDocument;
import at.ac.tuwien.docspars.entity.SimpleDict;
import at.ac.tuwien.docspars.entity.Term;

public class EntityTest {

	@Test
	public void testDocumentEquals() {
		//(int pageID, int revID, String title, Timestamp added, Timestamp removed, int length, boolean update)
		TimestampedDocument one = new TimestampedDocument(800, 1, "one", new Timestamp(100), 100);
		TimestampedDocument two = new TimestampedDocument(800, 1, "two", new Timestamp(System.currentTimeMillis()), 100);
		TimestampedDocument three = new TimestampedDocument(810, 2, "three", new Timestamp(System.currentTimeMillis()),100);
		TimestampedDocument four = new TimestampedDocument(800, 3, "four", new Timestamp(150), 200);
		TimestampedDocument four_2 = new TimestampedDocument(800,4, "four_update", new Timestamp(200),30);
		
		assertThat (one, is (equalTo(two)));
		assertThat (one, not( equalTo(three)));
		assertThat (one, not( equalTo(four)));	
		
		List<TimestampedDocument> docList = new ArrayList<TimestampedDocument>();
		docList.add(one);
		docList.add(two);
		docList.add(three);
		
		assertThat (docList, hasItem(three));
		assertThat (docList, not(hasItem(four)));
	}
	
	@Test
	public void testDocumentBehavior() {
		Dict dicta = new SimpleDict(1, "A");
		Dict dictb = new SimpleDict(2, "B");
		Dict dictc = new SimpleDict(3, "C");
		Dict dictd = new SimpleDict(4, "D");
		// copy of term with the same id as one
		Dict dicte = new SimpleDict(1, "E");
		
		TimestampedDocument doc1 = new TimestampedDocument(800, 1,"one", new Timestamp(100), 100);
		TimestampedDocument doc2 = new TimestampedDocument(850, 2,"two", new Timestamp(System.currentTimeMillis()), 100);
				
		Term term1 = doc1.addTerm(dicta, 1);
		Term term2 = doc1.addTerm(dictb, 2);
		Term term3 = doc1.addTerm(dicte, 3);
		
		// term1 and term3 must be the same because the dict shares the same tid
		assertThat(term1, is(term3));
		assertThat(doc1.getTerms().size(), is(2));
		assertThat(term1.getLastPosition(), is(3));	
	}
	
	@Test
	public void testaddBatch() {
		Dict dicta = new SimpleDict(1, "A");
		Dict dictb = new SimpleDict(2, "B");
		Dict dictc = new SimpleDict(3, "C");
		Dict dictd = new SimpleDict(4, "D");
		// copy of term with the same id as one
		Dict dicte = new SimpleDict(1, "E");
		
		TimestampedDocument doc1 = new TimestampedDocument(800, 1, "one", new Timestamp(100), 100);
		TimestampedDocument doc2 = new TimestampedDocument(850, 1, "two", new Timestamp(System.currentTimeMillis()), 100);
		
		Batch addBatch = new Batch();
		addBatch.addDocs(doc1);
		addBatch.addDocs(doc2);			
		addBatch.addTerm(doc1, dicta, 1);
		addBatch.addTerm(doc1, dictb, 2);
		addBatch.addTerm(doc1, dicte, 3);
		addBatch.addTerm(doc2, dictc, 1);
		addBatch.addTerm(doc2, dictd, 2);
		addBatch.addTerm(doc2, dicte, 3);
			
		assertThat(addBatch.getNrOfNewDictEntries(), is(0));
		assertThat(addBatch.getBatchSize(), is(2));
		addBatch.addNewVocab(dicta);
		addBatch.addNewVocab(dictc);
		
		assertThat(addBatch.getNewVocab().get(1), is(dictc));
		assertThat(addBatch.getNrOfUniqueTerms(), is(5));
		assertThat(addBatch.getNrOfTerms(), is(6));
		assertThat(addBatch.getDocs().size(), is(2));	
	}
		
	@Test
	public void testDocumentAddition() {
		Dict dicta = new SimpleDict(1, "A");
		Dict dictb = new SimpleDict(2, "B");
		Dict dictc = new SimpleDict(1, "A");
		Dict dictd = new SimpleDict(4, "D");	
		TimestampedDocument doc1 = new TimestampedDocument(800, 1, "one", new Timestamp(100), 100);
		TimestampedDocument doc2 = new TimestampedDocument(850, 1, "two", new Timestamp(System.currentTimeMillis()), 100);

		Batch batch = new Batch();
		
		batch.addDocs(doc1);
		batch.addDocs(doc2);
		batch.addDocs(doc1);
		assertThat(batch.getDocs().size(), is(3));		
	}

	@Test
	public void testCountItemList() {
		Dict dicta = new SimpleDict(1, "A");
		Dict dictb = new SimpleDict(2, "B");
		Dict dictc = new SimpleDict(1, "A");
		Dict dictd = new SimpleDict(4, "D");
		
		TimestampedDocument doc1 = new TimestampedDocument(800, 1, "one", new Timestamp(100), 100);
		TimestampedDocument doc2 = new TimestampedDocument(850, 1, "two", new Timestamp(System.currentTimeMillis()), 100);
	
		Batch batch = new Batch();
		batch.addDocs(doc1);
		batch.addDocs(doc2);
		
		assertThat(batch.getDocs().size(), is(2));
	
		batch.addTerm(doc1, dicta, 1);
		batch.addTerm(doc1, dictb, 1);
		batch.addTerm(doc1, dictc, 1);
		batch.addTerm(doc2, dicta, 1);
		batch.addTerm(doc2, dicta, 1);
		batch.addTerm(doc2, dictc, 1);
		batch.addTerm(doc2, dictd, 1);
		
		assertThat(batch.getNrOfTerms(), is(7));
		assertThat(batch.getNrOfUniqueTerms(), is(4));
		assertThat(batch.getUniqueElements().get(1), is(new Term(doc1, dictb)));
		assertThat(batch.getUniqueElements().get(2), is(new Term(doc2, dictc)));
		assertThat(batch.getUniqueElements().get(3), is(new Term(doc2, dictd)));
		assertThat(batch.getUniqueElements().getElementCount(new Term(doc2, dicta)), is(2));
	}

}
