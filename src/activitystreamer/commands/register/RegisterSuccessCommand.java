package activitystreamer.commands.register;

import activitystreamer.command.Command;
import activitystreamer.commands.AbstractCommand;

public class RegisterSuccessCommand extends AbstractCommand{

	private String info;
	
	public RegisterSuccessCommand(String info) {
		super(Command.Names.REGISTER_SUCCESS.toString());
		this.info = info;
	}

	public String getInfo() {
		return info;
	}
	
}
