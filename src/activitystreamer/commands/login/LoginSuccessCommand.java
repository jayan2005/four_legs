package activitystreamer.commands.login;

import activitystreamer.command.Command;
import activitystreamer.commands.AbstractCommand;
import activitystreamer.commands.ResultCommand;

public class LoginSuccessCommand extends AbstractCommand implements ResultCommand {

	private String info;

	public LoginSuccessCommand(String info) {
		super(Command.Names.LOGIN_SUCCESS.toString());
		this.info = info;
	}

	@Override
	public String getInfo() {
		return info;
	}

}
