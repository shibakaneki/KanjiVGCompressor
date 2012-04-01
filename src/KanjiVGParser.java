// Inspired from "http://patatos.over-blog.com/article-lire-un-fichier-xml-en-java-avec-sax-47229047.html"
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class KanjiVGParser extends DefaultHandler{

	private ArrayList<KanjiInfo> _kanjis;	
	private String _kanji;
	private int _id;
	
	public KanjiVGParser(){
		_kanjis = new ArrayList<KanjiInfo>();
		_kanjis.clear();
		_kanji = "";
	}
	
	public ArrayList<KanjiInfo> parsedKanjis(){
		return _kanjis;
	}
	
	public void parse(InputStream input) throws ParserConfigurationException, SAXException, IOException{
		
		Reader reader = new InputStreamReader(input,"UTF-8");
		InputSource is = new InputSource(reader);
		is.setEncoding("UTF-8");
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		
		parser.parse(is, this);
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
		
		for(int i=0; i<attributes.getLength(); i++){
			String name = attributes.getQName(i);
			String value = attributes.getValue(i);
			
			if(name.equals("id") && qName.equals("kanji")){
				int iUnderscore = value.indexOf("_");
				String sId = value.substring(iUnderscore + 1);
				_id = Integer.parseInt(sId,16);
			}			
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
			KanjiInfo kInf = new KanjiInfo();
			kInf.setId(_id);
			kInf.setKvg(_kanji);
			_kanjis.add(kInf);
		}
	}
}
