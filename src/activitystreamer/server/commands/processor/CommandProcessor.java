package activitystreamer.server.commands.processor;

import activitystreamer.command.Command;

public interface CommandProcessor<T extends Command> {

	Command processCommand(T command);
	
}
