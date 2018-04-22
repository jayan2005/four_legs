package activitystreamer.commands.authenticate;

import activitystreamer.command.Command;
import activitystreamer.commands.AbstractCommand;

public class AuthenticationFailCommand extends AbstractCommand {
	
	private String info;

	public AuthenticationFailCommand(String info) {
		super(Command.Names.AUTHTENTICATION_FAIL.toString());
		this.info = info;
	}
	
	public String getInfo() {
		return info;
	}

}
