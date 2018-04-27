package activitystreamer.commands.login;

import activitystreamer.command.Command;
import activitystreamer.commands.AbstractCommand;
import activitystreamer.commands.ResultCommand;

public class LoginFailedCommand extends AbstractCommand implements ResultCommand {

	private String info;
	
	public LoginFailedCommand(String info) {
		super(Command.Names.LOGIN_FAILED.toString());
		this.info = info;
	}

	@Override
	public String getInfo() {
		return info;
	}
	
	@Override
	public boolean isFailure() {
		return true;
	}
}

