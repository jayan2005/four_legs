package activitystreamer.server;

import java.util.ArrayList;

public class Login {
	private String username;
	private String secret;
	//private ArrayList<User> userlist;

	public Login(String username, String secret) {
		this.username = username;
		this.secret = secret; 
	}
	
	public boolean logUserIn() {
		User loginUser = new User();
		for(int i=0; i < Control.getInstance().getRegisteredUserList().size(); i++) {
			loginUser = Control.getInstance().getRegisteredUserList().get(i);
			
			if(loginUser.getUsername().equals(this.username) && loginUser.getSecret().equals(this.secret))
				return true;
		}
		
		return false;
	}
	
	
	
}
