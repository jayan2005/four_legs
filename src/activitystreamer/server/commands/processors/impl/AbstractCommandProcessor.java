package activitystreamer.server.commands.processors.impl;

import activitystreamer.command.Command;
import activitystreamer.commands.processors.CommandProcessor;

public class AbstractCommandProcessor<T extends Command> implements CommandProcessor<T> {

	@Override
	public void processCommand(T command) {
		
	}

    protected void doProcessCommand(T command) {
    	
    }
	
}
