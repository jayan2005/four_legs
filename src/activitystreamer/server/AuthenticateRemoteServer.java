package activitystreamer.server;

import org.json.simple.JSONObject;

import activitystreamer.commands.authenticate.AuthenticationFailCommand;
import activitystreamer.commands.json.builder.CommandJsonBuilder;
import activitystreamer.commands.json.builders.impl.CommandJsonBuilderFactoryImpl;
import activitystreamer.commands.login.LoginFailedCommand;
import activitystreamer.util.Settings;

public class AuthenticateRemoteServer {
	
	private String server_secret = "jsdfkjhaksjdhcmsdmfsdf";
	private String received_secret;

	public AuthenticateRemoteServer(String secret) {
		this.received_secret = secret;
	}
	
	/** Checking secret sent by remote server is correct **/
	public boolean isSecretCorrect() {
		if(this.received_secret.equals(server_secret))
			return true;
		
		return false;
	}
	
	/** AUTHENTICATION_FAIL command handling **/
	public void sendAuthenticationFailCommand(Connection con) {
		
		AuthenticationFailCommand authenticationFailCommandMsg = new AuthenticationFailCommand("The supplied secret is incorrect : " + received_secret);
		CommandJsonBuilder<AuthenticationFailCommand> authenticationFailCommandJsonBuilder = CommandJsonBuilderFactoryImpl
				.getInstance().getJsonBuilder(authenticationFailCommandMsg);

		JSONObject authenticationFailCommandJsonMsg = authenticationFailCommandJsonBuilder.buildJsonObject(authenticationFailCommandMsg);
		if (con.isOpen())
			con.writeMsg(authenticationFailCommandJsonMsg.toJSONString());
	}
	
	
}
