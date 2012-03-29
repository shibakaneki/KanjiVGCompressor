import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Compressor {
	private final String _KVGFILE = "/Users/ShibaKaneki/Projects/Java/KanjiVGCompressor/src/kanjivg.xml";
	private ArrayList<String> _kanjis;
	
	public Compressor(){
		
	}
	
	public void getKanjis(){
		System.out.println("Getting kanjis from KanjiVG.xml...");
		KanjiVGParser p = new KanjiVGParser();
		try {
			p.parse(_KVGFILE);
			_kanjis = p.parsedKanjis();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void compress(){
		System.out.println("Compressing kanjis...");
		for(int i=0; i<_kanjis.size(); i++){
			String kanjiVG = _kanjis.get(i);
			
			// TODO: Retrieve the codepoint of the current kanji and compress it
			String compressedKVG = /*compress(*/kanjiVG/*)*/;
			
			// Store the compressed kanji in DB
			storeKanjiVGInDB(0, compressedKVG);
		}
	}
	
	public void storeKanjiVGInDB(int codepoint, String kvg){
		System.out.println("----------------");
		System.out.println(kvg);
	}
}
