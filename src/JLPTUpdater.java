import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class JLPTUpdater {

	private static final int MAX_JLPT_LEVEL = 5;
	//private final String PROJECT_DIR = "/home/kindov/Projects/KanjiVGCompressor/src"; // Linux
	private final String PROJECT_DIR = "/Users/ShibaKaneki/Projects/Java/KanjiVGCompressor/src"; // Mac
	private KanjiDBHelper _db;
	
	public JLPTUpdater(){
		_db = new KanjiDBHelper();
	}
	
	public void updateJLPTLevels(){
		System.out.println("Updating JLPT levels...");

		for(int i=1; i<=MAX_JLPT_LEVEL; i++){
			try {
				
				File f  = new File(PROJECT_DIR +"/n" +i +"_list.txt");
				if(!f.exists() && f.length() < 0){
					System.out.println("Error while reading file");
				}else{
					FileReader fr = new FileReader(f);
					BufferedReader reader = new BufferedReader(fr);
					
					String line = "";
					while((line = reader.readLine()) != null){
						_db.saveJLPTLevelForKanji(TextTools.kanjiToCode(new String(line.getBytes(), "UTF-8")), i);
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
