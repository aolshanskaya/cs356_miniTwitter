package cs356_a2;

import java.util.*;

import javax.swing.*;
import javax.swing.tree.*;

import java.awt.*;
import java.awt.event.*;

public class AdminControlPanel extends UI{
	private int numUsers;
	private int numGroups;
	private static int totalNumTweets;
	private static float numPosTweets;
	
	private static Map<UserComponent , DefaultMutableTreeNode> userTreeComponents;
	
	private static AdminControlPanel acp;
	
	private JTextField userID;
	private JTextField groupID;
	private JTree userTree;
	private DefaultMutableTreeNode root;
	private JLabel output;
	private static String idOfLastToUpdate;
	
	private AdminControlPanel(){
		numUsers = 0;
		numGroups = 0;
		totalNumTweets = 0;
		numPosTweets = 0;
		
		configureAdminControlPanel();
	}
	
	public static AdminControlPanel getACP(){
		if(acp == null){
			AdminControlPanel temp = new AdminControlPanel();
			acp = temp;
		}
		return acp;
	}
	
	
	/*
	 * GUI CONFIGURATION AND INSTANTIATION 
	 */
	
	private void configureAdminControlPanel(){
        JFrame acpFrame = new JFrame("Admin Control Panel");
        acpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        acpFrame.setLocation(this.getXPos(), this.getYPos());
        acpFrame.setSize(this.getDim().width / 2 , this.getDim().height / 2);
        
        JPanel acpPanel = new JPanel();
        acpPanel.setLayout(new BorderLayout());
		
		acpPanel.add(treePanel() , BorderLayout.WEST);
		acpPanel.add(buttonsPanel() , BorderLayout.EAST);
		
		acpFrame.add(acpPanel);
		acpFrame.setVisible(true);
	}
	
	private JPanel treePanel(){
		JPanel treePanel = new JPanel();
		root = new DefaultMutableTreeNode("SOCIAL NETWORK                              ");
		userTreeComponents = new HashMap<>();
		userTree = new JTree(root);
		userTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		JScrollPane scroller = new JScrollPane(userTree);
		
		treePanel.add(scroller);
		return treePanel;
	}
	
	/**
	 * Right side panels.
	 */
	private JPanel buttonsPanel(){
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(4 , 1));
        
		JButton openUserViewButton = new JButton("OPEN USER VIEW");
		openUserViewButton.addActionListener(new openUserViewButtonPressed());
		JButton addUserToGroupButton = new JButton("ADD USER TO GROUP");
		addUserToGroupButton.addActionListener(new addUserToGroupButtonPressed());
		
		JPanel openAndAddGroupPanel = new JPanel();
		openAndAddGroupPanel.add(openUserViewButton);
		openAndAddGroupPanel.add(addUserToGroupButton);
		
		output = new JLabel("");
		output.setAlignmentX(CENTER_ALIGNMENT);
		
		buttonsPanel.add(addingPanel());
		buttonsPanel.add(openAndAddGroupPanel);
		buttonsPanel.add(output);
		buttonsPanel.add(statsPanel());
		
