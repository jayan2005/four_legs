package activitystreamer.client.commands.processors;

import activitystreamer.client.ClientSkeleton;
import activitystreamer.client.ClientUIManager;
import activitystreamer.commands.login.LoginFailedCommand;
import activitystreamer.client.commands.processor.CommandProcessor;

public class LoginFailedCommandProcessor implements CommandProcessor<LoginFailedCommand> {

	@Override
	public void processCommand(LoginFailedCommand command) {
		ClientSkeleton.getInstance().stopMessageListener();
		ClientUIManager uiManager = ClientUIManager.getInstance();
		uiManager.showDialog(uiManager.getLoginFrame(), command.getInfo());
	}

}
