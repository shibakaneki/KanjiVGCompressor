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


public class KanjiDic2Parser extends DefaultHandler {

	private ArrayList<KanjiInfo> _kanjis;	
	private KanjiInfo _kanji;
	private boolean _literal;
	private boolean _grade;
	private boolean _strokeCount;
	private boolean _freq;
	private boolean _r_type;
	private boolean _onyomi;
	private boolean _kunyomi;
	private boolean _meaning;
	
	public KanjiDic2Parser(){
		_kanjis = new ArrayList<KanjiInfo>();
		_kanjis.clear();
		_kanji = new KanjiInfo();
		_literal = false;
		_grade = false;
		_strokeCount = false;
		_freq = false;
		_r_type = false;
		_onyomi = false;
		_kunyomi = false;
		_meaning = false;
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
		if(qName.equals("character")){
			_kanji = new KanjiInfo();
		}else if(qName.equals("literal")){
			_literal = true;
		}else if(qName.equals("grade")){
			_grade = true;
		}else if(qName.equals("stroke_count")){
			_strokeCount = true;
		}else if(qName.equals("freq")){
			_freq = true;
		}else if(qName.equals("reading")){
			for(int i=0; i<attributes.getLength(); i++){
				String name = attributes.getQName(i);
				String value = attributes.getValue(i);
				
				if(name.equals("r_type") && value.equals("ja_on")){
					_r_type = true;
					_onyomi = true;
					break;
				}else if(name.equals("r_type") && value.equals("ja_kun")){
					_r_type = true;
					_kunyomi = true;
					break;
				}			
			}
		}else if(qName.equals("meaning")){
			boolean english = true;
			for(int i=0; i<attributes.getLength(); i++){
				String name = attributes.getQName(i);
				String value = attributes.getValue(i);
				if(name.equals("m_lang")){
					english = false;
					break;
				}
			}
			if(english){
				_meaning = true;
			}
		}
	}
	
	public void characters(char[] ch, int start, int length){
		String value = new String(ch, start, length);
		if(_literal){
			_kanji.setId(TextTools.kanjiToCode(value));
			_literal = false;
		}else if(_grade){
			_kanji.setGrade(Integer.parseInt(value));
			_grade = false;
		}else if(_strokeCount){
			_kanji.setStrokeCount(Integer.parseInt(value));
			_strokeCount = false;
		}else if(_freq){
			_kanji.setFrequency(Integer.parseInt(value));
			_freq = false;
		}else if(_r_type){
			if(_onyomi){
				_kanji.addOnYomi(value);
			}else if(_kunyomi){
				_kanji.addKunYomi(value);
			}
			_r_type = false;
			_onyomi = false;
			_kunyomi = false;
		}else if(_meaning){
			_kanji.addMeaning(value);
			_meaning = false;
		}
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException{
		// If the element was a kanji, store it in the list
		if(qName.equals("character")){
			_kanjis.add(_kanji);
		}
	}
}
