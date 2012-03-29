public class main {
	public static void main(String args[]){
		System.out.println("-- KanjiVG Compressor --");
		Compressor c = new Compressor();
		
		// Get the Kanjis
		c.getKanjis();
		
		// Compress each of them
		c.compress();
		
		System.out.println("Done!");
	}
}
