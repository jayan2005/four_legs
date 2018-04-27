package activitystreamer.server;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import activitystreamer.commands.authenticate.AuthenticationFailCommand;
import activitystreamer.commands.json.builder.CommandJsonBuilder;
import activitystreamer.commands.json.builders.impl.CommandJsonBuilderFactoryImpl;
import activitystreamer.commands.login.LoginFailedCommand;
import activitystreamer.commands.login.LoginSuccessCommand;
import activitystreamer.util.Settings;

public class Login {
	private static final Logger log = LogManager.getLogger();
	private String username;
	private String secret;

	public Login(String username, String secret) {
		this.username = username;
		this.secret = secret;
	}
	
	public boolean logUserIn() {
		
		if(isLoginDetailsCorrect()) {
			return true; //login success
		}
		
		if(this.username.equals("anonymous"))
			return true; //login success
		
		return false; //login fail
	}
	
	//Matching username and secret to verify login details
	public boolean isLoginDetailsCorrect() {
		User loginClient = new User();
		for(int i=0; i < Control.getInstance().getRegisteredUserList().size(); i++) {
			loginClient = Control.getInstance().getRegisteredUserList().get(i);
			
			if(loginClient.getUsername().equals(this.username) && loginClient.getSecret().equals(this.secret))
				return true; //login details are correct.
		}
		
		return false; // login details are incorrect
	}
	
	/** sending LOGIN_SUCCESS command **/
	public void sendLoginSuccessCommand(Connection con) {
		LoginSuccessCommand loginSuccessCommandMsg = new LoginSuccessCommand("Logged in as user " + this.username);
		CommandJsonBuilder<LoginSuccessCommand> loginSuccessCommandJsonBuilder = CommandJsonBuilderFactoryImpl
				.getInstance().getJsonBuilder(loginSuccessCommandMsg);

		JSONObject loginSuccessCommandJsonMsg = loginSuccessCommandJsonBuilder
				.buildJsonObject(loginSuccessCommandMsg);
		con.writeMsg(loginSuccessCommandJsonMsg.toJSONString());
	}
	
	/** sending LOGIN_FAIL command **/
	public void sendLoginFailCommand(Connection con) {
		
		LoginFailedCommand loginFailedCommandMsg = new LoginFailedCommand("Attempt to login with wrong secret");
		CommandJsonBuilder<LoginFailedCommand> loginFailedCommandJsonBuilder = CommandJsonBuilderFactoryImpl
				.getInstance().getJsonBuilder(loginFailedCommandMsg);

		JSONObject loginFailedCommandJsonMsg = loginFailedCommandJsonBuilder.buildJsonObject(loginFailedCommandMsg);
		con.writeMsg(loginFailedCommandJsonMsg.toJSONString());
	}
	
	/** sending AUTHENTICATION_FAIL command for ACTIVITY_MESSAGE **/
	public boolean sendAuthenticationFailCommand(Connection con) {
		
		AuthenticationFailCommand authenticationFailCommandMsg = new AuthenticationFailCommand("The supplied secret is incorrect : " + this.secret);
		CommandJsonBuilder<AuthenticationFailCommand> authenticationFailCommandJsonBuilder = CommandJsonBuilderFactoryImpl
				.getInstance().getJsonBuilder(authenticationFailCommandMsg);

		JSONObject authenticationFailCommandJsonMsg = authenticationFailCommandJsonBuilder.buildJsonObject(authenticationFailCommandMsg);
		log.debug("Command::"+authenticationFailCommandJsonMsg);
		con.writeMsg(authenticationFailCommandJsonMsg.toJSONString());
		return true;
	}
	
}
