package activitystreamer.commands.register;

import activitystreamer.command.Command;
import activitystreamer.commands.AbstractCommand;

public class RegisterFailedCommand extends AbstractCommand{

	private String info;
	
	public RegisterFailedCommand(String info) {
		super(Command.Names.REGISTER_FAILED.toString());
		this.info = info;
	}

	public String getInfo() {
		return info;
	}
	
}
