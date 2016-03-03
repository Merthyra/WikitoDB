package at.ac.tuwien.docspars.entity.factories.impl;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.Documentable;
import at.ac.tuwien.docspars.entity.impl.RevDocument;
import at.ac.tuwien.docspars.entity.impl.TrceRevTerm;

import java.util.HashMap;
import java.util.Map;

public class TraceTermFactory extends RevTermFactory {
  // maps tid to documentids, whenever the combination is present the Term is present
  // holds a references of terms, guarantees unique instances of term objects / document
  private final Map<Integer, Map<Integer, TrceRevTerm>> termMap;

  public TraceTermFactory() {
    super();
    this.termMap = new HashMap<>();
  }

  /**
   * returns a unique term object for each distinct term in a document amending the count
   */
  @Override
  public TrceRevTerm createTerm(final Documentable doc, final Dictionable dict, final int pos) {
    TrceRevTerm term = null;
    // if the termid is unknown create a new termid for a new document and termid
    if (this.termMap.get(dict.getTId()) == null) {
      final Map<Integer, TrceRevTerm> docTerms = new HashMap<>();
      docTerms.put(doc.getDId(), term = new TrceRevTerm((RevDocument) doc, dict, pos, 1));
      this.termMap.put(dict.getTId(), docTerms);
    }
    // if the term has appeared before but not for the document, create a new document key and add
    // the new term
    else if (this.termMap.get(dict.getTId()).get(doc.getDId()) == null) {
      this.termMap.get(dict.getTId()).put(doc.getDId(),
          term = new TrceRevTerm((RevDocument) doc, dict, pos, 1));
    }
    // if no new object has been created
    if (term == null) {
      // if term exists, return the stored object and update the frequency count
      term = this.termMap.get(dict.getTId()).get(doc.getDId());
      // increment object's frequency value
      term.setFrequency(term.getFrequency() + 1);
    }
    return term;
  }
}
