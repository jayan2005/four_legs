package activitystreamer.client.commands.processors;

import activitystreamer.client.ClientSkeleton;
import activitystreamer.client.ClientUIManager;
import activitystreamer.client.commands.processor.CommandProcessor;
import activitystreamer.commands.misc.InvalidMessageCommand;

public class InvalidMessageCommandProcessor implements CommandProcessor<InvalidMessageCommand> {

	@Override
	public void processCommand(InvalidMessageCommand command) {
		ClientSkeleton.getInstance().stopMessageListener();
		ClientUIManager uiManager = ClientUIManager.getInstance();
		uiManager.showMessageAndCloseTextFrame(command.getInfo());
	}

}
