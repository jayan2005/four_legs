package activitystreamer.commands.builder;

import org.json.simple.JSONObject;

import activitystreamer.command.Command;

public interface CommandBuilder<T extends Command> {

	T buildCommandObject(JSONObject jsonObject);
	
}
