package activitystreamer.commands.authenticate;

import activitystreamer.command.Command;
import activitystreamer.commands.AbstractCommand;

public class AuthenticateCommand extends AbstractCommand {

	private String secret;
	
	public AuthenticateCommand(String secret) {
		super(Command.Names.AUTHENTICATE.toString());
		this.secret = secret;
	}

	public String getSecret() {
		return secret;
	}
	
}
