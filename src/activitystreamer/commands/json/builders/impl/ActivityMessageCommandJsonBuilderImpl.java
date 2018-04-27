package activitystreamer.commands.json.builders.impl;

import org.json.simple.JSONObject;

import activitystreamer.commands.activity.ActivityMessageCommand;

public class ActivityMessageCommandJsonBuilderImpl extends AbstractCommandJsonBuilder<ActivityMessageCommand>{

	@SuppressWarnings("unchecked")
	@Override
	public void populateJsonObject(JSONObject jsonObject,ActivityMessageCommand aCommand) {
		jsonObject.put("username", aCommand.getUsername());
		jsonObject.put("secret", aCommand.getSecret());
		jsonObject.put("activity", aCommand.getActivity());
	}

}
