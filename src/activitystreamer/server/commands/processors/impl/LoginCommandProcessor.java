package activitystreamer.server.commands.processors.impl;

import activitystreamer.command.Command;
import activitystreamer.commands.login.LoginCommand;
import activitystreamer.commands.login.LoginFailedCommand;
import activitystreamer.commands.login.LoginSuccessCommand;
import activitystreamer.server.UserServiceImpl;
import activitystreamer.server.commands.processor.CommandProcessor;

public class LoginCommandProcessor implements CommandProcessor<LoginCommand> {

	@Override
	public Command processCommand(LoginCommand command) {
		boolean loginSuccess = false;
		if ("anonymous".equals(command.getUsername())) {
			loginSuccess = true;
		} else {
			loginSuccess = UserServiceImpl.getInstance().login(command.getUsername(), command.getSecret());
		}
		
		if (loginSuccess) {
			return prepareLoginSuccessCommand("logged in as user "+command.getUsername());
		} else {
			return prepareLoginFailedCommand("attempt to login with wrong secret");
		}
	}

	private LoginFailedCommand prepareLoginFailedCommand(String message) {
		LoginFailedCommand loginFailedCommand = new LoginFailedCommand(message);
		return loginFailedCommand;
	}

	private LoginSuccessCommand prepareLoginSuccessCommand(String message) {
		LoginSuccessCommand loginSuccessCommand = new LoginSuccessCommand(message);
		return loginSuccessCommand;
	}

}
