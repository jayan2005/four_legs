package activitystreamer.commands.json.builders.impl;

import org.json.simple.JSONObject;

import activitystreamer.commands.authenticate.AuthenticateCommand;

public class AuthenticateCommadJsonBuilderImpl extends AbstractCommandJsonBuilder<AuthenticateCommand> {

	@SuppressWarnings("unchecked")
	@Override
	public void populateJsonObject(JSONObject jsonObject, AuthenticateCommand aCommand) {
		jsonObject.put("secret", aCommand.getSecret());
	}

}

