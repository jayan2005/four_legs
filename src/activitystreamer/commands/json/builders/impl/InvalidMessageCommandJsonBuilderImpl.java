package activitystreamer.commands.json.builders.impl;

import org.json.simple.JSONObject;

import activitystreamer.commands.misc.InvalidMessageCommand;

public class InvalidMessageCommandJsonBuilderImpl extends AbstractCommandJsonBuilder<InvalidMessageCommand> {

	@SuppressWarnings("unchecked")
	@Override
	public void populateJsonObject(JSONObject jsonObject, InvalidMessageCommand aCommand) {
		jsonObject.put("info", aCommand.getInfo());
	}

}

