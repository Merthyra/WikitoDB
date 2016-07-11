package at.ac.tuwien.docspars.entity.factories.impl;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.impl.Dict;
import at.ac.tuwien.docspars.io.services.PersistanceService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DictionaryService {

  private Map<String, Dictionable> persistedDict = new HashMap<>();
  private int indexNr = 0;

  public Dict createNewDictionaryEntry(final String term) {
    Dict newDict = new Dict(++indexNr, term);
    persistedDict.put(term, newDict);
    return newDict;
  }

  public Optional<Dictionable> getDictFromMemory(final String term) {
    return Optional.ofNullable(this.persistedDict.get(term));
  }

  public void setup(PersistanceService service) {
    this.persistedDict = service.readDict();
    indexNr = this.persistedDict.size() + 1;
  }

}
