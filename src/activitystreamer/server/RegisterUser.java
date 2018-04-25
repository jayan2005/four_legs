package activitystreamer.server;

import java.util.ArrayList;

public class RegisterUser {
	
	private String username;
	private String secret;
	private ArrayList<User> userListAL;
	
	public RegisterUser(String username, String secret, ArrayList<User> listOfUsersAL) {
		this.username = username;
		this.secret = secret;
		//userListAL = new ArrayList();
		this.userListAL = listOfUsersAL;
	}
	
	/** Registering new user **/
	public boolean addUser() {
		
		if(!isUserRegistered()) {
			User newUser = new User();
			newUser.setUsername(this.username);
			newUser.setSecret(this.secret);
			
			userListAL.add(newUser);
			//printAllUsers();
			return true;
		} 
		
		return false;
	}
	
	/** Checks whether user is already registered **/
	public boolean isUserRegistered() {
		User checkUser = new User();
		
		for(int i=0 ; i < userListAL.size(); i++) {
			checkUser = userListAL.get(i);
			if(checkUser.getUsername().equals(this.username)) {
				System.out.print("** Checking registry : User found **");
				return true;
			}
		}
		
		return false;
	} 
	
	/** Returns current registered user list **/
	public ArrayList<User> getUpdatedUserList(){
		return this.userListAL;
	}
	
	/** HELPER METHOD - Print out all registered user **/
	private void printAllUsers() {
		User printUser = new User();
		if(!this.userListAL.isEmpty()) {
			for(int i=0; i < this.userListAL.size(); i++) {
				printUser = userListAL.get(i);
				System.out.println("Registered Username : " + printUser.getUsername());
			}
		}
		
		System.out.println("No users resgistered at this time.");
	}

}
