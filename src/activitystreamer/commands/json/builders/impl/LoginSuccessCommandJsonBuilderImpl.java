package activitystreamer.commands.json.builders.impl;

import org.json.simple.JSONObject;

import activitystreamer.commands.login.LoginSuccessCommand;

public class LoginSuccessCommandJsonBuilderImpl extends AbstractCommandJsonBuilder<LoginSuccessCommand> {

	@SuppressWarnings("unchecked")
	@Override
	public void populateJsonObject(JSONObject jsonObject, LoginSuccessCommand aCommand) {
		jsonObject.put("info", aCommand.getInfo());
	}

}

