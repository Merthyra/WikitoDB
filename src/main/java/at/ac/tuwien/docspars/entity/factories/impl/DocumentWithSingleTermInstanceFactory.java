package at.ac.tuwien.docspars.entity.factories.impl;

import at.ac.tuwien.docspars.entity.factories.DocumentCreationable;
import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.entity.impl.DocumentWithSingleTermInstance;

import java.sql.Timestamp;

public class DocumentWithSingleTermInstanceFactory implements DocumentCreationable {

  @Override
  public Document createDocument(final int documentId, final int revisionId, final String name, final Timestamp added, final int len) {
    return new DocumentWithSingleTermInstance(documentId, revisionId, name, added, len);
  }
}