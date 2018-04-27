package activitystreamer.commands.builders.impl;

import org.json.simple.JSONObject;

import activitystreamer.commands.activity.ActivityBroadcastCommand;
import activitystreamer.commands.builder.CommandBuilder;

public class ActivityBroadcastCommandBuilderImpl implements CommandBuilder<ActivityBroadcastCommand> {

	@Override
	public ActivityBroadcastCommand buildCommandObject(JSONObject jsonObject) {
		JSONObject activity = (JSONObject) jsonObject.get("activity");
		
		ActivityBroadcastCommand activityBroadcastCommand = new ActivityBroadcastCommand(activity);
		return activityBroadcastCommand;
	}

}
