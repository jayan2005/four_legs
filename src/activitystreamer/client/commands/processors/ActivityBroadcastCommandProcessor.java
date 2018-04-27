package activitystreamer.client.commands.processors;

import activitystreamer.client.ClientUIManager;
import activitystreamer.commands.activity.ActivityBroadcastCommand;
import activitystreamer.client.commands.processor.CommandProcessor;

public class ActivityBroadcastCommandProcessor implements CommandProcessor<ActivityBroadcastCommand>{
	
	@Override
	public void processCommand(ActivityBroadcastCommand command) {
		ClientUIManager uiManager = ClientUIManager.getInstance();
		uiManager.updateTextFrame(command.getActivity());
	}
	
}
