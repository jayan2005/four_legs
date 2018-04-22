package activitystreamer.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@SuppressWarnings("serial")
public class LoginFrame extends JFrame implements ActionListener {
	private static final Logger log = LogManager.getLogger();
	private JTextArea inputText;
	private JTextArea outputText;
	private JButton sendButton;
	private JButton disconnectButton;
	private JSONParser parser = new JSONParser();
	
	public LoginFrame(){
		setTitle("ActivityStreamer Login");
		
		JPanel mainPanel = new JPanel();
		
		
		setLocationRelativeTo(null); 
		setSize(200,200);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==sendButton){
			String msg = inputText.getText().trim().replaceAll("\r","").replaceAll("\n","").replaceAll("\t", "");
			JSONObject obj;
			try {
				obj = (JSONObject) parser.parse(msg);
				ClientSkeleton.getInstance().sendActivityObject(obj);
			} catch (ParseException e1) {
				log.error("invalid JSON object entered into input text field, data not sent");
			}
			
		} else if(e.getSource()==disconnectButton){
			ClientSkeleton.getInstance().disconnect();
		}
	}
}
