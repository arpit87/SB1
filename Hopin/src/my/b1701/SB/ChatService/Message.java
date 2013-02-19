package my.b1701.SB.ChatService;



import my.b1701.SB.ChatClient.SBChatMessage;

import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.util.StringUtils;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

//mFrom,to should have ip appended
//getParticipant gives name widout ip
public class Message implements Parcelable {

	private static String TAG = "my.b1701.SB.ChatService.Message";
/** Normal message type. Theese messages are like an email, with subject. */
public static final int MSG_TYPE_NORMAL = 100;

/** Chat message type. */
public static final int MSG_TYPE_CHAT = 200;

/** Group chat message type. */
public static final int MSG_TYPE_NEWUSER_BROADCAST = 300;

/** Error message type. */
public static final int MSG_TYPE_ERROR = 400;

public static final int MSG_TYPE_INFO = 500;

public static final int MSG_TYPE_ACK = 600;

public static final String UNIQUEID = "unique_id";
public static final String SBMSGTYPE = "sb_msg_type";



/** Parcelable.Creator needs by Android. */
public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {

@Override
public Message createFromParcel(Parcel source) {
    return new Message(source);
}

@Override
public Message[] newArray(int size) {
    return new Message[size];
}
};


private int mType = (int)Message.MSG_TYPE_CHAT;
private String mBody = "";
private String mSubject="";
private String mTo ="";
private String mFrom ="";
private String mThread = "";
private String mTime = "";
private int mStatus = SBChatMessage.UNKNOWN;
private long mUniqueMsgIdentifier = 0;

// TODO ajouter l'erreur

/**
 * Constructor.
 * @param to the destinataire of the message
 * @param type the message type
 */
public Message(final String to, final int type) {
mTo = to ;
mType = type;
mBody = "";
mSubject = "";
mThread = "";
mFrom = "";
mTime = "";
}

/**
 * Constructor a message of type chat.
 * @param to the destination of the message
 */
public Message(final String to) {
this(to, MSG_TYPE_CHAT);
}

/**
 * Construct a message from a smack message packet.
 * @param smackMsg Smack message packet
 */
public Message(final org.jivesoftware.smack.packet.Message smackMsg) {
mTo = smackMsg.getTo();
mType = (Integer) smackMsg.getProperty(SBMSGTYPE);
/*switch (smackMsg.getType()) {
    case chat:
	mType = MSG_TYPE_CHAT;
	break;
    case groupchat:
	mType = MSG_TYPE_NEWUSER_BROADCAST;
	break;
    case normal:
	mType = MSG_TYPE_NORMAL;
	break;
    case headline:
    mType = MSG_TYPE_ACK;
    case error:
	mType = MSG_TYPE_ERROR;
	break;
    default:
	Log.w(TAG, "message type error" + smackMsg.getType());
	break;
}*/
this.mFrom = smackMsg.getFrom();
if (mType == MSG_TYPE_ERROR) {
    XMPPError er = smackMsg.getError();
    String msg = er.getMessage();
    if (msg != null)
    	mBody = msg;
    else
    	mBody = er.getCondition();
} else {
    mBody = smackMsg.getBody();
    mSubject = smackMsg.getSubject();
    mThread = smackMsg.getThread();
    mTime = (String) smackMsg.getProperty("time");
    mUniqueMsgIdentifier = (Long) smackMsg.getProperty(UNIQUEID);
}
}

/**
 * Construct a message from a parcel.
 * @param in parcel to use for construction
 */
private Message(final Parcel in) {
mType = in.readInt();
mTo = in.readString();
mBody = in.readString();
mSubject = in.readString();
mThread = in.readString();
mFrom = in.readString();
mStatus = in.readInt();
mUniqueMsgIdentifier = in.readLong();
}

/**
 * {@inheritDoc}
 */
@Override
public void writeToParcel(Parcel dest, int flags) {
// TODO Auto-generated method stub
dest.writeInt(mType);
dest.writeString(mTo);
dest.writeString(mBody);
dest.writeString(mSubject);
dest.writeString(mThread);
dest.writeString(mFrom);
dest.writeInt(mStatus);
dest.writeLong(mUniqueMsgIdentifier);
}

/**
 * Get the type of the message.
 * like ACK,CHAT
 * @return the type of the message.
 */
public int getType() {
return mType;
}

/**
 * there is a mapping to smack msg type to ours msg type
 * we have ACK msg as headline smack msg
 * @return
 */
/*
public Type getSmackType() {
	Type smackMsgType = org.jivesoftware.smack.packet.Message.Type.normal;
	switch (mType) {
		case MSG_TYPE_CHAT:
			smackMsgType = org.jivesoftware.smack.packet.Message.Type.chat;
		break;
		case MSG_TYPE_NEWUSER_BROADCAST:
			smackMsgType = org.jivesoftware.smack.packet.Message.Type.groupchat ;
		break;
		case MSG_TYPE_NORMAL:
			smackMsgType = org.jivesoftware.smack.packet.Message.Type.normal;
		break;
		case MSG_TYPE_ACK:
			smackMsgType = org.jivesoftware.smack.packet.Message.Type.headline;
		case MSG_TYPE_ERROR:
			smackMsgType = org.jivesoftware.smack.packet.Message.Type.error;
		break;
		default:
		Log.w(TAG, "message type error" );
		break;
	}
	return smackMsgType;
}
*/
/**
 * Set the type of the message.
 * @param type the type to set
 */
public void setType(int type) {
mType = type;
}

/**
 * Get the body of the message.
 * @return the Body of the message
 */
public String getBody() {
return mBody;
}

/**
 * Set the body of the message.
 * @param body the body to set
 */
public void setBody(String body) {
mBody = body;
}

/**
 * Get the subject of the message.
 * @return the subject
 */
public String getSubject() {
return mSubject;
}

/**
 * Set the subject of the message.
 * @param subject the subject to set
 */
public void setSubject(String subject) {
mSubject = subject;
}

/**
 * Get the destinataire of the message.
 * @return the destinataire of the message
 */
public String getTo() {
return mTo;
}

/**
 * Set the destinataire of the message.
 * @param to the destinataire to set
 */
public void setTo(String to) {
mTo = to;
}

/**
 * Set the from field of the message.
 * @param from the mFrom to set
 */
public void setFrom(String from) {
this.mFrom = from;
}

/**
 * Get the from field of the message.
 * @return the mFrom
 */
public String getFrom() {
return mFrom;
}

/**
 * Get the from field of the message.
 * @return the mFrom
 */
public String getReceiver() {
return StringUtils.parseName(mTo);
}

public String getInitiator() {
return StringUtils.parseName(mFrom);
}

/**
 * Get the thread of the message.
 * @return the thread
 */
public String getThread() {
return mThread;
}

/**
 * Set the thread of the message.
 * @param thread the thread to set
 */
public void setThread(String thread) {
mThread = thread;
}

/**
 * {@inheritDoc}
 */
@Override
public int describeContents() {
// TODO Auto-generated method stub
return 0;
}

public String getTimestamp() {
	// TODO Auto-generated method stub
	return mTime;
}

public int getStatus() {
	return mStatus;
}

public void setStatus(int sent) {
	this.mStatus = sent;
}

public long getUniqueMsgIdentifier() {
	return mUniqueMsgIdentifier;
}

public void setUniqueMsgIdentifier(long l) {
	this.mUniqueMsgIdentifier = l;
}

}
