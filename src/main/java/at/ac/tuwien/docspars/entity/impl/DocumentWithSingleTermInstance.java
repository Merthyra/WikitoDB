package at.ac.tuwien.docspars.entity.impl;

import java.sql.Timestamp;
import java.util.HashSet;

public class DocumentWithSingleTermInstance extends Document {

  public DocumentWithSingleTermInstance(final int docId, final int rev, final String name, final Timestamp added, final int len) {
    super(docId, rev, name, added, len, new HashSet<Term>());
  }
}
