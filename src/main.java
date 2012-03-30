public class main {
	
	public static void main(String args[]){
		// -----------------------
		// - Create the database -
		// -----------------------
		KanjiDBHelper dbHelper = new KanjiDBHelper();
		dbHelper.createDatabase();

		// --------------------
		// -- Get the Kanjis --
		// --------------------
		Compressor c = new Compressor();
		c.getKanjis();
		// Compress each of them
		c.compress();
		
		// ----------------------------
		// -- Populate the favorites --
		// ----------------------------
		dbHelper.initFavorites();
		
		// -------------------------
		// -- Get the JLPT levels --
		// -------------------------
		JLPTUpdater jlpt = new JLPTUpdater();
		jlpt.updateJLPTLevels();
		
		// ----------------------------------
		// -- Get the infos from KanjiDic2 --
		// ----------------------------------
		KanjiDic2Parser dico = new KanjiDic2Parser();
		dico.parseDico();
		
		System.out.println("Done!");
	}
}
