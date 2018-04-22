package activitystreamer.commands.activity;

import org.json.simple.JSONObject;

import activitystreamer.command.Command;
import activitystreamer.commands.AbstractCommand;

public class ActivityBroadcastCommand extends AbstractCommand {
	
	private JSONObject activity;

	public ActivityBroadcastCommand(JSONObject activity) {
		super(Command.Names.ACTIVITY_BROADCAST.toString());
		this.activity = activity;
	}

	public JSONObject getActivity() {
		return activity;
	}
	
}
