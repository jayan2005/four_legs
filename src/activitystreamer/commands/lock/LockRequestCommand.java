package activitystreamer.commands.lock;

import activitystreamer.command.Command;
import activitystreamer.commands.AbstractCommand;

public class LockRequestCommand extends AbstractCommand {
	
	private String username;
	private String secret;

	public LockRequestCommand(String username, String secret) {
		super(Command.Names.LOCK_REQUEST.toString());
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
