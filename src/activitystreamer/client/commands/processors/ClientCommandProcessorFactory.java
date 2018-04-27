package activitystreamer.client.commands.processors;

import java.util.HashMap;
import java.util.Map;

import activitystreamer.command.Command;
import activitystreamer.command.Command.Names;
import activitystreamer.client.commands.processor.CommandProcessor;

@SuppressWarnings("rawtypes")
public class ClientCommandProcessorFactory {

	private static ClientCommandProcessorFactory instance;
	private Map<String, CommandProcessor> processors;

	private ClientCommandProcessorFactory() {
		processors = new HashMap<String, CommandProcessor>();
		processors.put(Names.REGISTER_SUCCESS.toString(), new RegisterSuccessCommandProcessor());
		processors.put(Names.REGISTER_FAILED.toString(), new RegisterFailedCommandProcessor());
		
		processors.put(Names.LOGIN_SUCCESS.toString(), new LoginSuccessCommandProcessor());
		processors.put(Names.LOGIN_FAILED.toString(), new LoginFailedCommandProcessor());
		
		processors.put(Names.REDIRECT.toString(), new RedirectCommandProcessor());
		
		processors.put(Names.ACTIVITY_BROADCAST.toString(), new ActivityBroadcastCommandProcessor());
		processors.put(Names.INVALID_MESSAGE.toString(), new InvalidMessageCommandProcessor());
	}

	public static ClientCommandProcessorFactory getInstance() {
		if (instance == null) {
			instance = new ClientCommandProcessorFactory();
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	public <T extends Command> CommandProcessor<T> getCommandProcessor(Command aCommand) {
		return processors.get(aCommand.getName());
	}
	
}
