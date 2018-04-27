package activitystreamer.client.commands.processors;

import activitystreamer.client.ClientSkeleton;
import activitystreamer.commands.misc.RedirectCommand;
import activitystreamer.commands.processors.CommandProcessor;
import activitystreamer.util.Settings;

public class RedirectCommandProcessor implements CommandProcessor<RedirectCommand>{
	
	@Override
	public void processCommand(RedirectCommand command) {
		ClientSkeleton.getInstance().stopMessageListener();
		Settings.setRemoteHostname(command.getHostname());
		Settings.setRemotePort(command.getPort());
		ClientSkeleton.getInstance().sendLoginCommand();
	}
	
}
