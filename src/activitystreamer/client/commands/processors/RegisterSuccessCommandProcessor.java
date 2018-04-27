package activitystreamer.client.commands.processors;

import activitystreamer.client.ClientUIManager;
import activitystreamer.commands.processors.CommandProcessor;
import activitystreamer.commands.register.RegisterSuccessCommand;

public class RegisterSuccessCommandProcessor implements CommandProcessor<RegisterSuccessCommand>{
	
	@Override
	public void processCommand(RegisterSuccessCommand command) {
		ClientUIManager uiManager = ClientUIManager.getInstance();
		uiManager.showDialog(uiManager.getLoginFrame(), command.getInfo());
		uiManager.handleRegisterSuccess();
	}
	
}
