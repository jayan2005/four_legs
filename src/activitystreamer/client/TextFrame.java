package activitystreamer.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import activitystreamer.util.Settings;

@SuppressWarnings("serial")
public class TextFrame extends JFrame implements ActionListener {
	private static final Logger log = LogManager.getLogger();
	private JTextArea inputText;
	private JTextArea outputText;
	private JButton sendButton;
	private JButton disconnectButton;
	private JSONParser parser = new JSONParser();
	private JLabel connectionInfo;
	
	public TextFrame(){
		setTitle("ActivityStreamer Text I/O");
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		JPanel statusPanel = new JPanel(new BorderLayout());
		JPanel inputPanel = new JPanel();
		JPanel outputPanel = new JPanel();
		
		connectionInfo = new JLabel();
		
		setConnectionInfo();
		statusPanel.setBorder(new EmptyBorder(0,10,0,0));
		statusPanel.add(connectionInfo,BorderLayout.WEST);
		
		inputPanel.setLayout(new BorderLayout());
		outputPanel.setLayout(new BorderLayout());
		Border lineBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.lightGray),"JSON input, to send to server");
		inputPanel.setBorder(lineBorder);
		lineBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.lightGray),"JSON output, received from server");
		outputPanel.setBorder(lineBorder);
		outputPanel.setName("Text output");
		
		inputText = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(inputText);
		inputPanel.add(scrollPane,BorderLayout.CENTER);
		
		JPanel buttonGroup = new JPanel();
		sendButton = new JButton("Send");
		disconnectButton = new JButton("Disconnect");
		
		buttonGroup.add(sendButton);
		buttonGroup.add(disconnectButton);
		inputPanel.add(buttonGroup,BorderLayout.SOUTH);
		sendButton.addActionListener(this);
		disconnectButton.addActionListener(this);
		
		
		outputText = new JTextArea();
		scrollPane = new JScrollPane(outputText);
		outputPanel.add(scrollPane,BorderLayout.CENTER);
		
		statusPanel.setPreferredSize(new Dimension(getWidth(), 80));
		mainPanel.add(statusPanel,BorderLayout.NORTH);
		
		JPanel textPanel = new JPanel(new GridLayout(1,2));
		textPanel.add(inputPanel);
		textPanel.add(outputPanel);
		textPanel.setPreferredSize(new Dimension(getWidth(), 650));
		mainPanel.add(textPanel,BorderLayout.SOUTH);
		
		add(mainPanel);
		
		
		setSize(1280,768);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null); 
	}

	public void setConnectionInfo() {
		StringBuilder connectionInfoBuilder = new StringBuilder();
		connectionInfoBuilder.append("<html><div align=left><br/><b><u>Connection Information:</b></u><br/>");
		connectionInfoBuilder.append("<i>Username: </i>"+Settings.getUsername()+"<br/>");
		connectionInfoBuilder.append("<i>Server hostname: </i>"+Settings.getRemoteHostname()+"<br/>");
		connectionInfoBuilder.append("<i>Server port: </i>"+Settings.getRemotePort()+"<br/></div></html>");
		connectionInfo.setText(connectionInfoBuilder.toString());
	}

	public void setOutputText(final JSONObject obj){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(obj.toJSONString());
		String prettyJsonString = gson.toJson(je);
		outputText.append("\n");
		outputText.append(prettyJsonString);
		outputText.revalidate();
		outputText.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==sendButton){
			String msg = inputText.getText().trim().replaceAll("\r","").replaceAll("\n","").replaceAll("\t", "");
			JSONObject obj;
			try {
				obj = (JSONObject) parser.parse(msg);
				ClientSkeleton.getInstance().sendActivityMessage(obj);
			} catch (ParseException e1) {
				JOptionPane.showMessageDialog(this, "invalid JSON object entered into input text field, data not sent");
				log.error("invalid JSON object entered into input text field, data not sent");
			}
			
		} else if(e.getSource()==disconnectButton){
			ClientSkeleton.getInstance().disconnect();
			ClientUIManager.getInstance().handleLogout();
		}
	}
}
