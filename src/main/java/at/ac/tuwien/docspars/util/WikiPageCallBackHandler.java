package at.ac.tuwien.docspars.util;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class WikiPageCallBackHandler implements PageCallbackHandler {

  private static final Logger logger = LogManager.getLogger(WikiPageCallBackHandler.class);
  private final DocumentHandler docHandler;

  public WikiPageCallBackHandler(final DocumentHandler docHandler) {
    this.docHandler = docHandler;
  }

  @Override
  public void process(final WikiPage page) {
    logger.trace("parsing page: {}", page.getTitle());
    // check if document fulfills basic criteria to be processed
    if (this.docHandler.isDocumentProcessed(Integer.valueOf(page.getID())) || isFilteredOut(page)) {
      this.docHandler.skipDocument();
      logger.trace("document {}  with id: {} skipped because of process properties");
      return;
    }
    List<String> tokens = null;
    final String wikiTitle = DocumentTextProcessor.trimTextTitle(page.getTitle());
    final String wikitext = wikiTitle + " " + page.getWikiText();
    try {
      // now using the Lucene CustomAnalyzer to tokenize the String
      tokens = DocumentTextProcessor.tokenizeTextStream(wikitext);

    } catch (final ParsingDocumentException e1) {
      logger.error("Error in tokenization of text stream {}", e1.getLocalizedMessage());
      return;
    }
    this.docHandler.addDocument(Integer.parseInt(page.getID()), Integer.parseInt(page.getRevid()), wikiTitle, null, tokens);
    logger.trace("document {} with Id {] successfully parsed and staged", page.getTitle(), page.getID());
  }

  boolean isFilteredOut(final WikiPage page) {
    if (page.getTitle().contains("(disambiguation)") || page.isRedirect() || page.isSpecialPage() || page.isStub()
        || page.isDisambiguationPage()) {
      this.docHandler.skipDocument();
      logger.trace("document {}  with id: {} skipped because of invalid format!", page.getTitle(), page.getID());
      return true;
    }
    return false;
  }
}
