package my.b1701.SB.ChatClient;

import java.util.Date;

public class SBChatMessage {

	private String mName = "";
	private String mMessage = "";
	private boolean mIsError = false;
	private String mTimestamp = "";
	private String mFrom = "";
	private String mTo = "";
	private String mTime= "";

	
	
	/**
	 * Constructor.
	 * @param bareJid A String containing the bare JID of the message's author.
	 * @param name A String containing the name of the message's author.
	 * @param message A String containing the message.
	 * @param isError if the message is an error message.
	 * @param date the time of the message.
	 */
	public SBChatMessage(final String from, String to, final String message, final boolean isError,
	    final String time) {
		mTo = to;
		mFrom = from;	    
	    mMessage = message;
	    mIsError = isError;
	    mTimestamp = time;
	}

	/**
	 * JID attribute accessor.
	 * @return A String containing the bare JID of the message's author.
	 */
	public String getReceiver() {
	    return mTo;
	}
	
	public String getInitiator()
	{
		return mFrom;
	}

	/**
	 * Name attribute accessor.
	 * @return A String containing the name of the message's author.
	 */
	public String getName() {
	    return mName;
	}

	/**
	 * Message attribute accessor.
	 * @return A String containing the message.
	 */
	public String getMessage() {
	    return mMessage;
	}

	
	/**
	 * Name attribute mutator.
	 * @param name A String containing the author's name of the message.
	 */

	public void setName(String name) {
	    mName = name;
	}

	/**
	 * Message attribute mutator.
	 * @param message A String containing a message.
	 */
	public void setMessage(String message) {
	    mMessage = message;
	}

	/**
	 * Get the message type.
	 * @return true if the message is an error message.
	 */
	public boolean isError() {
	    return mIsError;
	}

	/**
	 * Set the Date of the message.
	 * @param string date of the message.
	 */
	public void setTimestamp(String string) {
	    mTimestamp = string;
	}

	/**
	 * Get the Date of the message.
	 * @return if it is a delayed message get the date the message was sended.
	 */
	public String getTimestamp() {
	    return mTimestamp;
	}
	
	public String getTime() {
	    if(mTimestamp!="")
	    {
	    	int colon_at= mTimestamp.indexOf(":", 0);
	    	mTime = mTimestamp.substring(colon_at-2, colon_at+3);
	    }
	    return mTime;
	}

   }
