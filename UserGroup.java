package cs356_a2;

import java.util.*;

public class UserGroup implements UserComponent{
	private int numUsers;
	private GroupID gID;
	private long creationTime;
	
	public UserGroup(String groupID){
		gID = new GroupID(groupID);
		creationTime = System.currentTimeMillis();
	}
	
	public ID getID() {
		return gID;
	}


	public String getIDString() {
		return gID.getID();
	}

	
	public long getTimeCreated() {
		return creationTime;
	}
}
