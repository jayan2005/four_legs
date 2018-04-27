package activitystreamer.commands.activity;

import org.json.simple.JSONObject;

import activitystreamer.command.Command;
import activitystreamer.commands.AbstractCommand;

public class ActivityMessageCommand extends AbstractCommand {

	private String username;
	private String secret;
	private JSONObject activity;
	
	public ActivityMessageCommand(String username, String secret, JSONObject activity) {
		super(Command.Names.ACTIVITY_MESSAGE.toString());
		this.secret = secret;
		this.username = username;
		this.activity = activity;
	}

	public String getSecret() {
		return secret;
	}
	
	public String getUsername() {
		return username;
	}

	public JSONObject getActivity() {
		return activity;
	}
	
}
