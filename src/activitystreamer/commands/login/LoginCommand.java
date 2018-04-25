package activitystreamer.commands.login;

import activitystreamer.command.Command;
import activitystreamer.commands.AbstractCommand;

public class LoginCommand extends AbstractCommand {
	
	private String username;
	private String secret;

	public LoginCommand(String username, String secret) {
		super(Command.Names.LOGIN.toString());
		this.username = username;
		this.secret = secret;
	}

	public String getUsername() {
		return username;
	}

	public String getSecret() {
		return secret;
	}

}
