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

  public Dict locateOrCreateDictionaryEntry(final String term) {
    Optional<Dictionable> dictElem = Optional.ofNullable(this.persistedDict.get(term));
    if (dictElem.isPresent()) {
      return (Dict) dictElem.get();
    }
    Dict newDict = new Dict(++indexNr, term);
    persistedDict.put(term, newDict);
    return newDict;
  }

  public void setup(PersistanceService service) {
    this.persistedDict = service.readDict();
    indexNr = this.persistedDict.size() + 1;
  }

}
