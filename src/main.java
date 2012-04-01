import java.util.ArrayList;

public class main {
	
	//private final String PROJECT_DIR = "/home/kindov/Projects/KanjiVGCompressor/src"; // Linux
	public static final String PROJECT_DIR = "/Users/ShibaKaneki/Projects/Java/KanjiVGCompressor/src"; // Mac
	public static final String _KDIC2FILE = PROJECT_DIR +"/kanjidic2.xml";
	public static final String _KVGFILE = PROJECT_DIR +"/kanjivg.xml";
	
	public static void main(String args[]){
		// -----------------------
		// - Create the database -
		// -----------------------
		System.out.println("Creating database...");
		KanjiDBHelper dbHelper = new KanjiDBHelper();
		dbHelper.createDatabase(); 

		// --------------------
		// -- Get the Kanjis --
		// --------------------
		System.out.println("Getting kanjis from KanjiVG.xml...");
		Compressor c = new Compressor();
		c.getKanjis();
		// Compress each of them
		c.compress();
		
		// -------------------------
		// -- Get the JLPT levels --
		// -------------------------
		System.out.println("Updating JLPT levels...");
		JLPTUpdater jlpt = new JLPTUpdater();
		jlpt.updateJLPTLevels();
		
		// ----------------------------------
		// -- Get the infos from KanjiDic2 --
		// ----------------------------------
		System.out.println("Cleaning KanjiDic2...");
		try{
			String originalKanjiDic2 = TextTools.fileToText(_KDIC2FILE, "UTF8");
			String readyXMLString = TextTools.stripInvalidXMLChars(originalKanjiDic2);
			TextTools.textToFile(readyXMLString, _KDIC2FILE +"_clean.xml", "UTF8");
			
			System.out.println("Parsing KanjiDic2...");
			
				KanjiDic2Parser dico = new KanjiDic2Parser();
				dico.parse(_KDIC2FILE +"_clean.xml");
				ArrayList<KanjiInfo> kanjis = dico.parsedKanjis();
				for(int i=0; i<kanjis.size(); i++){
					KanjiInfo kanji = kanjis.get(i);
					dbHelper.saveInfosFromKanji(kanji);
				}
		}catch(Exception e){
			e.printStackTrace();
		}

		System.out.println("Done!");
	}
}
