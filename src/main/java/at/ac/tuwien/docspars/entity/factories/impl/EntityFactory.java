package at.ac.tuwien.docspars.entity.factories.impl;

import at.ac.tuwien.docspars.entity.PersistVariant;
import at.ac.tuwien.docspars.entity.factories.DictCreationable;
import at.ac.tuwien.docspars.entity.factories.DocumentCreationable;
import at.ac.tuwien.docspars.entity.factories.TermCreationable;

public abstract class EntityFactory {

  private final DocumentCreationable docFact;
  private final TermCreationable termFact;
  private final DictCreationable dictFact;

  public EntityFactory(PersistVariant variant) {
    switch (variant) {
      case V1:
        this.docFact = new DocumentFactory();
        this.termFact = new RevTermFactory();
        this.dictFact = new DictFactory();
        break;
      case V2:
        this.docFact = new DocumentFactory();
        this.termFact = new TraceTermFactory();
        this.dictFact = new DictFactory();
        break;
      case V3:
        this.docFact = new DocumentFactory();
        this.termFact = new TraceTermFactory();
        this.dictFact = new DictFactory();
        break;
      case V4:
        this.docFact = new DocumentFactory();
        this.termFact = new TraceTermFactory();
        this.dictFact = new DictFactory();
        break;
      default:
        this.docFact = new DocumentFactory();
        this.termFact = new RevTermFactory();
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
