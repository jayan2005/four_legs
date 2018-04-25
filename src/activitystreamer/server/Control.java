package activitystreamer.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
		String secret = null;
		
		//Convert msg JSON string to JSON object
		JSONParser parser = new JSONParser();
		JSONObject client_msg = new JSONObject();
		try {
			client_msg = (JSONObject) parser.parse(msg);
			
			if(client_msg.get("command").equals("REGISTER") || client_msg.get("command").equals("LOGIN")) {
				username = client_msg.get("username").toString();
				
				if(client_msg.containsKey("secret"))
					secret = client_msg.get("secret").toString();
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (client_msg.get("command").equals("REGISTER")) {
			log.debug("REGISTER command received");
			
			RegisterUser newUser = new RegisterUser(username, secret, listOfUsersAL);
			
			if(newUser.addUser()) {
				listOfUsersAL = newUser.getUpdatedUserList();
				log.debug("User registration successful");

				for(int i=0; i < listOfUsersAL.size(); i ++) {
					User testuser = new User();
					testuser = listOfUsersAL.get(i);
					System.out.println("Username : " + testuser.getUsername());
				}
				//send message to client
				return false;
			}
			
			// send message user already registered
			log.debug("User is already registered");
			return true; //should we close the server connection if user is already registered?
		}
		
		if (client_msg.get("command").equals("LOGIN")) {
			log.debug("LOGIN command received");
			
			Login newLogin = new Login(username, secret);
			if(newLogin.logUserIn()) {
				System.out.println("User login success");
				return false;
			}
			
			//Send login fail message
			System.out.println("User login failed");
			return true;
		}
		
		if (client_msg.get("command").equals("ACTIVITY_MESSAGE")) {
			log.debug("ACTIVITY_MESSAGE command received");
			return false;
		}
		
		if (client_msg.get("command").equals("LOGOUT")) {
			return true;
		}
		return true;
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
