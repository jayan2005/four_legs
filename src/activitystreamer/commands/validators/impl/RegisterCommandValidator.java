package activitystreamer.commands.validators.impl;

import org.apache.commons.lang3.StringUtils;

import activitystreamer.commands.exceptions.InvalidCommandException;
import activitystreamer.commands.register.RegisterCommand;
import activitystreamer.commands.validators.CommandValidator;

public class RegisterCommandValidator implements CommandValidator<RegisterCommand> {

	@Override
	public void validateCommand(RegisterCommand registerCommand) throws InvalidCommandException {
		if (StringUtils.isBlank(registerCommand.getUsername())) {
			throw new InvalidCommandException("Username is required.");
		}
		
		if (StringUtils.isBlank(registerCommand.getSecret())) {
			throw new InvalidCommandException("Secret is required.");
		}
	}
}
