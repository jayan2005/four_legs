package activitystreamer.commands.misc;

import activitystreamer.command.Command;
import activitystreamer.commands.AbstractCommand;

public class InvalidMessageCommand extends AbstractCommand{

	private String info;
	
	public InvalidMessageCommand(String info) {
		super(Command.Names.INVALID_MESSAGE.toString());
		this.info = info;
	}

	public String getInfo() {
		return info;
	}
	
}
