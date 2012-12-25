package my.b1701.SB.ChatService;

import android.app.Activity;


public class ChatClient {
	
	private static ChatClient instance = null;

    private boolean isConnected;
    private static boolean isAccountConfigured = false;
	private static String userName = "";
	private static String password = "";
    
	private ChatClient(){}
	
	public static ChatClient getInstance()
	{
		
		if(instance == null)
		{
			instance = new ChatClient();
			userName = ChatConfig.getInstance().getString(ChatConfig.ACCOUNT_USERNAME);
			password = ChatConfig.getInstance().getString(ChatConfig.ACCOUNT_PASSWORD);
			if(userName!=""  && password != "")
				isAccountConfigured = true;
		}
		return instance;
		
	}
	    
	
	 public boolean isConnected() {
			return isConnected;
		    }

		   
	    public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	    }
	
	    
	    public boolean isAccountConfigured() {
		return isAccountConfigured;
	    }

	    

}
