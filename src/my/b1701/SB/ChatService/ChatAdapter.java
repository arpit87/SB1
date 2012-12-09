package my.b1701.SB.ChatService;

import java.util.LinkedList;
import java.util.List;

import my.b1701.SB.ChatClient.IMessageListener;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;

import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

//there is a chat adapter which 
public class ChatAdapter extends IChatAdapter.Stub{

	private static final int HISTORY_MAX_SIZE = 50;
    private static final String TAG = "ChatAdapter";
    private boolean mIsOpen = false;
    private final Chat mSmackChat;
    private final String mParticipant;    
    private final List<Message> mMessages;   
    SBMsgListener mMsgListener = new SBMsgListener();
    private final RemoteCallbackList<IMessageListener> mRemoteListeners = new RemoteCallbackList<IMessageListener>();
    
    
    public ChatAdapter(final Chat chat) {
    	mSmackChat = chat;
    	mParticipant = chat.getParticipant();
    	mMessages = new LinkedList<Message>();
    	mSmackChat.addMessageListener(mMsgListener);
        }
    
	@Override
	public void sendMessage(Message msg) throws RemoteException{
		org.jivesoftware.smack.packet.Message msgToSend = new org.jivesoftware.smack.packet.Message();
		String msgBody = msg.getBody();
		String to = msg.getTo();
		msgToSend.setTo(to);
		Log.w(TAG, "message sending to " + msg.getTo());
		msgToSend.setBody(msgBody);
		
		try {
			mSmackChat.sendMessage(msgToSend);
		} catch (XMPPException e) {
		    e.printStackTrace();
		}
		
	}
	
	
	 private void addMessage(Message msg) {
			if (mMessages.size() == HISTORY_MAX_SIZE)
			    mMessages.remove(0);
			mMessages.add(msg);
	 }
	
	
	@Override
	public void setOpen(boolean value) throws RemoteException {
		mIsOpen = value;
		
	}

	@Override
	public void addMessageListener(IMessageListener listener)
			throws RemoteException {
		mRemoteListeners.register(listener);
		
	}
	
	@Override
    public void removeMessageListener(IMessageListener listen) {
	if (listen != null) {
	    mRemoteListeners.unregister(listen);
	}
    }
	
	private class SBMsgListener implements MessageListener {		

		@Override
		public void processMessage(Chat chat, org.jivesoftware.smack.packet.Message message) {
		    Message msg = new Message(message);
		    Log.d(TAG, "new msg " + msg.getBody());
		    String body;
		   
		    //TODO add que les message pas de type errors
		    ChatAdapter.this.addMessage(msg);
		    int n = mRemoteListeners.beginBroadcast();
		    for (int i = 0; i < n; i++) {
			IMessageListener listener = mRemoteListeners.getBroadcastItem(i);
			try {
			    if (listener != null)
				listener.processMessage(ChatAdapter.this, msg);
			} catch (RemoteException e) {
			    Log.w(TAG, "Error while diffusing message to listener", e);
			}
		    }
		    mRemoteListeners.finishBroadcast();
		    }		
		    
		}

	@Override
	public boolean isOpen() throws RemoteException {
		return mIsOpen;		
	}

	@Override
	public String getParticipant() throws RemoteException {
		
		return mParticipant;
	}

	@Override
	public List<Message> getMessages() throws RemoteException {		
		return mMessages;
	}

	

}
