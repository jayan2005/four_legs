package activitystreamer.commands.json.builders.impl;

import org.json.simple.JSONObject;

import activitystreamer.command.Command;
import activitystreamer.commands.json.builder.CommandJsonBuilder;

public abstract class AbstractCommandJsonBuilder<T extends Command> implements CommandJsonBuilder<T> {

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject buildJsonObject(T aCommand) {
		JSONObject commandJsonObject = new JSONObject();
		commandJsonObject.put("command", aCommand.getName());
		populateJsonObject(commandJsonObject,aCommand);
		return commandJsonObject;
	}

	protected abstract void populateJsonObject(JSONObject jsonObject, T aCommand);

}
