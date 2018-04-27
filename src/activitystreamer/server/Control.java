package activitystreamer.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import activitystreamer.command.Command;
import activitystreamer.commands.builder.CommandBuilder;
import activitystreamer.commands.builders.impl.CommandBuilderFactoryImpl;
import activitystreamer.commands.json.builder.CommandJsonBuilder;
import activitystreamer.commands.json.builders.impl.CommandJsonBuilderFactoryImpl;
import activitystreamer.commands.misc.InvalidMessageCommand;
import activitystreamer.server.commands.processor.CommandProcessor;
import activitystreamer.server.commands.processors.impl.ServerCommandProcessorFactory;
import activitystreamer.util.Settings;

public class Control extends Thread {
	private static final Logger log = LogManager.getLogger();
	private static ArrayList<Connection> connections;
	private static boolean term=false;
	private static Listener listener;
	
	protected static Control control = null;
	private JSONParser jsonParser;
	
	private List<User> users;
	
	public static Control getInstance() {
		if(control==null){
			control=new Control();
		} 
		return control;
	}
	
	public Control() {
		// initialize the connections array
		connections = new ArrayList<Connection>();
		jsonParser = new JSONParser();
		users = new ArrayList<User>();
		// start a listener
		try {
			listener = new Listener();
		} catch (IOException e1) {
			log.fatal("failed to startup a listening thread: "+e1);
			System.exit(-1);
		}	
	}
	
	public void initiateConnection(){
		// make a connection to another server if remote hostname is supplied
		if(Settings.getRemoteHostname()!=null){
			try {
				outgoingConnection(new Socket(Settings.getRemoteHostname(),Settings.getRemotePort()));
			} catch (IOException e) {
				log.error("failed to make connection to "+Settings.getRemoteHostname()+":"+Settings.getRemotePort()+" :"+e);
				System.exit(-1);
			}
		}
	}
	
	/*
	 * Processing incoming messages from the connection.
	 * Return true if the connection should close.
	 */
	@SuppressWarnings("rawtypes")
	public synchronized boolean process(Connection con,String msg){
		try {
			JSONObject jsonObject = (JSONObject) jsonParser.parse(msg);
			CommandBuilder commandBuilder = CommandBuilderFactoryImpl.getInstance().getCommandBuilder(jsonObject);
			if (commandBuilder != null) {
				// Build the command
				Command aCommand = commandBuilder.buildCommandObject(jsonObject);
				
				// Validate the command
				
				// Process the command
				CommandProcessor<Command> commandProcessor = ServerCommandProcessorFactory.getInstance().getCommandProcessor(aCommand);
				if (commandProcessor == null) {
					sendInvalidMessage(con, "Unknown command received.");
					return true;
				}

				Command result = commandProcessor.processCommand(aCommand);
				
				// Send the response command
				return sendResponse(con, result);
			} else {
				sendInvalidMessage(con,"Unknown command received.");
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			sendInvalidMessage(con,"Message is not a valid json");
			return true;
		}
	}

	private boolean sendResponse(Connection con, Command result) {
		CommandJsonBuilder<Command> commandJsonBuilder = CommandJsonBuilderFactoryImpl.getInstance().getJsonBuilder(result);
		JSONObject responseJson = commandJsonBuilder.buildJsonObject(result);
		
		con.writeMsg(responseJson.toJSONString());
		
		if (result.isFailure()) {
			return true;
		}
		
		return false;
	}
	
	private void sendInvalidMessage(Connection con,String message) {
		InvalidMessageCommand invalidMessageCommand = new InvalidMessageCommand(message);
		sendResponse(con, invalidMessageCommand);
	}

	/*
	 * The connection has been closed by the other party.
	 */
	public synchronized void connectionClosed(Connection con){
		if(!term) connections.remove(con);
	}
	
	/*
	 * A new incoming connection has been established, and a reference is returned to it
	 */
	public synchronized Connection incomingConnection(Socket s) throws IOException{
		log.debug("incomming connection: "+Settings.socketAddress(s));
		Connection c = new Connection(s);
		connections.add(c);
		return c;
		
	}
	
	/*
	 * A new outgoing connection has been established, and a reference is returned to it
	 */
	public synchronized Connection outgoingConnection(Socket s) throws IOException{
		log.debug("outgoing connection: "+Settings.socketAddress(s));
		Connection c = new Connection(s);
		connections.add(c);
		return c;
		
	}
	
	@Override
	public void run(){
		log.info("using activity interval of "+Settings.getActivityInterval()+" milliseconds");
		while(!term){
			// do something with 5 second intervals in between
			try {
				Thread.sleep(Settings.getActivityInterval());
			} catch (InterruptedException e) {
				log.info("received an interrupt, system is shutting down");
				break;
			}
			if(!term){
				log.debug("doing activity");
				term=doActivity();
			}
			
		}
		log.info("closing "+connections.size()+" connections");
		// clean up
		for(Connection connection : connections){
			connection.closeCon();
		}
		listener.setTerm(true);
	}
	
	public boolean doActivity(){
		return false;
	}
	
	public final void setTerm(boolean t){
		term=t;
	}
	
	public final ArrayList<Connection> getConnections() {
		return connections;
	}
}
