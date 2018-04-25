package activitystreamer.commands.json.builders.impl;

import org.json.simple.JSONObject;

import activitystreamer.commands.login.LoginCommand;

public class LoginCommandJsonBuilderImpl extends AbstractCommandJsonBuilder<LoginCommand> {

	@SuppressWarnings("unchecked")
	@Override
	public void populateJsonObject(JSONObject jsonObject, LoginCommand aCommand) {
		jsonObject.put("username", aCommand.getUsername());
		jsonObject.put("secret", aCommand.getSecret());
	}

}
