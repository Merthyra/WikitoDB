package at.ac.tuwien.docspars.util;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.util.List;

public class WikiPageCallBackHandler implements PageCallbackHandler {

  private static final Logger logger = LogManager.getLogger(WikiPageCallBackHandler.class);
  private final DocumentHandler docHandler;

  public WikiPageCallBackHandler(final DocumentHandler docHandler) {
    this.docHandler = docHandler;
  }

  @Override
  public void process(final WikiPage page) {
    logger.debug("parsing page: {}", page.getTitle());
    // check if document fulfills basic criteria to be processed
    if (this.docHandler.isDocumentProcessed(Integer.valueOf(page.getID()))) {
      this.docHandler.skipDocument();
      logger.trace("document {}  with id: {} skipped because of process properties");
      return;
    }
    final String wikitext = page.getTitle() + " " + page.getWikiText();
    List<String> tokens = null;
    final Timestamp tstmp = null;
    String wikititle = page.getTitle();
    // excluding redirection, special, stubs and disambiguation pages
    if (page.getTitle().contains("(disambiguation)") || page.isRedirect() || page.isSpecialPage() || page.isStub() || page.isDisambiguationPage()) {
      this.docHandler.skipDocument();
      logger.trace("document {}  with id: {} skipped because of invalid format!", page.getTitle(), page.getID());
      return;
    }
    try {
      // now using the Lucene CustomAnalyzer to tokenize the String
      tokens = DocumentTextProcessor.tokenizeTextStream(wikitext);
      wikititle = DocumentTextProcessor.trimTextTitle(DocumentTextProcessor.deAccent(wikititle));

    } catch (final ParsingDocumentException e1) {
      logger.error("Error in tokenization of text stream {}", e1.getLocalizedMessage());
      return;
    }
    this.docHandler.addDocument(Integer.parseInt(page.getID()), Integer.parseInt(page.getRevid()), wikititle, tstmp, tokens);
    logger.trace("document {} with Id {] successfully parsed and staged", page.getTitle(), page.getID());
  }
}
