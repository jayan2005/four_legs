package activitystreamer.server;

import java.io.IOException;

import java.net.Socket;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import activitystreamer.commands.json.builder.CommandJsonBuilder;
import activitystreamer.commands.json.builders.impl.CommandJsonBuilderFactoryImpl;
import activitystreamer.commands.login.LoginFailedCommand;
import activitystreamer.commands.login.LoginSuccessCommand;
import activitystreamer.commands.misc.InvalidMessageCommand;
import activitystreamer.commands.misc.LogoutCommand;
import activitystreamer.commands.register.RegisterFailedCommand;
import activitystreamer.commands.register.RegisterSuccessCommand;
import activitystreamer.util.Settings;

public class Control extends Thread {
	private static final Logger log = LogManager.getLogger();
	private static ArrayList<Connection> connections;
	private static boolean term=false;
	private static Listener listener;
	private ArrayList<User> listOfUsersAL;
	
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
			
			log.debug("Secret : " + secret);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (client_msg.get("command").equals("REGISTER")) {
			//log.debug("REGISTER command received");
			
			RegisterUser newUser = new RegisterUser(username, secret, listOfUsersAL);
			
			if(newUser.addUser()) {
				listOfUsersAL = newUser.getUpdatedUserList();
				log.debug("User registration successful");
				
				/*
				for(int i=0; i < listOfUsersAL.size(); i ++) {
					User testuser = new User();
					testuser = listOfUsersAL.get(i);
					System.out.println("Username : " + testuser.getUsername());
				}*/
				
				//User register success command
				RegisterSuccessCommand registerSuccessMsg = new RegisterSuccessCommand("Register success for "+ username);
				CommandJsonBuilder<RegisterSuccessCommand> registerSuccessCommandJsonBuilder = CommandJsonBuilderFactoryImpl.getInstance()
						.getJsonBuilder(registerSuccessMsg);
				
				JSONObject registerSuccessCommandJsonMsg = registerSuccessCommandJsonBuilder.buildJsonObject(registerSuccessMsg);
				if(con.writeMsg(registerSuccessCommandJsonMsg.toJSONString()))  // Check message sent
					return false; // will keep connection open
			}
			
			// User already registered command
			RegisterFailedCommand registerFailedMsg = new RegisterFailedCommand(username + " is already registered with the system");
			CommandJsonBuilder<RegisterFailedCommand> registerFailedCommandJsonBuilder = CommandJsonBuilderFactoryImpl.getInstance()
					.getJsonBuilder(registerFailedMsg);
			
			JSONObject registerFailedCommandJsonMsg = registerFailedCommandJsonBuilder.buildJsonObject(registerFailedMsg);
			if(con.writeMsg(registerFailedCommandJsonMsg.toJSONString())) // Check message sent
				return true; //will close connection.
			
		} else if (client_msg.get("command").equals("LOGIN")) {
				log.debug("LOGIN command received");
				
				Login newLogin = new Login(username, secret);
				if(newLogin.logUserIn()) {
					//System.out.println("User login success");
					
					//Send LOGIN_SUCCESS command to client
					LoginSuccessCommand loginSuccessCommandMsg = new LoginSuccessCommand("Logged in as user " + username);
					CommandJsonBuilder<LoginSuccessCommand> loginSuccessCommandJsonBuilder = CommandJsonBuilderFactoryImpl.getInstance()
							.getJsonBuilder(loginSuccessCommandMsg);
					
					JSONObject loginSuccessCommandJsonMsg = loginSuccessCommandJsonBuilder.buildJsonObject(loginSuccessCommandMsg);
					if(con.writeMsg(loginSuccessCommandJsonMsg.toJSONString())) // Check message sent
						return false; //will close connection.
				}
				
				//Send LOGIN_FAILED command to client
				LoginFailedCommand loginFailedCommandMsg = new LoginFailedCommand("Attempt to login with wrong secret");
				CommandJsonBuilder<LoginFailedCommand> loginFailedCommandJsonBuilder = CommandJsonBuilderFactoryImpl.getInstance()
						.getJsonBuilder(loginFailedCommandMsg);
				
				JSONObject loginFailedCommandJsonMsg = loginFailedCommandJsonBuilder.buildJsonObject(loginFailedCommandMsg);
				if(con.writeMsg(loginFailedCommandJsonMsg.toJSONString())) // Check message sent
					return true; //will close connection.
				
			} else if (client_msg.get("command").equals("ACTIVITY_MESSAGE")) {
						log.debug("ACTIVITY_MESSAGE command received");
						
						// Code to process "ACTIVITY_MESSAGE"
						return false;
						
					} else if (client_msg.get("command").equals("LOGOUT")) {
								return true;
							} 
		
		//Send INVALID_MESSAGE command to client
		InvalidMessageCommand invalidMessageCommandMsg = new InvalidMessageCommand("The received message did not contain a command");
		CommandJsonBuilder<InvalidMessageCommand> invalidMessageCommandMsgJsonBuilder = CommandJsonBuilderFactoryImpl.getInstance()
				.getJsonBuilder(invalidMessageCommandMsg);
		
		JSONObject invalidMessageCommandJsonMsg = invalidMessageCommandMsgJsonBuilder.buildJsonObject(invalidMessageCommandMsg);
		con.writeMsg(invalidMessageCommandJsonMsg.toJSONString());
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
	
}
