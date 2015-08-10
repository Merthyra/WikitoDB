package unittests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.map.MultiValueMap;
import org.junit.Test;

import at.ac.tuwien.docspars.entity.Batch;
import at.ac.tuwien.docspars.entity.Dict;
import at.ac.tuwien.docspars.entity.Document;
import at.ac.tuwien.docspars.entity.SimpleDict;
import at.ac.tuwien.docspars.entity.Term;

public class EntityTest {

	@Test
	public void testDocumentEquals() {
		//(int pageID, int revID, String title, Timestamp added, Timestamp removed, int length, boolean update)
		Document one = new Document(800, 400, "one", new Timestamp(100), new Timestamp(200), 100, false);
		Document two = new Document(800, 410, "two", new Timestamp(System.currentTimeMillis()), null, 100, false);
		Document three = new Document(810, 400, "three", new Timestamp(System.currentTimeMillis()),null, 100, false);
		Document four = new Document(800,400, "four", new Timestamp(150), null, 200, false);
		Document four_2 = new Document(800, 500, "four_update", new Timestamp(200), null, 30, true);
		
		assertThat (one, not( equalTo(two)));
		assertThat (one, not( equalTo(three)));
		assertThat (one, equalTo(four));	
		
		List<Document> docList = new ArrayList<Document>();
		docList.add(one);
		docList.add(two);
		docList.add(three);
		
		assertThat (docList, hasItem(four));

	}
	
	@Test
	public void testDocumentBehavior() {
		Dict dicta = new SimpleDict(1, "A");
		Dict dictb = new SimpleDict(2, "B");
		Dict dictc = new SimpleDict(3, "C");
		Dict dictd = new SimpleDict(4, "D");
		// copy of term with the same id as one
		Dict dicte = new SimpleDict(1, "E");
		
		Document doc1 = new Document(800, 400, "one", new Timestamp(100), new Timestamp(200), 100, false);
		Document doc2 = new Document(850, 410, "two", new Timestamp(System.currentTimeMillis()), null, 100, false);
				
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
		
		Document doc1 = new Document(800, 400, "one", new Timestamp(100), new Timestamp(200), 100, false);
		Document doc2 = new Document(850, 410, "two", new Timestamp(System.currentTimeMillis()), null, 100, false);
		
		Batch addBatch = new Batch();
		addBatch.addDocs(doc1);
		addBatch.addDocs(doc2);
				
		addBatch.addTerm(doc1, dicta, 1);
		addBatch.addTerm(doc1, dictb, 2);
		addBatch.addTerm(doc1, dicte, 3);
		
		addBatch.addTerm(doc2, dictc, 1);
		addBatch.addTerm(doc2, dictd, 2);
		addBatch.addTerm(doc2, dicte, 3);
		
		assertThat(addBatch.getNrOfNewDictEntries(), is(4));
		assertThat(addBatch.getBatchSize(), is(2));
		assertThat(addBatch.getNewVocab().get(2), is(dictc));
		assertThat(addBatch.getNrOfUniqueTerms(), is(4));
		assertThat(addBatch.getNrOfTerms(), is(6));
		assertThat(addBatch.getDocs().size(), is(2));
		
	}
	
	
	
	@Test
	public void testBatchTerm() {
		
		Batch batch = new Batch();
		
		
		
		
		
		
		
		
		
	}

	
	
	
	


}
