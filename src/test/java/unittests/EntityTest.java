package unittests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import at.ac.tuwien.docspars.entity.impl.Batch;
import at.ac.tuwien.docspars.entity.impl.BatchMode;
import at.ac.tuwien.docspars.entity.impl.Dict;
import at.ac.tuwien.docspars.entity.impl.Document;
import org.junit.Test;

import at.ac.tuwien.docspars.entity.impl.Batch;
import at.ac.tuwien.docspars.entity.impl.BatchMode;
import at.ac.tuwien.docspars.entity.impl.Dict;
import at.ac.tuwien.docspars.entity.impl.Document;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class EntityTest {

  @Test
  public void testDocumentEquals() {
    // (int pageID, int revID, String title, Timestamp added, Timestamp removed, int length, boolean
    // update)
    final Document one = new Document(800, 1, "one", new Timestamp(100), 100, false);
    final Document two = new Document(800, 1, "two", new Timestamp(System.currentTimeMillis()), 100, false);
    final Document three = new Document(810, 2, "three", new Timestamp(System.currentTimeMillis()), 100, false);
    final Document four = new Document(800, 3, "four", new Timestamp(150), 200, false);
    final Document four_2 = new Document(800, 4, "four_update", new Timestamp(200), 30, false);

    assertThat(one, is(equalTo(two)));
    assertThat(one, not(equalTo(three)));
    assertThat(one, not(equalTo(four)));

    final List<Document> docList = new ArrayList<Document>();
    docList.add(one);
    docList.add(two);
    docList.add(three);

    assertThat(docList, hasItem(three));
    assertThat(docList, not(hasItem(four)));
  }

  // @Test
  // public void testDocumentBehavior() {
  // final Dict dicta = new Dict(1, "A");
  // final Dict dictb = new Dict(2, "B");
  // final Dict dictc = new Dict(3, "C");
  // final Dict dictd = new Dict(4, "D");
  // // copy of term with the same id as one
  // final Dict dicte = new Dict(1, "E");
  //
  // final Document doc1 = new Document(800, 1, "one", new Timestamp(100), 100);
  // final Document doc2 = new Document(850, 2, "two", new Timestamp(System.currentTimeMillis()),
  // 100);
  // List<Term> terms = new ArrayList<Term>
  // final Term term1 = doc1.addTerm(dicta, 1);
  // final Term term2 = doc1.addTerm(dictb, 2);
  // final Term term3 = doc1.addTerm(dicte, 3);
  //
  // // term1 and term3 must be the same because the dict shares the same tid
  // assertThat(term1, is(term3));
  // assertThat(doc1.getTerms().size(), is(2));
  // assertThat(term1.getLastPosition(), is(3));
  // }

  // @Test
  // public void testaddBatch() {
  // final Dict dicta = new Dict(1, "A");
  // final Dict dictb = new Dict(2, "B");
  // final Dict dictc = new Dict(3, "C");
  // final Dict dictd = new Dict(4, "D");
  // // copy of term with the same id as one
  // final Dict dicte = new Dict(1, "E");
  //
  // final Document doc1 = new Document(800, 1, "one", new Timestamp(100), 100);
  // final Document doc2 = new Document(850, 1, "two", new Timestamp(System.currentTimeMillis()),
  // 100);
  //
  // final Batch addBatch = new Batch(BatchMode.ADD);
  // addBatch.addDocs(doc1);
  // addBatch.addDocs(doc2);
  // final TermCreationable termfactory = new TermFactory();
  // addBatch.addTerm(termfactory.createTerm(doc1, dicta, 1));
  // addBatch.addTerm(termfactory.createTerm(doc1, dictb, 2);
  // addBatch.addTerm(termfactory.createTerm(doc1, dicte, 3);
  // addBatch.addTerm(termfactory.createTerm(doc2, dictc, 1);
  // addBatch.addTerm(termfactory.createTerm(doc2, dictd, 2);
  // addBatch.addTerm(termfactory.createTerm(doc2, dicte, 3);
  //
  // assertThat(addBatch.getNrOfNewDictEntries(), is(0));
  // assertThat(addBatch.getSize(), is(2));
  // addBatch.addNewVocab(dicta);
  // addBatch.addNewVocab(dictc);
  //
  // assertThat(addBatch.getNewVocab().get(1), is(dictc));
  // assertThat(addBatch.getNrOfUniqueTerms(), is(5));
  // assertThat(addBatch.getNrOfTerms(), is(6));
  // assertThat(addBatch.getDocs().size(), is(2));
  // }

  @Test
  public void testDocumentAddition() {
    final Dict dicta = new Dict(1, "A");
    final Dict dictb = new Dict(2, "B");
    final Dict dictc = new Dict(1, "A");
    final Dict dictd = new Dict(4, "D");
    final Document doc1 = new Document(800, 1, "one", new Timestamp(100), 100, false);
    final Document doc2 = new Document(850, 1, "two", new Timestamp(System.currentTimeMillis()), 100, false);

    final Batch batch = new Batch(BatchMode.ADD);

    batch.addDocs(doc1);
    batch.addDocs(doc2);
    batch.addDocs(doc1);
    assertThat(batch.getDocs().size(), is(3));
  }

}
