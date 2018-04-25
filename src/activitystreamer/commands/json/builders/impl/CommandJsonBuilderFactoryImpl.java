package activitystreamer.commands.json.builders.impl;

import java.util.HashMap;

import java.util.Map;

import activitystreamer.command.Command;
import activitystreamer.commands.json.builder.CommandJsonBuilder;
import activitystreamer.commands.json.builder.CommandJsonBuilderFactory;
@SuppressWarnings("rawtypes")
public class CommandJsonBuilderFactoryImpl implements CommandJsonBuilderFactory {

	private static CommandJsonBuilderFactoryImpl instance;

	private Map<String,CommandJsonBuilder> builders;
	
	private CommandJsonBuilderFactoryImpl() {
		builders = new HashMap<String,CommandJsonBuilder>();
		builders.put(Command.Names.REGISTER.toString(), new RegisterCommandJsonBuilderImpl());
		
		builders.put(Command.Names.LOGIN.toString(), new LoginCommandJsonBuilderImpl());
		builders.put(Command.Names.LOGOUT.toString(), new LogoutCommandJsonBuilderImpl());
	}
	
	public static CommandJsonBuilderFactoryImpl getInstance() {
		if (instance == null) {
			instance = new CommandJsonBuilderFactoryImpl();
		}
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Command> CommandJsonBuilder<T> getJsonBuilder(T aCommand) {
		return builders.get(aCommand.getName());
	}

}
