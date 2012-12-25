package my.b1701.SB.ChatClient;

interface ISBChatConnAndMiscListener {

	void loggedIn();
	
    /**
     *  Callback to call when the connection is closed
     */
    void connectionClosed();

    /**
     *  Callback to call when the connection occurs
     *  @Deprecated
     */
    //void onConnect();

    //void connectionClosedOnError(in Exception e);
    /**
     *  Callback to call when the connection is closed on error
     */
    void connectionClosedOnError();

    /**
     * Callback to call when trying to reconnecting
     */
    void reconnectingIn(in int seconds);

    /**
     *  Callback to call when the reconnection has failed
     */
    void reconnectionFailed();

    /**
     *  Callback to call when the reconnection is successfull
     */
    void reconnectionSuccessful();

    /**
     *  Callback to call when the connection Failed
     */
    void connectionFailed(in String errorMsg);
}
