
public class KanjiInfo {
	private int _id;
	private String _kvg;
	
	public KanjiInfo(){
		_id = -1;
		_kvg = "";
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
	
}
