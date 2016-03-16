package at.ac.tuwien.docspars.util;

import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DocumentTextProcessor {

  public static Analyzer ANALYZER;
	// max length of term / terms exceeding will be ignored
	public static int MAX_TERM_LENGTH;

	public static int MAX_TITLE_LENGTH;
	private static final Logger logger = LogManager.getLogger(DocumentTextProcessor.class);
	// TO-DO make this call independent from class instantiation
	static {
		MAX_TITLE_LENGTH = 95;
		MAX_TERM_LENGTH = 95;
		try {
		ANALYZER = CustomAnalyzer.builder(Paths.get("src/main/resources"))
				 .withTokenizer("wikipedia")
//				 .addTokenFilter("standard")
				 .addTokenFilter("asciifolding")
				 .addTokenFilter("lowercase")
				 .addTokenFilter("elision")
				 .addTokenFilter("stop", "ignoreCase", "true", "words", "stop-words_en.txt")
				 .addTokenFilter("porterstem")
				 .addTokenFilter("trim")
				 .addTokenFilter("length", "min", "2", "max", String.valueOf(MAX_TERM_LENGTH))
				 .build();
		}
		catch (IOException ie) {
			logger.error("Error initializing custom analyzer, using StandardAnalyzer instead!");
			ANALYZER = new EnglishAnalyzer();
		}
	}

	/**
	 * Method converts text String to text array and removes characters and
	 * words which do not contain valuable information
	 *
	 * @param text
	 *            String that needs to be processed
	 * @return normalized and stemmed String array having stopwords removed
	 * @throws ParsingDocumentException
	 */
	@PerformanceMonitored
	public static List<String> tokenizeTextStream(String text) throws ParsingDocumentException {
	    TokenStream tokens = null;
		List<String> textList = new ArrayList<String>();
		long finalNrOfCharacters= 0;
		try {
			tokens = ANALYZER.tokenStream("document", new StringReader(text));
			tokens.reset();

			while (tokens.incrementToken()) {
				// additionally remove all Tokens that would exceed configured
				// database
				String dictEntry = tokens.getAttribute(CharTermAttribute.class).toString();
				// remove all non ascii character terms for convenience reasons
				if (dictEntry != null && dictEntry.matches("\\A\\p{ASCII}*\\z")) {
					// remove ' signs,
//					dictEntry.replaceAll(new String(new char[] {(char) 96}), "");
//					dictEntry.replaceAll(new String(new char[] {(char) 39}), "");
					// finally remove ' signs
				    String remainingTerm = dictEntry.replaceAll("'", "");
				    finalNrOfCharacters+=remainingTerm.length();
					textList.add(remainingTerm);
				}
			}
			tokens.close();

		} catch (IOException e) {
			throw new ParsingDocumentException("Error caused in Text Tokenization and Filtering/Stemming");
		}
		logger.debug("Document content with {} characters split into {} terms of total characters {} : cut-ratio[%] {}", text.length(), textList.size(), finalNrOfCharacters, finalNrOfCharacters/text.length());
		return textList;
	}

	/**
	 * normalize and deaccent string, removing all non US-ASCII characters (eg.
	 * 'a - > a)
	 *
	 * @param string
	 *            to be processed
	 * @return cleared string
	 */
	public static String deAccent(String str) {
		String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
		return nfdNormalizedString.replaceAll("\\P{InBasic_Latin}+", "");
	}

	/**
	 * method trims length of strings on the righthand side to fit in
	 * MAX_TITLE_LENGTH
	 *
	 * @param string
	 *            to be processed
	 * @return trimed string with length min(MAX_TITLE_LENGTH, length(str))
	 */
	public static String trimTextTitle(String title) {
		if (title.length() >= MAX_TITLE_LENGTH) {
			title = title.substring(0, MAX_TITLE_LENGTH - 2);
		}
		return title;
	}

	/**
	 * Method converts String with given Pattern to SQLTimestamp
	 *
	 * @param timestamp
	 *            the timestamped string
	 * @param formatPattern
	 *            pattern to parse timestamp
	 * @return SQLTimestamp
	 */
	public static Timestamp convertStringToSQLTimestamp(String timestamp, String formatPattern) {

		Timestamp timest = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(formatPattern);
			timest = new Timestamp((dateFormat.parse(timestamp)).getTime());
		} catch (ParseException ex) {
			timest = new Timestamp(System.currentTimeMillis());
		}
		return timest;
	}
}
