package activitystreamer.client.commands.processors;

import activitystreamer.client.ClientSkeleton;
import activitystreamer.commands.misc.RedirectCommand;
import activitystreamer.client.commands.processor.CommandProcessor;
import activitystreamer.util.Settings;

public class RedirectCommandProcessor implements CommandProcessor<RedirectCommand>{
	
	@Override
	public void processCommand(RedirectCommand command) {
		ClientSkeleton.getInstance().stopMessageListener();
		ClientSkeleton.getInstance().closeSocket();
		Settings.setRemoteHostname(command.getHostname());
		Settings.setRemotePort(command.getPort());
		Settings.setConnectionRedirect(true);
		ClientSkeleton.getInstance().sendLoginCommand();
	}
	
}
