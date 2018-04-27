package activitystreamer.commands.builders.impl;

import org.json.simple.JSONObject;

import activitystreamer.commands.builder.CommandBuilder;
import activitystreamer.commands.login.LoginCommand;

public class LoginCommandBuilderImpl implements CommandBuilder<LoginCommand> {

	@Override
	public LoginCommand buildCommandObject(JSONObject jsonObject) {
		String username = (String) jsonObject.get("username");
		String secret = (String) jsonObject.get("secret");
		
		LoginCommand loginCommand = new LoginCommand(username, secret);
		return loginCommand;
	}

}
