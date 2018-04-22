package activitystreamer.commands;

import activitystreamer.command.Command;

public abstract class AbstractCommand implements Command {

	private String name;
	
	public AbstractCommand(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
