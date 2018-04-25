package activitystreamer.client;

import java.io.BufferedReader;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.DebugGraphics;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import activitystreamer.util.Settings;

public class ClientSkeleton extends Thread {
	private static final Logger log = LogManager.getLogger();
	private static ClientSkeleton clientSolution;
	private TextFrame textFrame;
	private JSONParser parser;
	private JSONObject prevActivityObj;
	private String username;
	private String secretPassword;

	private Socket socket;

	public static ClientSkeleton getInstance() {
		if (clientSolution == null) {
			clientSolution = new ClientSkeleton();
		}
		return clientSolution;
	}

	public ClientSkeleton() {
		textFrame = new TextFrame();
		parser = new JSONParser();
		start();
	}

	public void sendActivityObject(JSONObject activityObj) {
		log.debug("Previous Command" + prevActivityObj);
		//saveLoginDetails(activityObj);
		
		try {

			initializeSocket();
			// Output and Input Stream
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());

			log.debug(activityObj.toJSONString());

			// Send RMI to Server
			output.write(activityObj.toJSONString().getBytes());
			output.write("\r".getBytes());
			output.flush();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		prevActivityObj = activityObj;
	}

	private JSONObject readMessageFromServer() {
		DataInputStream input;
		try {
			input = new DataInputStream(socket.getInputStream());
			BufferedReader inreader = new BufferedReader(new InputStreamReader(input));
			new MessageListener(inreader,textFrame).start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	private void initializeSocket() {
		if (socket == null || socket.isClosed()) {
			openSocket();
			readMessageFromServer();
		}
	}
	
	private void closeSocket() {
		try {
			socket.close();
		} catch(IOException e) {
			e.printStackTrace();
		} 
	}

	private Socket openSocket() {
		log.debug("Host : " + Settings.getRemoteHostname() );
		log.debug("port : " + Settings.getRemotePort());
		try {
			//socket = new Socket(Settings.getRemoteHostname(), Settings.getRemotePort());
			socket = new Socket(Settings.getLocalHostname(), Settings.getLocalPort());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	private void logout() {
		JSONObject logoutCommand = new JSONObject();
		logoutCommand.put("command", "LOGOUT");
		sendActivityObject(logoutCommand);
	}

	public void disconnect() {
		logout();
		closeSocket();
	}
	
	/** handles server redirection **/
	public void redirect(JSONObject redirect_command) throws IOException {
			String newHost = redirect_command.get("hostname").toString();
			int newPort = Integer.parseInt(redirect_command.get("port").toString());
			
			closeSocket();
				log.debug("prevous activity message" + prevActivityObj);
				log.debug("redirecting to server : " + newHost + ":"+newPort + "...");
				Settings.setRemoteHostname(newHost);
				Settings.setRemotePort(newPort);
				//openSocket();
				sendActivityObject(prevActivityObj);
	}
	
	/*
	private void saveLoginDetails(JSONObject loginData) {
		if(loginData.get("command").equals("LOGIN")) {
			this.username = loginData.get("username").toString();
			log.debug("username - "+ this.username + " saved.");
			this.secretPassword = loginData.get("secret").toString();
			log.debug("password - "+ this.secretPassword + " saved.");
		}
	}*/

	public void run() {
		
	}

}
