package at.ac.tuwien.docspars.io.daos;

import at.ac.tuwien.docspars.entity.TimestampedDocument;
import at.ac.tuwien.docspars.entity.Timestampable;
import gnu.trove.set.TIntSet;

public interface DocDAO extends CrudOperations<TimestampedDocument, TIntSet>, Timestampable {

}
