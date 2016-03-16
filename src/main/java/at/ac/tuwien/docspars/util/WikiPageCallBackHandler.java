package at.ac.tuwien.docspars.util;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.util.List;

public class WikiPageCallBackHandler implements PageCallbackHandler {

  private static final Logger logger = LogManager.getLogger(WikiPageCallBackHandler.class.getPackage().getName());
  private final DocumentHandler docHandler;

  public WikiPageCallBackHandler(final DocumentHandler docHandler) {
    this.docHandler = docHandler;
  }

  @Override
  public void process(final WikiPage page) {
    logger.debug("parsing " + page.getTitle());
    // using title and text values for IR retrieval
    if (this.docHandler.isMaxElementsReached()) {
      this.docHandler.finalize();
      return;
    }
    final String wikitext = page.getTitle() + " " + page.getWikiText();
    List<String> tokens = null;
    final Timestamp tstmp = null;
    String wikititle = page.getTitle();
    // skipping text processing
    // excluding redirection, special, stubs and disambiguation pages
    if (this.docHandler.isNextElementSkipped() || page.getTitle().contains("(disambiguation)") || page.isRedirect() || page.isSpecialPage() || page.isStub() || page.isDisambiguationPage()) {
      this.docHandler.skipDocument();
      return;
    }
    try {
      // now using the Lucene CustomAnalyzer to tokenize the String
      tokens = DocumentTextProcessor.tokenizeTextStream(wikitext);
      wikititle = DocumentTextProcessor.trimTextTitle(DocumentTextProcessor.deAccent(wikititle));
      // skip timestamp converstion if insert timestamp is uesed
      // converting String to Timestamp Datatype
      // tstmp =
      // DocumentTextProcessor.convertStringToSQLTimestamp(page.getTimestamp(),
      // this.dateFormat);

    } catch (final ParsingDocumentException e1) {
      logger.error("Error in tokenization of text stream " + e1.getLocalizedMessage());
      // proceed to next page
      return;
    }
    this.docHandler.addDocument(Integer.parseInt(page.getID()), Integer.parseInt(page.getRevid()), wikititle, tstmp, tokens);
  }
}
