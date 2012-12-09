package my.b1701.SB.ChatService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import my.b1701.SB.ChatClient.IChatManagerListener;
import my.b1701.SB.ChatClient.IMessageListener;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

public class SBChatManager extends IChatManager.Stub {
	
	private ChatManager mChatManager;	
	private XMPPConnection mXMPPConnection = null;
	private Roster mRoster;	
	private static final String TAG = "SBChatManager";   
    private final Map<String, ChatAdapter> mAllChats = new HashMap<String, ChatAdapter>();
    private final SBChatManagerListener mChatListener = new SBChatManagerListener();
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
 
    
    //any msg will be sent by chat adapter of that chat
    public boolean sendMessage(String participant, String message)
    {    	
    	ChatAdapter thisChatAdapter = null;
    	if (mAllChats.containsKey(participant))
    		thisChatAdapter = mAllChats.get(participant);
    	else
    	{
    		Chat newChat = mChatManager.createChat(participant, new SBSelfStartMessageListener());
    		thisChatAdapter = new ChatAdapter(newChat);
    		mAllChats.put(participant, thisChatAdapter);
    	}    	
    	return true;
    }

	
	private class SBChatManagerListener implements ChatManagerListener{

		@Override
		public void chatCreated(Chat chat, boolean createdLocally) {
			
			
			if(!createdLocally && !mAllChats.containsKey(chat.getParticipant()))
			{
				chat.addMessageListener(new SBRemoteStartMessageListener());
			}				
		  
		    Log.d(TAG, "Chat" + chat.toString() + " created locally " + createdLocally + " with " + chat.getParticipant());
	    		
	    	
		}				
				
	} 
	
	private class SBRosterListener implements RosterListener{

		@Override
		public void entriesAdded(Collection<String> paramCollection) {
			
			
		}

		@Override
		public void entriesUpdated(Collection<String> paramCollection) {
			
			
		}

		@Override
		public void entriesDeleted(Collection<String> paramCollection) {
			
			
		}

		@Override
		public void presenceChanged(Presence paramPresence) {
			
			
		}
		
	}
	
	private class SBRemoteStartMessageListener implements MessageListener{

		@Override
		public void processMessage(Chat chat, Message msg) {			
			mService.sendNotification(Integer.parseInt(chat.getParticipant()),msg.getBody());			
			mAllChats.put(chat.getParticipant(), new ChatAdapter(chat));
		}
		
	}
	
	private class SBSelfStartMessageListener implements MessageListener{

		@Override
		public void processMessage(Chat chat, Message msg) {			
			
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

}
