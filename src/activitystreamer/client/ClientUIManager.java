package activitystreamer.client;

public class ClientUIManager {

	private static ClientUIManager instance; 
	
	private TextFrame textFrame;
	private LoginFrame loginFrame;
	
	private ClientUIManager() {
		
	}
	
	public static ClientUIManager getInstance() {
		if (instance == null) {
			instance = new ClientUIManager();
		}
		return instance;
	}
	
	public  void showLoginFrame() {
		if (loginFrame == null) {
			loginFrame = new LoginFrame();
		}
	}
	
	public  void showTextFrame() {
		if (textFrame == null) {
			textFrame = new TextFrame();
		}
	}
	
	public void closeLoginFrame() {
		if (loginFrame != null) {
			loginFrame.dispose();
			loginFrame.setVisible(false);
			loginFrame = null;
		}
	}
	
	public void closeTextFrame() {
		if (textFrame != null) {
			textFrame.dispose();
			textFrame.setVisible(false);
			textFrame = null;
			showLoginFrame();
		}
	}
	
}
