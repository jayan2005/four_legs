package activitystreamer.commands.json.builders.impl;

import org.json.simple.JSONObject;

import activitystreamer.commands.register.RegisterCommand;

public class RegisterCommandJsonBuilderImpl extends AbstractCommandJsonBuilder<RegisterCommand> {

	@SuppressWarnings("unchecked")
	@Override
	public void populateJsonObject(JSONObject jsonObject, RegisterCommand aCommand) {
		jsonObject.put("username", aCommand.getUsername());
		jsonObject.put("secret", aCommand.getSecret());
	}

}
