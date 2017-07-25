package cs356_a2;

public class UserID implements ID{
	private String id;
	
	public UserID(String id){
		this.id = id;
	}
	
	public String getID(){
		return id;
	}
}
