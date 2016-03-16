package at.ac.tuwien.docspars.entity.factories.impl;

import at.ac.tuwien.docspars.entity.Mode;
import at.ac.tuwien.docspars.entity.factories.DictCreationable;
import at.ac.tuwien.docspars.entity.factories.DocumentCreationable;
import at.ac.tuwien.docspars.entity.factories.TermCreationable;

public class EntityFactory {

  private final DocumentCreationable docFact;
  private final TermCreationable termFact;
  private final DictCreationable dictFact;

  public EntityFactory(final Mode mode) {
    switch (mode) {
      case V1:
        this.docFact = new DocumentFactory();
        this.termFact = new TermFactory();
        this.dictFact = new DictFactory();
        break;
      case V2:
        this.docFact = new DocumentWithSingleTermInstanceFactory();
        this.termFact = new TraceTermFactory();
        this.dictFact = new DictFactory();
        break;
      case V3:
        this.docFact = new DocumentWithSingleTermInstanceFactory();
        this.termFact = new TraceTermFactory();
        this.dictFact = new DictFactory();
        break;
      case V4:
        this.docFact = new DocumentWithSingleTermInstanceFactory();
        this.termFact = new TraceTermFactory();
        this.dictFact = new DictFactory();
        break;
      default:
        this.docFact = new DocumentWithSingleTermInstanceFactory();
        this.termFact = new TermFactory();
        this.dictFact = new DictFactory();
        break;
    }
  }

  public TermCreationable createTermFactory() {
    return this.termFact;
  }

  public DictCreationable createDictFactory() {
    return this.dictFact;
  }

  public DocumentCreationable createDocFacory() {
    return this.docFact;
  }
}
