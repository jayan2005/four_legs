package activitystreamer.client.commands.processors;

import activitystreamer.client.ClientSkeleton;
import activitystreamer.client.ClientUIManager;
import activitystreamer.commands.processors.CommandProcessor;
import activitystreamer.commands.register.RegisterFailedCommand;

public class RegisterFailedCommandProcessor implements CommandProcessor<RegisterFailedCommand> {

	@Override
	public void processCommand(RegisterFailedCommand command) {
		ClientSkeleton.getInstance().stopMessageListener();
		ClientUIManager uiManager = ClientUIManager.getInstance();
		uiManager.showDialog(uiManager.getLoginFrame(), command.getInfo());
	}

}
