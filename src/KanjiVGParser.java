// Inspired from "http://patatos.over-blog.com/article-lire-un-fichier-xml-en-java-avec-sax-47229047.html"

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class KanjiVGParser extends DefaultHandler{

	private ArrayList<String> _kanjis;	// TODO: use a map<codepoint, kanjiNodeAsString>
	private String _kanji;
	
	public KanjiVGParser(){
		_kanjis = new ArrayList<String>();
		_kanjis.clear();
		_kanji = "";
	}
	
	public ArrayList<String> parsedKanjis(){
		return _kanjis;
	}
	
	public void parse(InputStream input) throws ParserConfigurationException, SAXException, IOException{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(false);
		SAXParser parser = factory.newSAXParser();
		parser.parse(input, this);
	}
	
	public void parse(String filename) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException{
		parse(new FileInputStream(filename));
	}
	
	public void startDocument() throws SAXException{
		// Nothing to do
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
		if(qName.equals("kanji")){
			// We start a new kanji element so we erase the old content of _kanji
			_kanji = "<" +qName;
		}else{
			_kanji += "<" +qName;
		}
		// TODO: add the missing elements
		for(int i=0; i<attributes.getLength(); i++){
			String name = attributes.getQName(i);
			String value = attributes.getValue(i);
			_kanji += " " +name +"='" +value +"'";
		}
		
		// Finally we add the closing bracket
		_kanji += ">";
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException{
		// Close the current element
		_kanji += "</" +qName +">";
		
		// If the element was a kanji, store it in the list
		if(qName.equals("kanji")){
			_kanjis.add(_kanji);
		}
	}
	
	
}
