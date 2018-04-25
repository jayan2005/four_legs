package activitystreamer.commands.json.builders.impl;

import org.json.simple.JSONObject;

import activitystreamer.commands.login.LoginFailedCommand;

public class LoginFailedCommandJsonBuilderImpl extends AbstractCommandJsonBuilder<LoginFailedCommand> {

	@SuppressWarnings("unchecked")
	@Override
	public void populateJsonObject(JSONObject jsonObject, LoginFailedCommand aCommand) {
		jsonObject.put("info", aCommand.getInfo());
	}

}

