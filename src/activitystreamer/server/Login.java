package activitystreamer.server;

import java.util.ArrayList;

import activitystreamer.util.Settings;

public class Login {
	private String username;
	private String secret;
	//private ArrayList<User> userlist;

	public Login(String username, String secret) {
		this.username = username;
		this.secret = secret;
	}
	
	public boolean logUserIn() {
		
		if(isLoginDetailsCorrect()) {
			return true; //login success
		}
		
		if(this.username == "anonymous")
			return true; //login success
		
		return false; //login fail
	}
	
	public boolean isLoginDetailsCorrect() {
		User loginClient = new User();
		for(int i=0; i < Control.getInstance().getRegisteredUserList().size(); i++) {
			loginClient = Control.getInstance().getRegisteredUserList().get(i);
			
			if(loginClient.getUsername().equals(this.username) && loginClient.getSecret().equals(this.secret))
				return true; //login details are correct.
		}
		
		return false; // login details are incorrect
	}
	
	
	
}
