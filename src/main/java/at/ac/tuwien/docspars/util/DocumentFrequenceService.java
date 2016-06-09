package at.ac.tuwien.docspars.util;

import at.ac.tuwien.docspars.entity.impl.DfValue;

import java.util.HashMap;
import java.util.Optional;

public class DocumentFrequenceService {

  private final HashMap<Integer, DfValue> documentSetForTermId = new HashMap<>();

  public Optional<DfValue> getValueForTerm(int tid) {
    return Optional.ofNullable(documentSetForTermId.get(tid));
  }

  public DfValue registerDocumentForTerm(int tid, int did) {
    return documentSetForTermId.merge(tid, new DfValue(did), (v1, v2) -> {
      v1.registerDocument(did);
      return v1;
    });
  }

  public DfValue unRegisterDocumentForTerm(int tid, int did) {
    return documentSetForTermId.merge(tid, null, (v1, v2) -> {
      v1.unRegisterDocument(did);
      return v1;
    });
  }

  void reset() {
    this.documentSetForTermId.clear();
  }

}
