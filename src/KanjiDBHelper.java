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
			System.out.println("Creating Database...");
			Class.forName("org.sqlite.JDBC");
		    Connection conn = DriverManager.getConnection("jdbc:sqlite:kanjidb.db");
		    Statement stat = conn.createStatement();
		    stat.executeUpdate("drop table if exists entries;");
		    stat.executeUpdate("drop table if exists favorites;");
		    stat.executeUpdate("drop table if exists android_metadata;");
		    stat.executeUpdate("create table entries (_id, grade, strokeCount, frequency, jlpt, paths);");
		    stat.executeUpdate("create table favorites (_id, state)");
		    
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
		    prep.setString(2, String.valueOf(kvg));
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
	
	private void testDecompress(int codepoint){
		try{
			Class.forName("org.sqlite.JDBC");
			
			Connection conn = DriverManager.getConnection("jdbc:sqlite:kanjidb.db");
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
	
	public void saveJLPTLevelForKanji(int codepoint, int level){
		try{	
			Class.forName("org.sqlite.JDBC");
		    Connection conn = DriverManager.getConnection("jdbc:sqlite:kanjidb.db");

		    PreparedStatement prep = conn.prepareStatement("update entries set jlpt=? where _id=?;");

		    prep.setString(1, String.valueOf(level));
		    prep.setString(2, String.valueOf(codepoint));
		    prep.addBatch();

		    conn.setAutoCommit(false);
		    prep.executeBatch();
		    conn.setAutoCommit(true);
		    
		    conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
