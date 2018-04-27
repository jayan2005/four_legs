package activitystreamer.client;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import activitystreamer.client.commands.processors.ClientCommandProcessorFactory;
import activitystreamer.command.Command;
import activitystreamer.commands.builder.CommandBuilder;
import activitystreamer.commands.builders.impl.CommandBuilderFactoryImpl;
import activitystreamer.client.commands.processor.CommandProcessor;


@SuppressWarnings("rawtypes")
public class MessageListener extends Thread {

	private BufferedReader reader;
	private static final Logger log = LogManager.getLogger();
	private JSONParser parser;
	private boolean term;
	
	public MessageListener(BufferedReader reader) {
		this.reader = reader;
		this.parser = new JSONParser();
	}
	
	public void close() {
		term = true;
	}
	
	@Override
	public void run() {
		try {
			String data = null;
			while (!term && (data = reader.readLine()) != null) {
				log.debug("Received from server: " + data);
				try {
					JSONObject jsonObject = (JSONObject) parser.parse(data);
					CommandBuilder commandBuilder = CommandBuilderFactoryImpl.getInstance().getCommandBuilder(jsonObject);
					if (commandBuilder != null) {
						// Build the command
						Command aCommand = commandBuilder.buildCommandObject(jsonObject);
						
						// Validate the command
						
						// Process the command
						CommandProcessor<Command> commandProcessor = ClientCommandProcessorFactory.getInstance().getCommandProcessor(aCommand);
						commandProcessor.processCommand(aCommand);
					} else {
						log.info("Unknown message received: " + jsonObject.toJSONString());
					}
				} catch (ParseException e) {
					log.error("Message received is not a JSON.");
				}
			}
		} catch (IOException ioe) {
			log.error(ioe);
		} finally {
			try {
				reader.close();
			} catch (IOException ioe) {
				log.error(ioe);
			}
		}
	}
}
