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
import org.jivesoftware.smack.util.StringUtils;

import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.InputFilter.AllCaps;
import android.util.Log;

public class SBChatManager extends IChatManager.Stub {
	
	private ChatManager mChatManager;	
	private XMPPConnection mXMPPConnection = null;
	private Roster mRoster;	
	private static final String TAG = "SBChatManager";   
    private final Map<String, ChatAdapter> mAllChats = new HashMap<String, ChatAdapter>();
    private final SBChatManagerAndInitialMsgListener mChatAndInitialMsgListener = new SBChatManagerAndInitialMsgListener();
    private final RemoteCallbackList<IChatManagerListener> mRemoteChatCreationListeners = new RemoteCallbackList<IChatManagerListener>();	
    private SBChatService mService = null;
    private final SBChatRosterListener mChatRosterListener = new SBChatRosterListener();   
    

	public SBChatManager(XMPPConnection xmppConnection, SBChatService service) {
		this.mXMPPConnection = xmppConnection;
		this.mChatManager = xmppConnection.getChatManager();
		this.mService = service;
		this.mRoster = xmppConnection.getRoster();				
		this.mChatManager.addChatListener(mChatAndInitialMsgListener);		
	}
	
	/**
     * Get an existing ChatAdapter or create it if necessary.
     * @param chat The real instance of smack chat
     * @return a chat adapter register in the manager
     */
    private ChatAdapter getChatAdapter(Chat chat) {
	String key = StringUtils.parseName(chat.getParticipant());
	if (mAllChats.containsKey(key)) {
	    return mAllChats.get(key);
	}
	ChatAdapter newChatAdapter = new ChatAdapter(chat,this);	
	mAllChats.put(key, newChatAdapter);
	return newChatAdapter;
    }
    
    @Override
    public void deleteChatNotification(IChatAdapter chat) {
	mService.deleteNotification(1);
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
			String key = participant+"@54.243.171.212";
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
		String key = participant;
		if (mAllChats.containsKey(key)) {
		    return mAllChats.get(key);
		}
		return null;
    }   
	
	public int numChats()
	{
		return mAllChats.size();
	}
	
	public void notifyChat(int id,String participant) { 
	   			
			
	    	mService.sendNotification(id,participant);
	   
	}
   
	
	
	private class SBChatManagerAndInitialMsgListener implements ChatManagerListener {

		/****
		 * this is initial remote msg listener registered to a newly remote created chat.It is called back only 
		 * till this window not opened by user and it keeps showing incoming msgs as notifications.
		 * as soon the user taps on notification and this chat opens we change msg listener to one with 
		 * client so that further call backs are handled by SBonChatMsgListener
		 */
		/*@Override
		public void processMessage(IChatAdapter chat,
				my.b1701.SB.ChatService.Message msg) throws RemoteException {
			try {
				String body = msg.getBody();
				if (!chat.isOpen() && body != null) {
				    if (chat instanceof ChatAdapter) {
					mAllChats.put(chat.getParticipant(), (ChatAdapter) chat);
				    }
				    //will put it as notification
				    notifyChat(chat, body);
				}
			    } catch (RemoteException e) {
				Log.e(TAG, e.getMessage());
			    }
			
		}*/
		

		@Override
		public void chatCreated(Chat chat, boolean locally) {
			/// no call backs required on remote chat creation as we showing notification
			//till chat window opened by user. As soon he opens we will register msglistener
			//which will then take care of further msgs
			 ChatAdapter newchatAdapter;
			 String key = StringUtils.parseName(chat.getParticipant());
				if (mAllChats.containsKey(key)) {
					newchatAdapter= mAllChats.get(key);
				}
				else
				{
					newchatAdapter = new ChatAdapter(chat,SBChatManager.this);	
					mAllChats.put(key,newchatAdapter);
				}
				//newchatAdapter.addMessageListener(mChatAndInitialMsgListener);
			    Log.d(TAG, "Chat" + chat.toString() + " created locally " + locally + " with " + chat.getParticipant());
			   
		
		}	
		}
		
	
		
	
}
