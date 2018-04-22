package activitystreamer.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("serial")
public class SettingsDialog extends JDialog implements ActionListener {
	private static final Logger log = LogManager.getLogger();
	
	public SettingsDialog(JFrame textFrame){
		setTitle("Settings");
		
		JPanel mainPanel = new JPanel();
		
		
		JLabel serverHostLabel = new JLabel("Server host/IP:",JLabel.TRAILING);
		JLabel serverPortLabel = new JLabel("Server Port:",JLabel.TRAILING);
		
		JTextField serverHostText = new JTextField(25);
		JTextField serverPortText = new JTextField(5);
		
		serverHostLabel.setLabelFor(serverHostText);
		serverPortLabel.setLabelFor(serverPortText);
		
		mainPanel.add(serverHostLabel);
		mainPanel.add(serverHostText);
		
		mainPanel.add(serverPortLabel);
		mainPanel.add(serverPortText);
		
		
		JButton saveButton = new JButton("Save");
		JButton closeButton = new JButton("Close");
		
		mainPanel.add(saveButton);
		mainPanel.add(closeButton);
		
		add(mainPanel);
		
		setSize(100, 100);
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setLocationRelativeTo(textFrame); 
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}
}
