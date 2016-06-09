package at.ac.tuwien.docspars.entity.impl;

import java.util.HashSet;
import java.util.Set;

public class DfValue {

  private final Set<Integer> containedDocuments = new HashSet<>();

  public DfValue() {}

  public DfValue(int did) {
    containedDocuments.add(did);
  }

  public int getValue() {
    return this.containedDocuments.size();
  }

  public DfValue registerDocument(int docId) {
    this.containedDocuments.add(docId);
    return this;
  }

  public DfValue unRegisterDocument(int docId) {
    this.containedDocuments.remove(docId);
    return this;
  }
}

