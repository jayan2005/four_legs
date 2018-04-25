package activitystreamer.commands.json.builder;

import org.json.simple.JSONObject;

import activitystreamer.command.Command;

public interface CommandJsonBuilder<T extends Command> {

	JSONObject buildJsonObject(T aCommand);
	
}
