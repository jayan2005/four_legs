package activitystreamer.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

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

	private Socket socket;

	public static ClientSkeleton getInstance() {
		if (clientSolution == null) {
			clientSolution = new ClientSkeleton();
		}
		return clientSolution;
	}

	public ClientSkeleton() {
		//textFrame = new TextFrame();
		new LoginFrame();
		parser = new JSONParser();
		start();
	}
	
	public void login(String username, String secret) {
		
	}
	
	
	public JSONObject sendAndReceive(JSONObject request) {
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
	}

	public void sendActivityObject(JSONObject activityObj) {
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
	}

	
	private JSONObject readMessageFromServerOnce() {
		DataInputStream input;
		try {
			input = new DataInputStream(socket.getInputStream());
			BufferedReader inreader = new BufferedReader(new InputStreamReader(input));
			while (true) {
				String data = inreader.readLine();
				log.debug("Received from server: " + data);
				try {
					final JSONObject result = (JSONObject) parser.parse(data);
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							textFrame.setOutputText(result);
						}
					});
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	
	private JSONObject readMessageFromServer() {
		initializeSocket();
		DataInputStream input;
		try {
			input = new DataInputStream(socket.getInputStream());
			BufferedReader inreader = new BufferedReader(new InputStreamReader(input));
			while (true) {
				String data = inreader.readLine();
				log.debug("Received from server: " + data);
				try {
					final JSONObject result = (JSONObject) parser.parse(data);
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							textFrame.setOutputText(result);
						}
					});
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	private void initializeSocket() {
		if (socket == null || socket.isClosed()) {
			openSocket();
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
		try {
			socket = new Socket(Settings.getRemoteHostname(), Settings.getRemotePort());
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

	public void run() {
	}

}
