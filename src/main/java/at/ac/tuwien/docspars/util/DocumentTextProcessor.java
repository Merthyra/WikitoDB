package at.ac.tuwien.docspars.util;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class DocumentTextProcessor {

	public static Analyzer ANALYZER;
	// max length of term / terms exceeding will be ignored
	public static int MAX_TERM_LENGTH;

	public static int MAX_TITLE_LENGTH;

	// TO-DO make this call independent from class instantiation
	static {
		MAX_TITLE_LENGTH = 100;
		MAX_TERM_LENGTH = 100;
		ANALYZER = new EnglishAnalyzer();
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
	public static List<String> clearUpText(String text) throws ParsingDocumentException {
		TokenStream tokens = null;
		text = deAccent(text);
		List<String> textList = new ArrayList<String>();
		// text = text.replaceAll("[[^\\p{L}\\p{Z}]]", " ").trim();
		try {
			tokens = ANALYZER.tokenStream("document", new StringReader(text));
			tokens.reset();
			while (tokens.incrementToken()) {
				// additionally remove all Tokens that would exceed configured
				// database

				String dictEntry = tokens.getAttribute(CharTermAttribute.class).toString();
				if (dictEntry != null && dictEntry.length() < MAX_TERM_LENGTH) {
					textList.add(dictEntry);
				}
			}
			tokens.close();

		} catch (IOException e) {
			throw new ParsingDocumentException();
		}

		return textList;
	}

	/**
	 * normalize and deaccent string, removing all non US-ASCII characters (eg.
	 * � - > a)
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
