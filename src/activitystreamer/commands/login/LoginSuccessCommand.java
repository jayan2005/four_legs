package activitystreamer.commands.login;

import activitystreamer.command.Command;
import activitystreamer.commands.AbstractCommand;

public class LoginSuccessCommand extends AbstractCommand {

	private String info;

	public LoginSuccessCommand(String info) {
		super(Command.Names.LOGIN_SUCCESS.toString());
		this.info = info;
	}

	public String getInfo() {
		return info;
	}

}
