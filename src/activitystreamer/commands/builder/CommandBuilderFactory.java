package activitystreamer.commands.builder;

import org.json.simple.JSONObject;

import activitystreamer.command.Command;

public interface CommandBuilderFactory {

	<T extends Command> CommandBuilder<T> getCommandBuilder(JSONObject jsonObject);
	
}
