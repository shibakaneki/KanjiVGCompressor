import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class KanjiDBHelper {
	
	public KanjiDBHelper(){
		
	}
	
	public void createDatabase(){
		try{
			Class.forName("org.sqlite.JDBC");
		    Connection conn = DriverManager.getConnection("jdbc:sqlite:kanjidb.db");
		    Statement stat = conn.createStatement();
		    stat.executeUpdate("drop table if exists entries;");
		    stat.executeUpdate("drop table if exists favorites;");
		    stat.executeUpdate("drop table if exists android_metadata;");
		    stat.executeUpdate("drop table if exists info;");
		    stat.executeUpdate("create table entries (_id smallint(5), grade smallint(5), strokeCount smallint(2), frequency smallint(2), jlpt smallint(1), paths);");
		    stat.executeUpdate("create table favorites (_id smallint(5), state smallint(1))");
		    stat.executeUpdate("create table onYomi (_id smallint(5), yomi);");
		    stat.executeUpdate("create table kunYomi (_id smallint(5), yomi);");
		    stat.executeUpdate("create table words (_id smallint(5), word);");
		    stat.executeUpdate("create table meanings (_id smallint(5), meaning);");
		    
		    stat.executeUpdate("create table android_metadata (locale)");
		    PreparedStatement prep = conn.prepareStatement("insert into android_metadata values (?);");
		    prep.setString(1, "en_US");
		    prep.addBatch();
		    
		    conn.setAutoCommit(false);
		    prep.executeBatch();
		    conn.setAutoCommit(true);
		    
		    stat.executeUpdate("create table info (version, kanjidic2Version, kanjiVGVersion)");
		    PreparedStatement prep2 = conn.prepareStatement("insert into info values (?, ?, ?);");
		    prep2.setString(1, "1");
		    prep2.setString(2, "2011-11-20");
		    prep2.setString(3, "2012-02-19");
		    prep2.addBatch();
		    
		    conn.setAutoCommit(false);
		    prep2.executeBatch();
		    conn.setAutoCommit(true);
		    
		    conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void storeKanjiVGInDB(int codepoint, byte[] kvg){
		try{	
			Class.forName("org.sqlite.JDBC");
		    Connection conn = DriverManager.getConnection("jdbc:sqlite:kanjidb.db");

		    PreparedStatement prep = conn.prepareStatement("insert into entries (_id, paths) values (?, ?);");

		    prep.setString(1, String.valueOf(codepoint));
		    prep.setBytes(2, kvg);
		    //prep.setString(2, String.valueOf(kvg));
		    prep.addBatch();

		    conn.setAutoCommit(false);
		    prep.executeBatch();
		    conn.setAutoCommit(true);
		    
		    conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void initFavoriteForKanji(int codepoint){
		try{	
			Class.forName("org.sqlite.JDBC");
		    Connection conn = DriverManager.getConnection("jdbc:sqlite:kanjidb.db");

		    PreparedStatement prep = conn.prepareStatement("insert into favorites (_id, state) values (?, ?);");

		    prep.setString(1, String.valueOf(codepoint));
		    prep.setString(2, String.valueOf(0));
		    prep.addBatch();

		    conn.setAutoCommit(false);
		    prep.executeBatch();
		    conn.setAutoCommit(true);
		    
		    conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void testDecompress(int codepoint){
		try{
			Class.forName("org.sqlite.JDBC");
			
			Connection conn = DriverManager.getConnection("jdbc:sqlite:kanjidb.db");
		    Statement stat = conn.createStatement();
		    
		    ResultSet rs = stat.executeQuery("select paths from entries where _id=" +codepoint +";");
		    byte[] compressedData = rs.getBytes("paths");
		    System.out.println("paths = " + compressedData);
		    //System.out.println(compressedData.getBytes());

		    rs.close();
		    conn.close();
		    
		    System.out.println(ZipTools.decompress(compressedData));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void saveJLPTLevelForKanji(int codepoint, int level){
		try{	
			Class.forName("org.sqlite.JDBC");
		    Connection conn = DriverManager.getConnection("jdbc:sqlite:kanjidb.db");

		    PreparedStatement prep = conn.prepareStatement("update entries set jlpt=? where _id=?;");

		    prep.setInt(1, level);
		    prep.setInt(2, codepoint);
		    prep.addBatch();

		    conn.setAutoCommit(false);
		    prep.executeBatch();
		    conn.setAutoCommit(true);
		    
		    conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// create table entries (_id, grade, strokeCount, frequency, jlpt, paths)
	public void saveInfosFromKanji(KanjiInfo kanji){
		try{	
			Class.forName("org.sqlite.JDBC");
		    Connection conn = DriverManager.getConnection("jdbc:sqlite:kanjidb.db");

		    // Update the 'entries' table
		    PreparedStatement prep = conn.prepareStatement("update entries set grade=?, strokeCount=?, frequency=? where _id=?;");

		    if(-1 != kanji.grade()){
		    	prep.setInt(1, kanji.grade());
		    }
		    if(-1 != kanji.strokeCount()){
		    	prep.setInt(2, kanji.strokeCount());
		    }
		    if(-1 != kanji.frequency()){
		    	prep.setInt(3, kanji.frequency());
		    }
		    prep.setInt(4, kanji.id());
		    prep.addBatch();

		    conn.setAutoCommit(false);
		    prep.executeBatch();
		    conn.setAutoCommit(true);
		    
		    // Update the 'onYomi' table
		    for(int i=0; i<kanji.onyomi().size(); i++){
		    	String yomi = kanji.onyomi().get(i);
		    	prep = conn.prepareStatement("insert into onYomi (_id, yomi) values (?, ?);");

			    prep.setInt(1, kanji.id());
			    prep.setString(2, yomi);
			    prep.addBatch();

			    conn.setAutoCommit(false);
			    prep.executeBatch();
			    conn.setAutoCommit(true);
		    }
		    
		    // Update the 'kunYomi' table
		    for(int i=0; i<kanji.kunyomi().size(); i++){
		    	String yomi = kanji.kunyomi().get(i);
		    	prep = conn.prepareStatement("insert into kunYomi (_id, yomi) values (?, ?);");

			    prep.setInt(1, kanji.id());
			    prep.setString(2, yomi);
			    prep.addBatch();

			    conn.setAutoCommit(false);
			    prep.executeBatch();
			    conn.setAutoCommit(true);
		    }
		    
		    // Update the 'meanings' table
		    for(int i=0; i<kanji.meanings().size(); i++){
		    	String meaning = kanji.meanings().get(i);
		    	prep = conn.prepareStatement("insert into meanings (_id, meaning) values (?, ?);");

			    prep.setInt(1, kanji.id());
			    prep.setString(2, meaning);
			    prep.addBatch();

			    conn.setAutoCommit(false);
			    prep.executeBatch();
			    conn.setAutoCommit(true);
		    }
		    
		    conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
