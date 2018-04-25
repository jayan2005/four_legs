package activitystreamer.commands.validators;

import activitystreamer.command.Command;
import activitystreamer.commands.exceptions.InvalidCommandException;

public interface CommandValidator<T extends Command> {

	void validateCommand(T aCommand) throws InvalidCommandException;
	
}
