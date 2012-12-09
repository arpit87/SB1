package my.b1701.SB.ChatClient;

import  my.b1701.SB.ChatService.IChatAdapter;

interface IChatManagerListener {

    	/**
    	 * Callback when a new chat session is created.
    	 * @param chat		the created chat session
    	 * @param locally	true if the session is create by a chat manager.
    	 */
    	void chatCreated(IChatAdapter chat, boolean locally);
    	
    	

}