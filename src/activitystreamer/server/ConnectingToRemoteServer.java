package activitystreamer.server;

import activitystreamer.util.Settings;

public class ConnectingToRemoteServer {
	
	private String remote_server_secret = "gen1p85md2qnq0d59qll3fbcoa";
	private String remote_server_name;
	private int remote_server_port;

	public ConnectingToRemoteServer() {
		this.remote_server_name = Settings.getRemoteHostname();
		this.remote_server_port = Settings.getRemotePort();
	}
	
	
}
