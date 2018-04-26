package activitystreamer.server;

import java.io.IOException;

import java.net.Socket;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import activitystreamer.commands.authenticate.AuthenticateCommand;
import activitystreamer.commands.json.builder.CommandJsonBuilder;
import activitystreamer.commands.json.builders.impl.CommandJsonBuilderFactoryImpl;
import activitystreamer.commands.misc.InvalidMessageCommand;
import activitystreamer.util.Settings;

public class Control extends Thread {
	private static final Logger log = LogManager.getLogger();
	private static ArrayList<Connection> connections;
	private static boolean term=false;
	private static Listener listener;
	private static ArrayList<User> listOfUsersAL;
	
	protected static Control control = null;
	
	public static Control getInstance() {
		if(control==null){
			control=new Control();
		} 
		return control;
	}
	
	public Control() {
		
		//initializing user list array
		listOfUsersAL = new ArrayList();
		
		// initialize the connections array
		connections = new ArrayList<Connection>();
		// start a listener
		try {
			listener = new Listener();
		} catch (IOException e1) {
			log.fatal("failed to startup a listening thread: "+e1);
			System.exit(-1);
		}	
		
		// connecting to remote server
		initiateConnection();
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
	public synchronized boolean process(Connection con,String msg){
		log.debug("Server received : " + msg);
		String username = null;
		String secret = "";
		
		//Convert msg JSON string to JSON object
		JSONParser parser = new JSONParser();
		JSONObject client_msg = new JSONObject();
		try {
			client_msg = (JSONObject) parser.parse(msg);
			
			if(client_msg.get("command").equals("REGISTER") || client_msg.get("command").equals("LOGIN") || client_msg.get("command").equals("ACTIVITY_MESSAGE")) {
				username = client_msg.get("username").toString();
				
				if(client_msg.containsKey("secret") && (client_msg.get("secret") != null))
					secret = client_msg.get("secret").toString();
			}
			
			//log.debug("Secret : " + secret);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Handling client REGISTER command
		if (client_msg.get("command").equals("REGISTER")) {
			//log.debug("REGISTER command received");
			
			RegisterUser newUser = new RegisterUser(username, secret, listOfUsersAL);
			
			if(newUser.addUser()) {
				listOfUsersAL = newUser.getUpdatedUserList();
				log.debug("User registration successful");
				newUser.sendRegisterSuccessCommand(con); //User register success command
				return false; // keep connection open
			}
			
			newUser.sendRegisterFailedCommand(con); // User already registered command
			return true; // close connection.
		} 

		// Handling client LOGIN command
		if (client_msg.get("command").equals("LOGIN")) {
			// log.debug("LOGIN command received");

			Login newLogin = new Login(username, secret);
			if (newLogin.logUserIn()) {
				// System.out.println("User login success");
				newLogin.sendLoginSuccessCommand(con); // Send LOGIN_SUCCESS command to client
				return false; // Keep connection open
			}
			
			//if(con.isOpen()) {
				newLogin.sendLoginFailCommand(con); // Send LOGIN_FAILED command to client
				return true; // close connection.
			//}

		} 
		
		// Handling client ACTIVITY_MESSAGE command
		if (client_msg.get("command").equals("ACTIVITY_MESSAGE")) {
			log.debug("ACTIVITY_MESSAGE command received");
			
			Login activityMsgUser = new Login(username, secret);
			if(activityMsgUser.logUserIn()) {
				// Code to process "ACTIVITY_MESSAGE"
				log.debug("Broadcast activity message");
				return false;
			}			
			
			if(activityMsgUser.sendAuthenticationFailCommand(con))
				return true;
		} 

		// Handling client LOGOUT command
		if (client_msg.get("command").equals("LOGOUT")) {
			return true;
		} 
		
		// Handling SERVER_ANNOUNCE command
		if (client_msg.get("command").equals("SERVER_ANNOUNCE")) {
			// code here for handling server announcements
			return false;
		}
		
		// Handling remote server AUTHENTICATE command 
		if(client_msg.get("command").equals("AUTHENTICATE")) {
			String serverSecret = client_msg.get("secret").toString();
			
			AuthenticateRemoteServer remoteServer = new AuthenticateRemoteServer(serverSecret);
			if(!remoteServer.isSecretCorrect()) {
				remoteServer.sendAuthenticationFailCommand(con);
				return true;
			}
			return false;
		}
		
		// Handling remote server AUTHENTICATION_FAIL command 
		if(client_msg.get("command").equals("AUTHENTICATION_FAIL")) {
			log.debug("Remote sever connection failed - ERROR Command : " + client_msg.toJSONString());
			return true;
		}
		
		//Send INVALID_MESSAGE command to client
		sendInvalidMessageCommand(con);
		return true; //will close connection.
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
		
		// Sending AUTHENTICATE command once server is started
		AuthenticateCommand authenticateCommand = new AuthenticateCommand(Settings.getSecret());
		CommandJsonBuilder<AuthenticateCommand> authenticateCommandJsonBuilder = CommandJsonBuilderFactoryImpl.getInstance()
				.getJsonBuilder(authenticateCommand);
		
		JSONObject authenticateCommandJsonMsg = authenticateCommandJsonBuilder.buildJsonObject(authenticateCommand);
		if(c.writeMsg(authenticateCommandJsonMsg.toJSONString()))
			log.debug("Remote Server Connect Request sent : " +authenticateCommandJsonMsg);
		
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
	
	public ArrayList<User> getRegisteredUserList(){
		return this.listOfUsersAL;
	}
	
	// send INVALID_MESSAGE
	public void sendInvalidMessageCommand(Connection con) {
		InvalidMessageCommand invalidMessageCommandMsg = new InvalidMessageCommand("The received message did not contain a command");
		CommandJsonBuilder<InvalidMessageCommand> invalidMessageCommandMsgJsonBuilder = CommandJsonBuilderFactoryImpl.getInstance()
				.getJsonBuilder(invalidMessageCommandMsg);
		
		JSONObject invalidMessageCommandJsonMsg = invalidMessageCommandMsgJsonBuilder.buildJsonObject(invalidMessageCommandMsg);
		con.writeMsg(invalidMessageCommandJsonMsg.toJSONString());
	}
	
}
