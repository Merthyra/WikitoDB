package at.ac.tuwien.docspars.entity.factories.impl;

import at.ac.tuwien.docspars.entity.factories.DocumentCreationable;
import at.ac.tuwien.docspars.entity.impl.Document;

import java.sql.Timestamp;

public class SimpleDocumentFactory implements DocumentCreationable {

  @Override
  public Document createDocument(int documentId, int revisionId, String name, Timestamp added,
      int len) {
    return new Document(documentId, name, added, len);
  }

}
