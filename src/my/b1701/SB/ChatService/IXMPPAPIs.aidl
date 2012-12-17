package my.b1701.SB.ChatService;

import my.b1701.SB.ChatService.IChatManager;
import my.b1701.SB.ChatClient.ISBChatConnAndMiscListener;

interface IXMPPAPIs {
	     
    /**
     * Connect and login synchronously on the server.
     */
    void connect();

	boolean isConnected();

    /**
     * Disconnect from the server
     */
    void disconnect();
    
     void loginAsync(in String login, in String password);
     
     void loginWithCallBack(in String login, in String password,in ISBChatConnAndMiscListener listener);
     
     boolean isLoggedIn();
     
     boolean tryingLogging();

    /**
     * Get the chat manager.
     */
    IChatManager getChatManager();

    /**
     * Change the status of the user.
     * @param status the status to set
     * @param msg the message state to set
     */
    void changeStatus(in int status, in String msg);
   

}