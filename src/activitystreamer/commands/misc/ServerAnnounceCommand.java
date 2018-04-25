package activitystreamer.commands.misc;

import activitystreamer.command.Command;
import activitystreamer.commands.AbstractCommand;

public class ServerAnnounceCommand extends AbstractCommand {

	private String id;
	private int load;
	private String hostname;
	private int port;
	
	public ServerAnnounceCommand(String id, int load, String hostname, int port) {
		super(Command.Names.SERVER_ANNOUNCE.toString());
		this.id = id;
		this.load = load;
		this.hostname = hostname;
		this.port = port;
	}

	public String getId() {
		return id;
	}

	public int getLoad() {
		return load;
	}

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}
	
}