		return buttonsPanel;
	}
	
	/**
	 * Bottom buttons
	 */
	private JPanel statsPanel(){
		JPanel statsPanel = new JPanel();
		statsPanel.setLayout(new BorderLayout());
		statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JButton userTotalButton = new JButton("TOTAL NUMBER OF USERS");
		userTotalButton.addActionListener(new userTotalButtonPressed());
		
		JButton groupTotalButton = new JButton("TOTAL NUMBER OF GROUPS");
		groupTotalButton.addActionListener(new groupTotalButtonPressed());
		
		JButton messageTotalButton = new JButton("TOTAL NUMBER OF MESSAGES");
		messageTotalButton.addActionListener(new messageTotalButtonPressed());
		
		JButton percentButton = new JButton("PERCENTAGE OF POSITIVE MESSAGES	");
		percentButton.addActionListener(new posMessageTotalButtonPressed());
		
		JButton idVerificationButton = new JButton("VERIFY ID");
		idVerificationButton.addActionListener(new idVerificationButtonPressed());
		
		JButton lastUpdateButton = new JButton("LAST USER TO POST");
		lastUpdateButton.addActionListener(new latestUpdateButtonPressed());
	
		JPanel firstSet = new JPanel();
		JPanel secondSet = new JPanel();
		JPanel thirdSet = new JPanel();
		
		firstSet.add(userTotalButton);
		firstSet.add(groupTotalButton);
		secondSet.add(messageTotalButton);
		secondSet.add(percentButton);
		thirdSet.add(idVerificationButton);
		thirdSet.add(lastUpdateButton);
		
		statsPanel.add(firstSet , BorderLayout.NORTH);
		statsPanel.add(secondSet);
		statsPanel.add(thirdSet , BorderLayout.SOUTH);
		
		return statsPanel;
	}
	/**
	 * Top fields and buttons.
	 */
	private JPanel addingPanel(){
		JPanel addingPanel = new JPanel();
		
		JButton addUserButton = new JButton("ADD USER");
		userID = new JTextField(20);
		userID.setText("USER ID");
		addUserButton.addActionListener(new addUserButtonPressed());
		
		addingPanel.add(userID);
		addingPanel.add(addUserButton);
		
		JButton addGroupButton = new JButton("ADD GROUP");
		groupID = new JTextField(20);
		groupID.setText("GROUP ID");
		addGroupButton.addActionListener(new addGroupButtonPressed());

		addingPanel.add(groupID);
		addingPanel.add(addGroupButton , BorderLayout.EAST);
				
		return addingPanel;
	}
	
	/*
	 *  HELPER & LOGIC METHODS
	 */
	
	public static void incrementTweetStats(boolean pos){
		totalNumTweets++;
		if(pos)
			numPosTweets++;
	}
	
	public static User getUserByID(String id){
		return (User)getComponentByID(id);
	}
	
	private boolean componentExists(String userID){
		if(getComponentByID(userID) != null)
			return true;
		return false;
	}
	
	private static UserComponent getComponentByID(String id){
		for(UserComponent g: userTreeComponents.keySet()){
			if(g.getIDString().equals(id)){
				return g;
			}
		}
		return null;
	}
	
	private DefaultMutableTreeNode addComponent(UserComponent adding , DefaultMutableTreeNode parentNode){
		DefaultMutableTreeNode componentNode;
		if(parentNode != root)
			componentNode = userTreeComponents.get(adding);
		else{
			componentNode = new DefaultMutableTreeNode(adding.getIDString());
			userTreeComponents.put(adding, componentNode);
		}
		parentNode.add(componentNode);
		DefaultTreeModel tree = (DefaultTreeModel) userTree.getModel();
		tree.reload();
		return componentNode;
	}
	
	public static void updateLastUpdate(String uID){
		idOfLastToUpdate = uID;
	}
	
	/*
	 * ACTION LISTENERS
	 */
	
	private class addUserButtonPressed implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(!componentExists(userID.getText())){
				User newUser = new User(userID.getText());
				addComponent(newUser , root);
				numUsers++;
			}
		}
	}
	
	private class addGroupButtonPressed implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			UserGroup newGroup = new UserGroup(groupID.getText());
			DefaultMutableTreeNode userTreeNode = addComponent(newGroup , root);
			userTreeComponents.put(newGroup, userTreeNode);
			numGroups++;
		}
	}
	
	private class addUserToGroupButtonPressed implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(componentExists(userID.getText()) && getComponentByID(groupID.getText()) != null){
				UserGroup addToGroup = (UserGroup) getComponentByID(groupID.getText());
				User userToAdd = (User)getComponentByID(userID.getText());
				addComponent(userToAdd , userTreeComponents.get(addToGroup));
			}
			else 
				System.out.println("Error: User or group might not exist.");
		}
	}
	
	private class openUserViewButtonPressed implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(componentExists(userID.getText()))
				new UserView((User)getComponentByID(userID.getText()));
			else
				System.out.println("Error: Invalid user window attempt.");
		}
	}
	
	private class userTotalButtonPressed implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			output.setText("Total number of users: " + numUsers);
		}
	}
	
	private class groupTotalButtonPressed implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			output.setText("Total number of groups: " + numGroups);
		}
	}
	
	private class messageTotalButtonPressed implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			output.setText("Total number of tweets: " + totalNumTweets);
		}
	}
	
	private class posMessageTotalButtonPressed implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			output.setText("Percentage of tweets that are positive: " + Math.round(numPosTweets/totalNumTweets * 10000) /100+ "%");
		}
	}
	
	private class idVerificationButtonPressed implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String out;
			if(componentExists(userID.getText()))
				out = "UserID Invalid: A user with this name already exists!";
			else if (userID.getText().contains(" "))
				out = "UserID Invalid: No spaces allowed in user IDs!";
			else
				out = "UserID Valid";
			
			if(componentExists(groupID.getText()))
				out = out.concat("     GroupID Invalid: A group with this name already exists!");
			else if (groupID.getText().contains(" "))
				out = out.concat("     GroupID Invalid: No spaces allowed in user IDs!");
			else
				out = out.concat("     GroupID Valid");
			output.setText(out);
		}
	}
	
	private class latestUpdateButtonPressed implements ActionListener{
		public void actionPerformed(ActionEvent e) {	
			output.setText("The last user to post was: " + idOfLastToUpdate);
		}
	}
}
