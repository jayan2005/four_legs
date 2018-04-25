package activitystreamer.commands.misc;

import activitystreamer.command.Command;
import activitystreamer.commands.AbstractCommand;

public class LogoutCommand extends AbstractCommand {

	public LogoutCommand() {
		super(Command.Names.LOGOUT.toString());
	}

}
