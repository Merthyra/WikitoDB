package at.ac.tuwien.docspars.entity.factories;

import at.ac.tuwien.docspars.entity.impl.Document;

import java.sql.Timestamp;

public interface DocumentCreationable {

  public Document createDocument(int documentId, int revisionId, String name, Timestamp added,
      int len);

}
