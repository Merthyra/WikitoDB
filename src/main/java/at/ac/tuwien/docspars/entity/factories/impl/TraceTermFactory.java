package at.ac.tuwien.docspars.entity.factories.impl;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.Documentable;
import at.ac.tuwien.docspars.entity.impl.FrequencyTraceableTerm;

import java.util.HashMap;
import java.util.Map;

public class TraceTermFactory extends TermFactory {

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

  public void reset() {
    this.tidToTermMap.clear();
    this.tidToTermMap.clear();
  }
}
