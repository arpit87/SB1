package my.b1701.SB.ChatService;

import java.util.HashMap;
import java.util.Map;

import my.b1701.SB.ChatClient.IChatManagerListener;
import my.b1701.SB.ChatClient.IMessageListener;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.XMPPConnection;

import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

public class SBChatManager extends IChatManager.Stub {
	
	private ChatManager mChatManager;	
	private XMPPConnection mXMPPConnection = null;
	private Roster mRoster;	
	private static final String TAG = "SBChatManager";   
    private final Map<String, ChatAdapter> mAllChats = new HashMap<String, ChatAdapter>();
    private final SBChatManagerAndMsgListener mChatListener = new SBChatManagerAndMsgListener();
    private final RemoteCallbackList<IChatManagerListener> mRemoteChatCreationListeners = new RemoteCallbackList<IChatManagerListener>();	
    private SBChatService mService = null;
    private final SBChatRosterListener mChatRosterListener = new SBChatRosterListener();   
    

	public SBChatManager(XMPPConnection xmppConnection, SBChatService service) {
		this.mXMPPConnection = xmppConnection;
		this.mChatManager = xmppConnection.getChatManager();
		this.mService = service;
		this.mRoster = xmppConnection.getRoster();		
		this.mRoster.setSubscriptionMode(SubscriptionMode.accept_all);
		this.mChatManager.addChatListener(mChatListener);		
	}
	
	/**
     * Get an existing ChatAdapter or create it if necessary.
     * @param chat The real instance of smack chat
     * @return a chat adapter register in the manager
     */
    private ChatAdapter getChatAdapter(Chat chat) {
	String key = chat.getParticipant();
	if (mAllChats.containsKey(key)) {
	    return mAllChats.get(key);
	}
	ChatAdapter newChatAdapter = new ChatAdapter(chat);	
	mAllChats.put(key, newChatAdapter);
	return newChatAdapter;
    }
    
    @Override
    public void deleteChatNotification(IChatAdapter chat) {
	try {
	    mService.deleteNotification(Integer.parseInt(chat.getParticipant()));
	} catch (RemoteException e) {
	    Log.v(TAG, "Remote exception ", e);
	}
    }
    
    @Override
    public void addChatCreationListener(IChatManagerListener listener) throws RemoteException {
	if (listener != null)
	    mRemoteChatCreationListeners.register(listener);
    }
	
 @Override
    public void removeChatCreationListener(IChatManagerListener listener) throws RemoteException {
	if (listener != null)
	    mRemoteChatCreationListeners.unregister(listener);
    }

	@Override
	public IChatAdapter createChat(String participant, IMessageListener listener) throws RemoteException {
			String key = participant;
			ChatAdapter chatAdapter;
			if (mAllChats.containsKey(key)) {
				chatAdapter = mAllChats.get(key);
				chatAdapter.addMessageListener(listener);
			    return chatAdapter;
			}
			Chat c = mChatManager.createChat(key, null);
			// maybe a little probleme of thread synchronization
			// if so use an HashTable instead of a HashMap for mChats
			chatAdapter = getChatAdapter(c);
			chatAdapter.addMessageListener(listener);
			return chatAdapter;
		    }
	
	@Override
    public ChatAdapter getChat(String participant) {	
	return mAllChats.get(participant);
    }   
   
	
	private class SBChatManagerAndMsgListener extends IMessageListener.Stub implements ChatManagerListener {


		@Override
		public void processMessage(IChatAdapter chat,
				my.b1701.SB.ChatService.Message msg) throws RemoteException {
			try {
				String body = msg.getBody();
				if (!chat.isOpen() && body != null) {
				    if (chat instanceof ChatAdapter) {
					mAllChats.put(chat.getParticipant(), (ChatAdapter) chat);
				    }
				    //will put it as notification
				    notifyNewChat(chat, body);
				}
			    } catch (RemoteException e) {
				Log.e(TAG, e.getMessage());
			    }
			
		}		
		

		@Override
		public void chatCreated(Chat chat, boolean locally) {
			 ChatAdapter newchat;
			 String key = chat.getParticipant();
				if (mAllChats.containsKey(key)) {
					newchat= mAllChats.get(key);
				}
				else
					newchat = new ChatAdapter(chat);
				
			    Log.d(TAG, "Chat" + chat.toString() + " created locally " + locally + " with " + chat.getParticipant());
			    try {
				newchat.addMessageListener(mChatListener);
				final int n = mRemoteChatCreationListeners.beginBroadcast();

				for (int i = 0; i < n; i++) {
				    IChatManagerListener listener = mRemoteChatCreationListeners.getBroadcastItem(i);
				    listener.chatCreated(newchat, locally);
				}
				mRemoteChatCreationListeners.finishBroadcast();
			    } catch (RemoteException e) {
				// The RemoteCallbackList will take care of removing the
				// dead listeners.
				Log.w(TAG, " Error while triggering remote connection listeners in chat creation", e);
			    }
			}
		
		private void notifyNewChat(IChatAdapter chat, String msgBody) {		    
		    try {			
		    	mService.sendNotification(chat.getParticipant(), msgBody);
		    } catch (RemoteException e) {
			Log.e(TAG, e.getMessage());
		    }
		}
		
			
		}
		
	
		
	
}
