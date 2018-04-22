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
	private BufferedReader inreader;
	private DataInputStream input;
	private DataOutputStream output;
	private Socket socket;
	
	// IP and port
	private static String ip = "sunrise.cis.unimelb.edu.au";
	private static int port = 3780;
	//private static String ip = "localhost";
	
	public static ClientSkeleton getInstance(){
		if(clientSolution==null){
			clientSolution = new ClientSkeleton();
		}
		return clientSolution;
	}
	
	
	public ClientSkeleton(){
		textFrame = new TextFrame();
		//this.socket = socket;
		start();
	}

	
	@SuppressWarnings("unchecked")
	public void sendActivityObject(JSONObject activityObj){
		//System.out.println(activityObj);
				
		//textFrame.setOutputText(activityObj);
		//System.out.println(activityObj.toJSONString());
		
		//Connecting to the server
		try(Socket socket = new Socket(ip, port);){
			//Output and Input Stream
			input = new DataInputStream(socket.
					getInputStream());
		    output = new DataOutputStream(socket.
		    		getOutputStream());
		    inreader = new BufferedReader( new InputStreamReader(input));
		    
    		System.out.println(activityObj.toJSONString());
    		    		
    		// Send RMI to Server
    		output.write(activityObj.toJSONString().getBytes());
    		output.write("\r".getBytes());
    		output.flush();
    		
    		// Print out results received from server..
    		if(!activityObj.get("command").equals("LOGOUT")) {
	    		String server_reply_msg = inreader.readLine();
	    		System.out.println("Received from server: "+server_reply_msg);
	
	    		// Displaying results from the server in the GUI
	    		try {
	    			JSONObject JSON_server_response = (JSONObject) parser.parse(server_reply_msg);
	    			textFrame.setOutputText(JSON_server_response);
				} catch (ParseException e1) {
					log.error("invalid JSON object entered into input text field, data not sent");
				}
    		}
		    
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			
		}
	}
	
	
	public void disconnect(){

	    JSONObject diconnect_command = new JSONObject();
	    diconnect_command.put("command" , "LOGOUT");
	    sendActivityObject(diconnect_command);
	    
	    try {
            inreader.close();
            output.close();
            this.socket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

	}
	
	
	public void run(){

	}

	
}
