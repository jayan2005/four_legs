package activitystreamer.commands.builders.impl;

import org.json.simple.JSONObject;

import activitystreamer.commands.builder.CommandBuilder;
import activitystreamer.commands.register.RegisterFailedCommand;

public class RegisterFailedCommandBuilderImpl implements CommandBuilder<RegisterFailedCommand> {

	@Override
	public RegisterFailedCommand buildCommandObject(JSONObject jsonObject) {
		String info = (String) jsonObject.get("info");
		
		RegisterFailedCommand registerFailedCommand = new RegisterFailedCommand(info);
		return registerFailedCommand;
	}

}
