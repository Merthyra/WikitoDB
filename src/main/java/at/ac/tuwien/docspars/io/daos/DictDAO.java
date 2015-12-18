package at.ac.tuwien.docspars.io.daos;

import gnu.trove.map.TObjectIntMap;
import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.util.ASCIIString2ByteArrayWrapper;

public interface DictDAO extends CrudOperations<Dictionable, TObjectIntMap<ASCIIString2ByteArrayWrapper>> {


}
