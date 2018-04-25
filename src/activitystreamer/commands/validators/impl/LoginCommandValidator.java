package activitystreamer.commands.validators.impl;

import org.apache.commons.lang3.StringUtils;

import activitystreamer.command.Command;
import activitystreamer.commands.exceptions.InvalidCommandException;
import activitystreamer.commands.login.LoginCommand;
import activitystreamer.commands.validators.CommandValidator;

public class LoginCommandValidator implements CommandValidator<LoginCommand> {
	
	@Override
	public void validateCommand(LoginCommand loginCommand) throws InvalidCommandException {
		if (StringUtils.isBlank(loginCommand.getUsername())) {
			throw new InvalidCommandException("Username is required.");
		}
		
		if (StringUtils.isBlank(loginCommand.getSecret()) && !Command.USER_ANONYMOUS.equals(loginCommand.getUsername())) {
			throw new InvalidCommandException("Secret is required.");
		}
	}

}
