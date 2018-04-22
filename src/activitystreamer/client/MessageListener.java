package activitystreamer.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;

import javax.swing.SwingUtilities;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MessageListener extends Thread {

	private BufferedReader reader;
	private TextFrame textFrame;
	private JSONParser parser;
	
	public MessageListener(BufferedReader reader,TextFrame textFrame) {
		this.reader = reader;
		this.textFrame = textFrame;
		parser = new JSONParser();
	}
	
	@Override
	public void run() {
		
		try {
			
			while (true) {
				String data = reader.readLine();
				//log.debug("Received from server: " + data);
				try {
					final JSONObject result = (JSONObject) parser.parse(data);
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							textFrame.setOutputText(result);
						}
					});	
					
					if(result.get("command").equals("REDIRECT")){
						//log.debug("Login redirection acttivated");
						ClientSkeleton.getInstance().redirect(result);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
