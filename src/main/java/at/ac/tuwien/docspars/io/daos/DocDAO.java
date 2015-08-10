package at.ac.tuwien.docspars.io.daos;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.map.MultiValueMap;

import at.ac.tuwien.docspars.entity.Batch;
import at.ac.tuwien.docspars.entity.Dict;
import at.ac.tuwien.docspars.entity.Document;

public interface DocDAO extends CrudOperations<List<Document>, Set<Integer>>{


}
