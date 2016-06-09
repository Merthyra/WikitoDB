package at.ac.tuwien.docspars.entity.factories.impl;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.Documentable;
import at.ac.tuwien.docspars.entity.impl.FrequencyTraceableTerm;

import java.util.HashMap;
import java.util.Map;

public class TraceTermFactory extends TermFactory {
  // maps tid to documentids, whenever the combination is present the Term is present
  // holds a references of terms, guarantees unique instances of term objects / document
  // private final Map<Integer, Map<Integer, FrequencyTraceableTerm>> termMap;
  private final Map<Integer, FrequencyTraceableTerm> tidToTermMap;
  private Documentable lastDocument = null;

  public TraceTermFactory() {
    super();
    this.tidToTermMap = new HashMap<>();
  }

  @Override
  public FrequencyTraceableTerm createTerm(final Documentable doc, final Dictionable dict, final int pos) {
    updateDocumentRelationIfDocumentHasChanged(doc);
    FrequencyTraceableTerm term =
        tidToTermMap.merge(dict.getTId(), new FrequencyTraceableTerm(doc, dict, pos), (t1, t2) -> t1.incrementedTrace());
    return term;
  }


  private void updateDocumentRelationIfDocumentHasChanged(final Documentable doc) {
    if (!(lastDocument == doc) || lastDocument == null) {
      lastDocument = doc;
      tidToTermMap.clear();
    }
  }


  // /**
  // * returns a unique term object for each distinct term in a document amending the count
  // */
  // @Override
  // public FrequencyTraceableTerm createTerm(final Documentable doc, final Dictionable dict,
  // final int pos) {
  // FrequencyTraceableTerm term = null;
  // // if the termid is unknown create a new termid for a new document and termid
  // if (this.termMap.get(dict.getTId()) == null) {
  // final Map<Integer, FrequencyTraceableTerm> docTerms = new HashMap<>();
  // docTerms.put(doc.getDId(), term = new FrequencyTraceableTerm(doc, dict, pos));
  // this.termMap.put(dict.getTId(), docTerms);
  // }
  // // if the term has appeared before but not for the document, create a new document key and add
  // // the new term
  // else if (this.termMap.get(dict.getTId()).get(doc.getDId()) == null) {
  // this.termMap.get(dict.getTId()).put(doc.getDId(),
  // term = new FrequencyTraceableTerm(doc, dict, pos));
  // }
  // // if no new object has been created
  // if (term == null) {
  // // if term exists, return the stored object and update the frequency count
  // term = this.termMap.get(dict.getTId()).get(doc.getDId());
  // // increment object's frequency value
  // term.incrementTrace();
  // }
  // return term;
  // }

  public void reset() {
    this.tidToTermMap.clear();
    this.tidToTermMap.clear();
  }
}
