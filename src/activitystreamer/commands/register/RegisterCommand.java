package activitystreamer.commands.register;

import activitystreamer.command.Command;
import activitystreamer.commands.AbstractCommand;

public class RegisterCommand extends AbstractCommand {

	private String username;
	private String secret;
	
	public RegisterCommand(String username, String secret) {
		super(Command.Names.REGISTER.toString());
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
