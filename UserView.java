package cs356_a2;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;

public class UserView extends UI{
	private User user;
	private JTextField userIDtext;
	private JTextArea tweetMessage;
	private static AdminControlPanel acp = null;
	private DefaultListModel<String> fList;
	private DefaultListModel<String> pList;
	private int numFollowing;
	private int numOwnPosts;
	private int totalNewsFeedPosts;
	private JLabel timeUpdated;
	
	public UserView(User u){
		numFollowing = 0;
		totalNewsFeedPosts = 0;
		numOwnPosts = 0;
		
		user = u;
		user.createView(this);
		acp.getACP();
		configureUserView();
	}
	
	private void configureUserView(){
		JFrame uvFrame = new JFrame("User " + user.getIDString());
		uvFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        uvFrame.setLocation(this.getXPos(), this.getYPos());
        uvFrame.setSize(this.getDim().width / 3 , this.getDim().height / 2);
		
		JPanel uvPanel = new JPanel();
		uvPanel.setLayout(new GridLayout(4,1));
		
		uvPanel.add(followUserPanel());
		uvPanel.add(followingList());
		uvPanel.add(postTweet());
		uvPanel.add(tweetsList());

		uvFrame.add(uvPanel);	
		uvFrame.setVisible(true);
	}
	
	private JPanel followUserPanel(){
		JPanel followPanel = new JPanel();
		
		JPanel userPanel = new JPanel();
		userPanel.add(new JLabel("User has been active since: " + user.getTimeCreated()));
		timeUpdated = new JLabel("User feed was last updated: " + user.getLastUpdateTime());
		userPanel.add(timeUpdated);
		
		userIDtext = new JTextField(25);
		userIDtext.setText("USER ID");
		JButton followUserButton = new JButton("FOLLOW USER");
		followUserButton.addActionListener(new followUserButtonPressed());
		
		followPanel.add(userPanel);
		followPanel.add(userIDtext);	
		followPanel.add(followUserButton);
		
		return followPanel;
	}
	
	private JPanel followingList(){
		JPanel followingPanel = new JPanel();
		followingPanel.setLayout(new BorderLayout());
		
		fList = new DefaultListModel();
		JLabel followingListLabel = new JLabel("CURRENTLY FOLLOWING:");
		JList followingList = new JList(fList);
		followingList.setVisibleRowCount(7);
		
		followingPanel.add(followingListLabel , BorderLayout.NORTH);
		followingPanel.add(new JScrollPane(followingList));
		return followingPanel;
	}
	
	private JPanel postTweet(){
		JPanel postPanel = new JPanel();
		
		tweetMessage = new JTextArea(2 , 30);
		tweetMessage.setText("Write something...");
		JButton postTweetButton = new JButton("POST TWEET");
		postTweetButton.addActionListener(new postTweet());
		
		postPanel.add(tweetMessage);	
		postPanel.add(postTweetButton);
		
		return postPanel;
	}
	
	private JPanel tweetsList(){
		JPanel tweetsPanel = new JPanel();
		tweetsPanel.setLayout(new BorderLayout());
		
		pList = new DefaultListModel();
		JLabel postListLabel = new JLabel("NEWS FEED");
		JList postList = new JList(pList);
		postList.setVisibleRowCount(7);
		
		tweetsPanel.add(postListLabel , BorderLayout.NORTH);
		tweetsPanel.add(new JScrollPane(postList));
		return tweetsPanel;
	}
	
	
	/*
	 * HELPER & LOGIC METHODS
	 */
	
	public void updateTweetList(String m){
		pList.add(totalNewsFeedPosts, m);
		totalNewsFeedPosts++;
	}
	
	private boolean checkIfPositive(String m){
		if(m.contains("good") || m.contains("great") || m.contains("fantastic") || m.contains("excellent")){
			return true;
		}
		return false;
	}
	
	public void addNewTweet(String message){
		pList.add(totalNewsFeedPosts , message);
	}
	
	private boolean canFollow(User u){
		if(u == null || u == user || fList.contains(u.getIDString()))
			return false;
		return true;
	}
	
	/*
	 * ACTION LISTENERS
	 */
	
	private class postTweet implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String toAdd = user.getIDString().concat(": ");
			toAdd = toAdd.concat(tweetMessage.getText());
			
			user.postTweet(toAdd);
			
			timeUpdated.setText("User feed was last updated: " + user.getLastUpdateTime());
			acp.updateLastUpdate(user.getIDString());
			numOwnPosts++;
			acp.incrementTweetStats(checkIfPositive(tweetMessage.getText()));
		}
	}
	
	private class followUserButtonPressed implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(canFollow(acp.getUserByID(userIDtext.getText()))){
				user.addFollowing(acp.getUserByID(userIDtext.getText()));
				fList.add(numFollowing, acp.getUserByID(userIDtext.getText()).getIDString());
				numFollowing++;
			}
		}
	}
	
}
