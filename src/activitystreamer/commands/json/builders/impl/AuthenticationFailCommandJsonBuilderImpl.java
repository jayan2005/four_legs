package activitystreamer.commands.json.builders.impl;

import org.json.simple.JSONObject;

import activitystreamer.commands.authenticate.AuthenticationFailCommand;

public class AuthenticationFailCommandJsonBuilderImpl extends AbstractCommandJsonBuilder<AuthenticationFailCommand> {

	@SuppressWarnings("unchecked")
	@Override
	public void populateJsonObject(JSONObject jsonObject, AuthenticationFailCommand aCommand) {
		jsonObject.put("info", aCommand.getInfo());
	}

}

