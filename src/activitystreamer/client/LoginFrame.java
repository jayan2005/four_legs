package activitystreamer.client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@SuppressWarnings("serial")
public class LoginFrame extends JFrame implements ActionListener {
	private static final Logger log = LogManager.getLogger();
	private JTextArea inputText;
	private JTextArea outputText;
	private JButton sendButton;
	private JButton disconnectButton;
	private JSONParser parser = new JSONParser();

	public LoginFrame() {
		setTitle("Activity Streamer");

		JPanel mainPanel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		mainPanel.setLayout(layout);
		
		GridBagConstraints gbc = new GridBagConstraints();
		JLabel usernameLabel = new JLabel("Username:", JLabel.TRAILING);
		JLabel secretLabel = new JLabel("Secret:", JLabel.TRAILING);
		JLabel serverHostLabel = new JLabel("Server host/IP:", JLabel.TRAILING);
		JLabel serverPortLabel = new JLabel("Server Port:", JLabel.TRAILING);

		JTextField usernameText = new JTextField(25);
		JTextField secretText = new JPasswordField(25);
		JTextField serverHostText = new JTextField(25);
		JTextField serverPortText = new JTextField(5);

		serverHostLabel.setLabelFor(serverHostText);
		serverPortLabel.setLabelFor(serverPortText);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.ipadx = 10;
		mainPanel.add(usernameLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		mainPanel.add(usernameText, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		mainPanel.add(secretLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 3;
		mainPanel.add(secretText, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		mainPanel.add(serverHostLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 3;
		mainPanel.add(serverHostText, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		mainPanel.add(serverPortLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 3;
		mainPanel.add(serverPortText, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		//gbc.insets = new Insets(10, 10, 10, 0);
		gbc.anchor = GridBagConstraints.WEST;
		
		JButton saveButton = new JButton("Login");
		mainPanel.add(saveButton, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;
		//gbc.insets = new Insets(10, 10, 10, 0);
		JButton closeButton = new JButton("Register");
		mainPanel.add(closeButton, gbc);

		add(mainPanel);

		setSize(150, 100);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sendButton) {
			String msg = inputText.getText().trim().replaceAll("\r", "").replaceAll("\n", "").replaceAll("\t", "");
			JSONObject obj;
			try {
				obj = (JSONObject) parser.parse(msg);
				ClientSkeleton.getInstance().sendActivityObject(obj);
			} catch (ParseException e1) {
				log.error("invalid JSON object entered into input text field, data not sent");
			}

		} else if (e.getSource() == disconnectButton) {
			ClientSkeleton.getInstance().disconnect();
		}
	}
}
