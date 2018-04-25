package activitystreamer.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import activitystreamer.command.Command;
import activitystreamer.commands.json.builder.CommandJsonBuilder;
import activitystreamer.commands.json.builders.impl.CommandJsonBuilderFactoryImpl;
import activitystreamer.commands.login.LoginCommand;
import activitystreamer.commands.misc.LogoutCommand;
import activitystreamer.commands.register.RegisterCommand;
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
		ClientUIManager.getInstance().showLoginFrame();
		parser = new JSONParser();
		start();
	}
	
	public Status register() {
		RegisterCommand registerCommand = new RegisterCommand(Settings.getUsername(), Settings.getSecret()); 
		CommandJsonBuilder<RegisterCommand> commandJsonBuilder = CommandJsonBuilderFactoryImpl.getInstance()
				.getJsonBuilder(registerCommand);
		JSONObject registerCommandJson = commandJsonBuilder.buildJsonObject(registerCommand);

		JSONObject resultJsonObject = sendAndReceive(registerCommandJson);
		Status status = new Status();
		if (resultJsonObject!=null) {
			status.setSuccess(Command.Names.REGISTER_SUCCESS.toString().equals(resultJsonObject.get("command")));
			status.setMessage((String) resultJsonObject.get("info"));
		}
		return status;
	}

	public Status login() {
		LoginCommand loginCommand = new LoginCommand(Settings.getUsername(), Settings.getSecret());

		CommandJsonBuilder<LoginCommand> commandJsonBuilder = CommandJsonBuilderFactoryImpl.getInstance()
				.getJsonBuilder(loginCommand);
		JSONObject loginCommandJson = commandJsonBuilder.buildJsonObject(loginCommand);

		JSONObject resultJsonObject = sendAndReceive(loginCommandJson);
		
		Status status = new Status();
		if (resultJsonObject!=null) {
			status.setSuccess(Command.Names.LOGIN_SUCCESS.toString().equals(resultJsonObject.get("command")));
			status.setMessage((String) resultJsonObject.get("info"));
		}
		return status;
	}

	public void logout() {
		LogoutCommand logoutCommand = new LogoutCommand();
		CommandJsonBuilder<LogoutCommand> commandJsonBuilder = CommandJsonBuilderFactoryImpl.getInstance()
				.getJsonBuilder(logoutCommand);
		JSONObject logoutCommandJson = commandJsonBuilder.buildJsonObject(logoutCommand);

		send(logoutCommandJson);

		ClientUIManager.getInstance().closeTextFrame();
		ClientUIManager.getInstance().showLoginFrame();
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
		} catch (IOException e) {
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

	public void disconnect() {
		logout();
		closeSocket();
	}

	public void run() {
	}

}
