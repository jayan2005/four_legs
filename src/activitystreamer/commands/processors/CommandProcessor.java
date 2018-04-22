package activitystreamer.commands.processors;

import activitystreamer.command.Command;

public interface CommandProcessor<T extends Command> {

	void processCommand(T command);
	
}
