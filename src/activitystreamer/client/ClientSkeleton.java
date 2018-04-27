package activitystreamer.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import activitystreamer.commands.json.builder.CommandJsonBuilder;
import activitystreamer.commands.json.builders.impl.CommandJsonBuilderFactoryImpl;
import activitystreamer.commands.login.LoginCommand;
import activitystreamer.commands.misc.LogoutCommand;
import activitystreamer.commands.register.RegisterCommand;
import activitystreamer.util.Settings;

public class ClientSkeleton extends Thread {
	
	private static final Logger log = LogManager.getLogger();
	private static ClientSkeleton clientSolution;
	private JSONParser parser;

	private Socket socket;
	private MessageListener messageListener;

	public static ClientSkeleton getInstance() {
		if (clientSolution == null) {
			clientSolution = new ClientSkeleton();
		}
		return clientSolution;
	}

	public ClientSkeleton() {
		parser = new JSONParser();
		ClientUIManager.getInstance().showLoginFrame();
		start();
	}
	
	public void sendRegisterCommand() {
		RegisterCommand registerCommand = new RegisterCommand(Settings.getUsername(), Settings.getSecret()); 
		CommandJsonBuilder<RegisterCommand> commandJsonBuilder = CommandJsonBuilderFactoryImpl.getInstance()
				.getJsonBuilder(registerCommand);
		JSONObject registerCommandJson = commandJsonBuilder.buildJsonObject(registerCommand);

		send(registerCommandJson);
	}

	public void sendLoginCommand() {
		LoginCommand loginCommand = new LoginCommand(Settings.getUsername(), Settings.getSecret());

		CommandJsonBuilder<LoginCommand> commandJsonBuilder = CommandJsonBuilderFactoryImpl.getInstance()
				.getJsonBuilder(loginCommand);
		JSONObject loginCommandJson = commandJsonBuilder.buildJsonObject(loginCommand);

		send(loginCommandJson);
	}

	public void sendLogoutCommand() {
		LogoutCommand logoutCommand = new LogoutCommand();
		CommandJsonBuilder<LogoutCommand> commandJsonBuilder = CommandJsonBuilderFactoryImpl.getInstance()
				.getJsonBuilder(logoutCommand);
		JSONObject logoutCommandJson = commandJsonBuilder.buildJsonObject(logoutCommand);

		stopMessageListener();
		
		send(logoutCommandJson);
		
		Settings.setUsername(null);
		Settings.setSecret(null);
	}
	

	public JSONObject sendAndReceive(JSONObject request) {
		JSONObject result = null;
		try {
			send(request);
			DataInputStream input = new DataInputStream(socket.getInputStream());
			BufferedReader inreader = new BufferedReader(new InputStreamReader(input));
			String data = inreader.readLine();
			log.debug("Received from server: " + data);
			result = (JSONObject) parser.parse(data);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public void send(JSONObject request) {
		try {
			initializeSocket();
			// Output and Input Stream
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());

			log.debug(request.toJSONString());

			// Send RMI to Server
			output.write(request.toJSONString().getBytes());
			output.write("\r".getBytes());
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initializeSocket() {
		if (socket == null || socket.isClosed()) {
			openSocket();
			startMessageListener();
		}
	}

	private void closeSocket() {
		try {
			socket.close();
			socket = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Socket openSocket() {
		log.debug("Host : " + Settings.getRemoteHostname() );
		log.debug("port : " + Settings.getRemotePort());
		try {
			socket = new Socket(Settings.getRemoteHostname(), Settings.getRemotePort());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public void disconnect() {
		sendLogoutCommand();
		stopMessageListener();
		closeSocket();
	}
	
	public void startMessageListener() {
		DataInputStream input;
		try {
			input = new DataInputStream(socket.getInputStream());
			BufferedReader inreader = new BufferedReader(new InputStreamReader(input));
			messageListener = new MessageListener(inreader);
			messageListener.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stopMessageListener() {
		if (messageListener != null) {
			messageListener.close();
		}
	}
	
	public void run() {
		
	}

}
