package my.b1701.SB.ChatService;

import java.util.Date;

import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.util.StringUtils;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


public class Message implements Parcelable {

	private static String TAG = "Message";
/** Normal message type. Theese messages are like an email, with subject. */
public static final int MSG_TYPE_NORMAL = 100;

/** Chat message type. */
public static final int MSG_TYPE_CHAT = 200;

/** Group chat message type. */
public static final int MSG_TYPE_GROUP_CHAT = 300;

/** Error message type. */
public static final int MSG_TYPE_ERROR = 400;

public static final int MSG_TYPE_INFO = 500;

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


private int mType;
private String mBody;
private String mSubject;
private String mTo;
private String mFrom;
private String mThread;
private String mTime;

// TODO ajouter l'erreur

/**
 * Constructor.
 * @param to the destinataire of the message
 * @param type the message type
 */
public Message(final String to, final int type) {
mTo = to;
mType = type;
mBody = "";
mSubject = "";
mThread = "";
mFrom = null;
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
this(smackMsg.getTo());
switch (smackMsg.getType()) {
    case chat:
	mType = MSG_TYPE_CHAT;
	break;
    case groupchat:
	mType = MSG_TYPE_GROUP_CHAT;
	break;
    case normal:
	mType = MSG_TYPE_NORMAL;
	break;
    // TODO gerer les message de type error
    // this a little work around waiting for a better handling of error
    // messages
    case error:
	mType = MSG_TYPE_ERROR;
	break;
    default:
	Log.w(TAG, "message type error" + smackMsg.getType());
	break;
}
this.mFrom = StringUtils.parseName(smackMsg.getFrom());
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
}

/**
 * Get the type of the message.
 * @return the type of the message.
 */
public int getType() {
return mType;
}

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

}
