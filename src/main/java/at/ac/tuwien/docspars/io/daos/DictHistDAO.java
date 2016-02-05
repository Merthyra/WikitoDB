package at.ac.tuwien.docspars.io.daos;

import java.util.Map;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.Timestampable;

public interface DictHistDAO extends CrudOperations<Dictionable, Map<String, Integer>>, Timestampable {

}
