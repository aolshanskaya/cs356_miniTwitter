package cs356_a2;

import java.util.*;

public class User implements Observer , UserComponent{

	private UserID id;
	private List<User> followers;
	private List<User> following;
	private boolean viewExists = false;
	private UserView view;
	private long creationTime;
	private long lastUpdateTime;
	
	public User(String id){
		this.id = new UserID(id);
		followers = new ArrayList<>();
		followers.add(this);
		following = new ArrayList<>();
		creationTime = System.currentTimeMillis();
		lastUpdateTime = System.currentTimeMillis();
	}
	
	/**
	 * @return UserID object associated with this User.
	 */
	public UserID getID(){
		return id;
	}
	
	/**
	 * @return String representation of the UserID.
	 */
	public String getIDString(){
		return id.getID();
	}
	
	
	/**
	 * Used by UserView UI class. 
	 * Takes a String, converts it to a Message object, calls 
	 * the updateTweetStack method inherited from the Observer interface.
	 * @param String message
	 */
	public void postTweet(String message){
		for(User u : followers)
			u.updateTweetStack(message);
	}
	
	/**
	 * Inherited from the Observer interface. 
	 * Pushes the message to all Observers' message stacks as well as this user's.
	 * @param m
	 */
	public void updateTweetStack(String m) {
		view.addNewTweet(m);
		lastUpdateTime = System.currentTimeMillis();
	}
	
	/**
	 * Used by UserView UI class.
	 * Adds the user follow into this user's following list. 
	 * Calls follow's addFollower method to add this user to their followers list.
	 * @param User follow
	 */
	public void addFollowing(User follow){
		following.add(follow);
		follow.addFollower(this);
	}
	
	/**
	 * Called by the addFollowing() methods of other users when they follow this one. 
	 * Adds the User follower to this user's followers list. 
	 * @param User follower
	 */
	public void addFollower(User follower){
		followers.add(follower);
	}
	
	public List<User> getFollowing(){
		return following;
	}
	
	public List<User> getFollowers(){
		List<User> temp = followers;
		followers.add(this);
		return temp;
	}
	
	public String[] getFollowingIDStrings(){
		String[] followingIDs = new String[following.size()];
		int i = 0;
		for(User u : following){
			followingIDs[i] = u.getIDString();
			i++;
		}
		return followingIDs;
	}

	
	
	public void createView(UserView v){
		view = v;
		viewExists = false;
	}
	
	public boolean getViewExists(){
		return viewExists;
	}
	
	public long getTimeCreated() {
		return creationTime;
	}
	
	public long getLastUpdateTime() {
		return lastUpdateTime;
	}
}
