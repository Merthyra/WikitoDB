package at.ac.tuwien.docspars.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.util.List;

public class DocumentHandler {

  protected static final Logger logger = LogManager.getLogger(DocumentHandler.class);

  private final EnvironmentService environmentService;

  public DocumentHandler(final EnvironmentService environmentService) {
    this.environmentService = environmentService;
  }

  public void addDocument(final int did, final int revid, final String name, final Timestamp added, final List<String> content) {
    this.environmentService.addDocument(did, revid, name, added, content);
  }

  @Override
  public void finalize() {
    this.environmentService.shutDown();
  }

  public void skipDocument() {
    this.environmentService.skipDocument();
  }

  public boolean isNextDocumentHandled(int did, int revid) {
    return this.environmentService.doHandleNextDocument(did, revid);
  }
}