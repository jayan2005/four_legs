package activitystreamer.commands.json.builders.impl;

import org.json.simple.JSONObject;

import activitystreamer.commands.register.RegisterSuccessCommand;

public class RegisterSuccessCommandJsonBuilderImpl extends AbstractCommandJsonBuilder<RegisterSuccessCommand> {

	@SuppressWarnings("unchecked")
	@Override
	public void populateJsonObject(JSONObject jsonObject, RegisterSuccessCommand aCommand) {
		jsonObject.put("info", aCommand.getInfo());
	}

}
