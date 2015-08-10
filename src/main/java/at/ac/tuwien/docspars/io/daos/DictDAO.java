package at.ac.tuwien.docspars.io.daos;

import java.util.List;
import java.util.Map;

import at.ac.tuwien.docspars.entity.Batch;
import at.ac.tuwien.docspars.entity.Dict;

public interface DictDAO extends CrudOperations<List<Dict>, Map<String,Integer>> {


}
