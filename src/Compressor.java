import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import sun.text.CodePointIterator;

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
			System.out.println("Compressing & storing " +_kanjis.size() +" kanjis...");
			for(int i=0; i<_kanjis.size(); i++){
				String kanjiVG = _kanjis.get(i).kvg();
				int kanjiId = _kanjis.get(i).id();
				
				byte[] compressedKVG = ZipTools.compress(kanjiVG);
				if(33 == kanjiId){
					System.out.println("id 33 compressed before stored in db: " +compressedKVG);
					System.out.println(String.valueOf(compressedKVG));
					System.out.println(ZipTools.decompress(compressedKVG));
				}
				// Store the compressed kanji in DB
				storeKanjiVGInDB(kanjiId, compressedKVG);
			}
			// Just some test
			testDecompress(33);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void storeKanjiVGInDB(int codepoint, byte[] kvg){
		try{
			
			Class.forName("org.sqlite.JDBC");
		    Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
		    Statement stat = conn.createStatement();
		    stat.executeUpdate("drop table if exists entries;");
		    stat.executeUpdate("create table entries (_id, paths);");
		    PreparedStatement prep = conn.prepareStatement(
		      "insert into entries values (?, ?);");

		    prep.setString(1, String.valueOf(codepoint));
		    prep.setString(2, String.valueOf(kvg));
		    prep.addBatch();

		    conn.setAutoCommit(false);
		    prep.executeBatch();
		    conn.setAutoCommit(true);
		    conn.close();
			
			// TODO: Uncomment this code because it's the kanjidb one
			/*Class.forName("org.sqlite.JDBC");
		    Connection conn = DriverManager.getConnection("jdbc:sqlite:kanjidic2-en.db");
		    Statement stat = conn.createStatement();
		    stat.execute("UPDATE entries SET paths='" +kvg +"' WHERE _id=" +codepoint);
		    conn.close();*/
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void testDecompress(int codepoint){
		try{
			Class.forName("org.sqlite.JDBC");
			
			Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
		    Statement stat = conn.createStatement();
		    
		    ResultSet rs = stat.executeQuery("select paths from entries where _id=" +codepoint +";");
		    String compressedData = rs.getString("paths");
		    System.out.println("paths = " + compressedData);
		    System.out.println(compressedData.getBytes());

		    rs.close();
		    conn.close();
		    
		    System.out.println(ZipTools.decompress(compressedData.getBytes()));
			
			
			// TODO: Uncomment this code because it's the kanjidb one
			/*Connection conn = DriverManager.getConnection("jdbc:sqlite:kanjidic2-en.db");
		    Statement stat = conn.createStatement();
		    
		    ResultSet rs = stat.executeQuery("select paths from entries where _id=" +codepoint +";");
		    String compressedData = rs.getString("paths");
		    System.out.println("paths = " + compressedData);
		    System.out.println(compressedData.getBytes());

		    rs.close();
		    conn.close();
		    
		    System.out.println(ZipTools.decompress(compressedData.getBytes()));
		    */
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
