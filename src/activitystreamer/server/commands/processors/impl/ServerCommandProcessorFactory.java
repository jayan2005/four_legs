package activitystreamer.server.commands.processors.impl;

import java.util.HashMap;
import java.util.Map;

import activitystreamer.command.Command;
import activitystreamer.command.Command.Names;
import activitystreamer.server.commands.processor.CommandProcessor;

@SuppressWarnings("rawtypes")
public class ServerCommandProcessorFactory {

	private static ServerCommandProcessorFactory instance;
	private Map<String, CommandProcessor> processors;

	private ServerCommandProcessorFactory() {
		processors = new HashMap<String, CommandProcessor>();
		
		processors.put(Names.LOGIN.toString(), new LoginCommandProcessor());
	}

	public static ServerCommandProcessorFactory getInstance() {
		if (instance == null) {
			instance = new ServerCommandProcessorFactory();
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	public <T extends Command> CommandProcessor<T> getCommandProcessor(Command aCommand) {
		return processors.get(aCommand.getName());
	}
	
}
