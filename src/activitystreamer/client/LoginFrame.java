package activitystreamer.client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;

import activitystreamer.util.Settings;

@SuppressWarnings("serial")
public class LoginFrame extends JFrame implements ActionListener {

	private JTextField usernameText;
	private JTextField secretText;
	private JTextField serverHostText;
	private JTextField serverPortText;

	private JButton loginButton;
	private JButton registerButton;
	private JButton loginAnonymouslyButton;

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

		usernameText = new JTextField(25);
		secretText = new JPasswordField(25);
		serverHostText = new JTextField(Settings.getRemoteHostname(), 25);
		serverPortText = new JTextField(String.valueOf(Settings.getRemotePort()), 5);

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
		// gbc.insets = new Insets(10, 10, 10, 0);
		gbc.anchor = GridBagConstraints.WEST;

		loginAnonymouslyButton = new JButton("Login anonymously");
		loginAnonymouslyButton.addActionListener(this);
		mainPanel.add(loginAnonymouslyButton, gbc);

		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;
		// gbc.insets = new Insets(10, 10, 10, 0);
		loginButton = new JButton("Login");
		loginButton.addActionListener(this);
		mainPanel.add(loginButton, gbc);

		gbc.gridx = 2;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;
		// gbc.insets = new Insets(10, 10, 10, 0);
		registerButton = new JButton("Register");
		registerButton.addActionListener(this);
		mainPanel.add(registerButton, gbc);

		add(mainPanel);

		setSize(150, 200);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginButton) {
			handleLogin(false);
		} else if (e.getSource() == loginAnonymouslyButton) {
			Settings.setUsername("anonymous");
			handleLogin(true);
		}
		else if (e.getSource() == registerButton) {
			handleRegister();
		}
	}

	public void handleRegister() {
		if (isFormValid()) {
			ClientSkeleton.getInstance().sendRegisterCommand();
		}
	}

	public void handleLogin(boolean anonymousLogin) {
		if (!anonymousLogin) {
			if (isFormValid()) {
				doHandleLogin();
			}
		} else {
			if (isServerSettingsValid()) {
				doHandleLogin();
			}
		}
	}

	private void doHandleLogin() {
		ClientSkeleton.getInstance().sendLoginCommand();
	}

	private boolean isFormValid() {
		String username = usernameText.getText();
		String secret = secretText.getText();

		if (!isServerSettingsValid()) {
			return false;
		}

		if (StringUtils.isBlank(username) || StringUtils.isBlank(secret)) {
			JOptionPane.showMessageDialog(this, "All fields are required.");
			return false;
		}

		Settings.setUsername(username);
		Settings.setSecret(secret);

		return true;
	}

	private boolean isServerSettingsValid() {
		String serverHost = serverHostText.getText();
		String serverPort = serverPortText.getText();
		if (StringUtils.isBlank(serverHost) || StringUtils.isBlank(serverPort)) {
			JOptionPane.showMessageDialog(this, "All fields are required.");
			return false;
		}

		try {
			Integer.parseInt(serverPort);
		} catch (NumberFormatException nfe) {
			JOptionPane.showMessageDialog(this, "Port is not a number.");
			return false;
		}

		Settings.setRemoteHostname(serverHost);
		Settings.setRemotePort(Integer.parseInt(serverPort));

		return true;
	}
}
