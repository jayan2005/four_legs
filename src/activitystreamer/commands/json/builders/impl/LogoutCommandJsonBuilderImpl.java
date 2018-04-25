package activitystreamer.commands.json.builders.impl;

import org.json.simple.JSONObject;

import activitystreamer.commands.misc.LogoutCommand;

public class LogoutCommandJsonBuilderImpl extends AbstractCommandJsonBuilder<LogoutCommand> {

	@Override
	public void populateJsonObject(JSONObject jsonObject, LogoutCommand aCommand) {
		// do Nothing as no additional properties required
	}

}
