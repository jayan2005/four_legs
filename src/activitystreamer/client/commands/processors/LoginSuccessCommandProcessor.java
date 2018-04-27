package activitystreamer.client.commands.processors;

import activitystreamer.client.ClientUIManager;
import activitystreamer.commands.login.LoginSuccessCommand;
import activitystreamer.commands.processors.CommandProcessor;

public class LoginSuccessCommandProcessor implements CommandProcessor<LoginSuccessCommand>{
	
	@Override
	public void processCommand(LoginSuccessCommand loginSuccessCommand) {
		ClientUIManager uiManager = ClientUIManager.getInstance();
		uiManager.showDialog(uiManager.getLoginFrame(), loginSuccessCommand.getInfo());
		uiManager.handleLoginSuccess();
	}
	
}
