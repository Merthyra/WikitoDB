package at.ac.tuwien.docspars.entity.factories.impl;

import at.ac.tuwien.docspars.entity.Mode;
import at.ac.tuwien.docspars.entity.factories.DocumentCreationable;
import at.ac.tuwien.docspars.entity.factories.TermCreationable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityFactory {
  private static Logger logger = LogManager.getLogger(EntityFactory.class);
  private DocumentCreationable docFact;
  private DocumentCreationable docWithSingleTermInstanceFact;
  private TermCreationable termFact;
  private TermCreationable traceTermFact;
  private boolean isInitialized;

  public EntityFactory(DocumentFactory docFactory, DocumentWithSingleTermInstanceFactory docWithSingleTermInstanceFactory,
      TermFactory termFactory, TraceTermFactory traceTermFactory) {
    this.docFact = docFactory;
    this.docWithSingleTermInstanceFact = docWithSingleTermInstanceFactory;
    this.termFact = termFactory;
    this.traceTermFact = traceTermFactory;
  }

  public void init(Mode mode) {
    if (!isInitialized) {
      switch (mode) {
        case V1:
          break;
        default:
          this.docFact = docWithSingleTermInstanceFact;
          this.termFact = traceTermFact;
          break;
      }
      isInitialized = true;
    } else {
      logger.warn("EntityFactory has already been initialized");
    }
  }

  public TermCreationable createTermBuilder() {
    return this.termFact;
  }

  public DocumentCreationable createDocBuilder() {
    return this.docFact;
  }
}
