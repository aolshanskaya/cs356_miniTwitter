package cs356_a2;

public class Message implements Post{
	private String message;
	
	public Message(String message){
		
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}
	
	public void editMessage(String message){
		this.message = message;
	}
}
