package activitystreamer.commands.json.builder;

import activitystreamer.command.Command;

public interface CommandJsonBuilderFactory {

	<T extends Command> CommandJsonBuilder<T> getJsonBuilder(T aCommand);
	
}
