import java.io.IOException;
import java.util.ArrayList;

public class Compressor {
	private final String _KVGFILE = "/Users/ShibaKaneki/Projects/Java/KanjiVGCompressor/src/kanjivg.xml"; // Disgusting but it's just for me...
	private final String _DB = "/Users/ShibaKaneki/Projects/Java/KanjiVGCompressor/src/kanjidic2-en.db"; // Disgusting but it's just for me...
	private ArrayList<KanjiInfo> _kanjis;
	
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
		try{
			System.out.println("Compressing & storing kanjis...");
			for(int i=0; i<_kanjis.size(); i++){
				String kanjiVG = _kanjis.get(i).kvg();
				int kanjiId = _kanjis.get(i).id();
				
				byte[] compressedKVG = ZipTools.compress(kanjiVG);
				
				// Store the compressed kanji in DB
				storeKanjiVGInDB(kanjiId, compressedKVG);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void storeKanjiVGInDB(int codepoint, byte[] kvg){
		
	}
}
