package at.ac.tuwien.docspars.entity.factories;

import at.ac.tuwien.docspars.entity.impl.Dict;

public interface DictCreationable {

  public Dict createDict(int id, String name);
}