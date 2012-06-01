import java.util.ArrayList;


public class KanjiInfo {
	private int _id;
	private String _kvg;
	private int _grade;
	private int _strokeCount;
	private int _frequency;
	private ArrayList<String> _onyomi;
	private ArrayList<String> _kunyomi;
	private ArrayList<String> _meaning;
	

	// TODO : Add the meaning and deal with the yomi when the description zone will be implemented.
	
	public KanjiInfo(){
		_id = -1;
		_kvg = "";
		_grade = -1;
		_strokeCount = -1;
		_frequency = -1;
		_onyomi = new ArrayList<String>();
		_kunyomi = new ArrayList<String>();
		_meaning = new ArrayList<String>();
	}
	
	public void setId(int id){
		_id = id;
	}
	
	public int id(){
		return _id;
	}
	
	public void setKvg(String kvg){
		_kvg = kvg;
	}
	
	public String kvg(){
		return _kvg;
	}
	
	public int grade(){
		return _grade;
	}
	
	public void setGrade(int grade){
		_grade = grade;
	}

	public void setStrokeCount(int count){
		_strokeCount = count;
	}
	
	public int strokeCount(){
		return _strokeCount;
	}

	public void setFrequency(int freq){
		_frequency = freq;
	}
	
	public int frequency(){
		return _frequency;
	}

	public void addOnYomi(String yomi){
		_onyomi.add(yomi);
	}
	
	public ArrayList<String> onyomi(){
		return _onyomi;
	}

	public void addKunYomi(String yomi){
		_kunyomi.add(yomi);
	}
	
	public ArrayList<String> kunyomi(){
		return _kunyomi;
	}
	
	public void addMeaning(String m){
		_meaning.add(m);
	}
	
	public ArrayList<String> meanings(){
		return _meaning;
	}
}
