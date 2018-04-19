package activitystreamer.client;

import java.awt.Container;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.UnknownHostException;

import javax.swing.JTextArea;

import activitystreamer.util.Settings;

public class ClientSkeleton extends Thread {
	private static final Logger log = LogManager.getLogger();
	private static ClientSkeleton clientSolution;
	private TextFrame textFrame;
	private JSONParser parser = new JSONParser();
	
	// IP and port
	private static String ip = "sunrise.cis.unimelb.edu.au";
	private static int port = 3781;
	//private static String ip = "localhost";
	
	public static ClientSkeleton getInstance(){
		if(clientSolution==null){
			clientSolution = new ClientSkeleton();
		}
		return clientSolution;
	}
	
	
	public ClientSkeleton(){
		textFrame = new TextFrame();
		start();
	}

	
	@SuppressWarnings("unchecked")
	public void sendActivityObject(JSONObject activityObj){
		//System.out.println(activityObj);
		
		//Connecting to the server
		try(Socket socket = new Socket(ip, port);){
			// Output and Input Stream
			DataInputStream input = new DataInputStream(socket.
					getInputStream());
		    DataOutputStream output = new DataOutputStream(socket.
		    		getOutputStream());
		    
    	
    		System.out.println(activityObj.toJSONString());

    		
    		// Read hello from server..
    		String message = input.readUTF();
    		System.out.println(message);
    		
    		// Send RMI to Server
    		output.writeUTF(activityObj.toJSONString());
    		output.flush();
    		
    		// Print out results received from server..
    		String result = input.readUTF();
    		System.out.println("Received from server: "+result);
    		
    		// Displaying results from the server in the GUI
    		try {
    			JSONObject JSON_server_response = (JSONObject) parser.parse(result);
    			textFrame.setOutputText(JSON_server_response);
			} catch (ParseException e1) {
				log.error("invalid JSON object entered into input text field, data not sent");
			}
		    
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			
		}

	}
	
	
	public void disconnect(){
		
	}
	
	
	public void run(){

	}

	
}
