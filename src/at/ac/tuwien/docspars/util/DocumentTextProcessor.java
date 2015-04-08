package at.ac.tuwien.docspars.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class DocumentTextProcessor {
	
	private List<String> text;
	private Analyzer analyzer;
	// max length of term / terms exceeding will be ignored
	private final int maxTermLength;
	
	
	public DocumentTextProcessor(int maxLength) {
		this.text = null;
		this.analyzer = new EnglishAnalyzer();
		this.maxTermLength = maxLength;
	}
	
	/**
	 * Method converts text String to text array and removes characters and words which do not contain valuable information
	 * @param text String that needs to be processed
	 * @return normalized and stemmed String array having stopwords removed 
	 * @throws ParsingDocumentException 
	 */
	public List<String> clearUpText(String text) throws ParsingDocumentException {
		TokenStream tokens = null;
		this.text = new ArrayList<String>();
		try {
			tokens = this.analyzer.tokenStream("document", new StringReader(text));
			tokens.reset();
			while (tokens.incrementToken()) {
				//additionally remove all Tokens that would exceed configured database
				String dictEntry = tokens.getAttribute(CharTermAttribute.class).toString();
				if (dictEntry!=null && dictEntry.length() <= maxTermLength) {
					this.text.add(dictEntry);
				}
			}
			
		} catch (IOException e) {
			throw new ParsingDocumentException();
		}		

		return this.text;		
	}
	
	public List<String> getclearedText() {
		return this.text;
	}
	
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}
}
