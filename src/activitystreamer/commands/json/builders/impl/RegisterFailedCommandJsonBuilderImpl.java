package activitystreamer.commands.json.builders.impl;

import org.json.simple.JSONObject;

import activitystreamer.commands.register.RegisterFailedCommand;

public class RegisterFailedCommandJsonBuilderImpl extends AbstractCommandJsonBuilder<RegisterFailedCommand> {

	@SuppressWarnings("unchecked")
	@Override
	public void populateJsonObject(JSONObject jsonObject, RegisterFailedCommand aCommand) {
		jsonObject.put("info", aCommand.getInfo());
	}

}
