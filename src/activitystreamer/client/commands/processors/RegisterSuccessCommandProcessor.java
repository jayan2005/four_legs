package activitystreamer.client.commands.processors;

import activitystreamer.client.ClientUIManager;
import activitystreamer.commands.register.RegisterSuccessCommand;
import activitystreamer.client.commands.processor.CommandProcessor;

public class RegisterSuccessCommandProcessor implements CommandProcessor<RegisterSuccessCommand>{
	
	@Override
	public void processCommand(RegisterSuccessCommand command) {
		ClientUIManager uiManager = ClientUIManager.getInstance();
		uiManager.showDialog(uiManager.getLoginFrame(), command.getInfo());
		uiManager.handleRegisterSuccess();
	}
	
}
