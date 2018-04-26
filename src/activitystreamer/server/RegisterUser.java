package activitystreamer.server;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import activitystreamer.commands.json.builder.CommandJsonBuilder;
import activitystreamer.commands.json.builders.impl.CommandJsonBuilderFactoryImpl;
import activitystreamer.commands.register.RegisterFailedCommand;
import activitystreamer.commands.register.RegisterSuccessCommand;
import activitystreamer.util.Settings;

public class RegisterUser {
	
	private static final Logger log = LogManager.getLogger();
	private String username;
	private String secret;
	private ArrayList<User> userListAL;
	
	public RegisterUser(String username, String secret, ArrayList<User> listOfUsersAL) {
		this.username = username;
		this.secret = secret;
		//userListAL = new ArrayList();
		this.userListAL = listOfUsersAL;
	}
	
	/** Will return true one the user is registered **/
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
	
	/** Checks whether user already registered **/
	public boolean isUserRegistered() {
		User checkUser = new User();
		
		for(int i=0 ; i < Control.getInstance().getRegisteredUserList().size(); i++) {
			checkUser = Control.getInstance().getRegisteredUserList().get(i);
			if(checkUser.getUsername().equals(this.username)) {
				log.debug("Checking registry : User Already exists.");
				return true;
			}
		}
		
		return false;
	} 
	
	/** Returns registered user list **/
	public ArrayList<User> getUpdatedUserList(){
		return this.userListAL;
	}
	
	/** sending REGISTER_SUCCESS command **/
	public void sendRegisterSuccessCommand(Connection con) {
		RegisterSuccessCommand registerSuccessMsg = new RegisterSuccessCommand("Register success for "+ this.username);
		CommandJsonBuilder<RegisterSuccessCommand> registerSuccessCommandJsonBuilder = CommandJsonBuilderFactoryImpl.getInstance()
				.getJsonBuilder(registerSuccessMsg);
		
		JSONObject registerSuccessCommandJsonMsg = registerSuccessCommandJsonBuilder.buildJsonObject(registerSuccessMsg);
		con.writeMsg(registerSuccessCommandJsonMsg.toJSONString());
	}
	
	/** sending REGISTER_FAILED command **/
	public void sendRegisterFailedCommand(Connection con) {
		RegisterFailedCommand registerFailedMsg = new RegisterFailedCommand(this.username + " is already registered with the system");
		CommandJsonBuilder<RegisterFailedCommand> registerFailedCommandJsonBuilder = CommandJsonBuilderFactoryImpl.getInstance()
				.getJsonBuilder(registerFailedMsg);
		
		JSONObject registerFailedCommandJsonMsg = registerFailedCommandJsonBuilder.buildJsonObject(registerFailedMsg);
		con.writeMsg(registerFailedCommandJsonMsg.toJSONString());	
	}
	
	/** HELPER METHOD - Print out all registered user **/
	private void printAllUsers() {
		User printUser = new User();
		if(!this.userListAL.isEmpty()) {
			for(int i=0; i < this.userListAL.size(); i++) {
				printUser = userListAL.get(i);
				log.debug("Registered Username : " + printUser.getUsername());
			}
		}
		
		log.debug("No users resgistered at this time.");
	}

}
