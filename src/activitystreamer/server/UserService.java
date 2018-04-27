package activitystreamer.server;

public interface UserService {
	
	boolean register(String username, String secret);
	
	boolean login(String username, String secret);

}
