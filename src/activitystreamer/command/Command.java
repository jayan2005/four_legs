package activitystreamer.command;

public interface Command {

	String USER_ANONYMOUS = "anonymous";
	
	enum Names {
		REGISTER,
		REGISTER_FAILED,
		REGISTER_SUCCESS,
		
		LOCK_REQUEST,
		LOCK_DENIED,
		LOCK_ALLOWED,
		
		LOGIN,
		LOGIN_SUCCESS,
		LOGIN_FAILED,
		REDIRECT,
		
		LOGOUT,
		
		AUTHENTICATE,
		AUTHTENTICATION_FAIL,
		
		ACTIVITY_MESSAGE,
		ACTIVITY_BROADCAST,
		
		SERVER_ANNOUNCE,
		
		INVALID_MESSAGE
	}
	
	String getName();
	
	boolean isFailure();
	
}
