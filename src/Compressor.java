import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import sun.text.CodePointIterator;

public class Compressor {
	
	private ArrayList<KanjiInfo> _kanjis;
	private KanjiDBHelper _db;
	
	public Compressor(){
		_db = new KanjiDBHelper();
	}
	
	public void getKanjis(){
		KanjiVGParser p = new KanjiVGParser();
		try {
			p.parse(main._KVGFILE);
			_kanjis = p.parsedKanjis();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void compress(){
		try{
			System.out.println("Compressing & storing " +_kanjis.size() +" kanjis...");
			for(int i=0; i<_kanjis.size(); i++){
				String kanjiVG = _kanjis.get(i).kvg();
				int kanjiId = _kanjis.get(i).id();
				
				byte[] compressedKVG = ZipTools.compress(kanjiVG);

				// Store the compressed kanji in DB
				_db.storeKanjiVGInDB(kanjiId, compressedKVG);
				_db.initFavoriteForKanji(kanjiId);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
