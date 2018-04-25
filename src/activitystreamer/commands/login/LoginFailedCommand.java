package activitystreamer.commands.login;

import activitystreamer.command.Command;
import activitystreamer.commands.AbstractCommand;

public class LoginFailedCommand extends AbstractCommand {

	private String info;
	
	public LoginFailedCommand(String info) {
		super(Command.Names.LOGIN_FAILED.toString());
	}

	public String getInfo() {
		return info;
	}
}
