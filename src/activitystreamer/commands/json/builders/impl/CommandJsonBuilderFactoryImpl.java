package activitystreamer.commands.json.builders.impl;

import java.util.HashMap;

import java.util.Map;

import activitystreamer.command.Command;
import activitystreamer.commands.json.builder.CommandJsonBuilder;
import activitystreamer.commands.json.builder.CommandJsonBuilderFactory;
import activitystreamer.commands.login.LoginSuccessCommand;
@SuppressWarnings("rawtypes")
public class CommandJsonBuilderFactoryImpl implements CommandJsonBuilderFactory {

	private static CommandJsonBuilderFactoryImpl instance;

	private Map<String,CommandJsonBuilder> builders;
	
	private CommandJsonBuilderFactoryImpl() {
		builders = new HashMap<String,CommandJsonBuilder>();
		builders.put(Command.Names.REGISTER.toString(), new RegisterCommandJsonBuilderImpl());
		builders.put(Command.Names.REGISTER_SUCCESS.toString(), new RegisterSuccessCommandJsonBuilderImpl());
		builders.put(Command.Names.REGISTER_FAILED.toString(), new RegisterFailedCommandJsonBuilderImpl());
		
		builders.put(Command.Names.LOGIN.toString(), new LoginCommandJsonBuilderImpl());
		builders.put(Command.Names.LOGIN_SUCCESS.toString(), new LoginSuccessCommandJsonBuilderImpl());
		builders.put(Command.Names.LOGIN_FAILED.toString(), new LoginFailedCommandJsonBuilderImpl());
		builders.put(Command.Names.LOGOUT.toString(), new LogoutCommandJsonBuilderImpl());
		
		builders.put(Command.Names.AUTHENTICATE.toString(), new AuthenticateCommadJsonBuilderImpl());
		builders.put(Command.Names.AUTHTENTICATION_FAIL.toString(), new AuthenticationFailCommandJsonBuilderImpl());
		
		builders.put(Command.Names.INVALID_MESSAGE.toString(), new InvalidMessageCommandJsonBuilderImpl());
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
