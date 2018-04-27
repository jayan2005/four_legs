package activitystreamer.client.commands.processor;

import activitystreamer.command.Command;

public interface CommandProcessor<T extends Command> {

	void processCommand(T command);
	
}
