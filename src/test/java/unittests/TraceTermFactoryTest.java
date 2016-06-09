package unittests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.factories.TermCreationable;
import at.ac.tuwien.docspars.entity.factories.impl.TraceTermFactory;
import at.ac.tuwien.docspars.entity.impl.Dict;
import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.entity.impl.Term;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TraceTermFactoryTest {

  private List<Document> documents;
  private List<Dictionable> dicts;
  private TermCreationable termFactory;

  @Before
  public void setup() {
    dicts = createDicts(10);
    documents = createDocs(10);
    this.termFactory = new TraceTermFactory();
  }

  @Test
  public void testTermInstanceIsSameForTwoDictsFromSameDocument() {
    Term term1 = termFactory.createTerm(documents.get(0), new Dict(1, "Dict1"), 1);
    Term term2 = termFactory.createTerm(documents.get(0), new Dict(1, "Dict1"), 10);
    assertThat(term1, is(term2));
    assertThat(term1.getTrace(), is(2));
  }

  @Test
  public void testSameInstanceIsReturnedForDifferntTermOfSameDocument() {
    Term term1 = termFactory.createTerm(documents.get(0), dicts.get(0), 1);
    Term term2 = termFactory.createTerm(documents.get(0), dicts.get(1), 2);
    assertThat(term1.getTrace(), is(1));
    assertThat(term2.getTrace(), is(1));
    assertThat(term1, is(not(term2)));
  }

  @Test
  public void testSameDictButDifferntDocumentCreatesDifferentTerms() {
    Term term1 = termFactory.createTerm(documents.get(0), dicts.get(0), 1);
    Term term2 = termFactory.createTerm(documents.get(1), dicts.get(0), 2);
    termFactory.createTerm(documents.get(2), dicts.get(0), 3);
    assertTrue(term1.getTrace() == 1);
    assertTrue(term2.getTrace() == 1);
    assertTrue(term1 != term2);
  }

  private List<Dictionable> createDicts(int num) {
    return IntStream.range(1, num).mapToObj(i -> new Dict(i, "Dict" + i)).collect(Collectors.toList());
  }

  private List<Document> createDocs(int num) {
    return IntStream.range(1, num).mapToObj(i -> new Document(i, 1, "Dict" + i, new Timestamp(100000000000L * i), 100))
        .collect(Collectors.toList());
  }

}
