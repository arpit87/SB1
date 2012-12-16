package my.b1701.SB.ChatService;

import my.b1701.SB.ChatService.IChatManager;

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
    
     void login(in String login, in String password);
     
     boolean isLoggedIn();

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