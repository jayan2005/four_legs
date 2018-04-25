package activitystreamer.commands.misc;

import activitystreamer.command.Command;
import activitystreamer.commands.AbstractCommand;

public class RedirectCommand extends AbstractCommand {
	
	private String hostname;
	private int port;

	public RedirectCommand(String hostname, int port) {
		super(Command.Names.REDIRECT.toString());
		this.hostname = hostname;
		this.port = port;
	}

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}
	
}
