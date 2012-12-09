package my.b1701.SB.ChatClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import my.b1701.SB.R;
import my.b1701.SB.ChatService.IChatAdapter;
import my.b1701.SB.ChatService.IChatManager;
import my.b1701.SB.ChatService.IXMPPAPIs;
import my.b1701.SB.ChatService.Message;
import my.b1701.SB.ChatService.SBChatService;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.Platform.Platform;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatWindow extends Activity{
	
	//private static final Intent SERVICE_INTENT = new Intent();

	//static {
	//	SERVICE_INTENT.setComponent(new ComponentName("my.b1701.SB.ChatService", "my.b1701.SB.ChatService.SBChatService"));
	 //   }
	private static String TAG = "ChatWindow";
	private IXMPPAPIs xmppApis = null;
	 private TextView mContactNameTextView;
    private TextView mContactStatusMsgTextView;	 
    private TextView mContactDestination;	
    private ImageView mContactPic;    
    private ListView mMessagesListView;
    private EditText mInputField;
    private Button mSendButton;
    private IChatAdapter chatAdapter;
    private IChatManager mChatManager;
    private IChatManagerListener mChatManagerListener = new ChatManagerListener();
    private IMessageListener mMessageListener = new SBOnChatMessageListener();
    private final ChatServiceConnection mChatServiceConnection = new ChatServiceConnection();
    private String mParticipant = "";    
    private boolean selfInitiated = true;
    private String mThisUserChatID = "";
    private SBBroadcastReceiver mSBBroadcastReceiver = new SBBroadcastReceiver();
    Handler mHandler = new Handler();
    private SBChatListViewAdapter mMessagesListAdapter = new SBChatListViewAdapter();
    
		    
	    @Override
		public void onCreate(Bundle savedInstanceState) {	    	
		super.onCreate(savedInstanceState);			
		setContentView(R.layout.chatwindow);
		this.registerReceiver(mSBBroadcastReceiver, new IntentFilter(SBBroadcastReceiver.SBCHAT_CONNECTION_CLOSED));
	    mContactNameTextView = (TextView) findViewById(R.id.chat_contact_name);
	    mContactStatusMsgTextView = (TextView) findViewById(R.id.chat_contact_status_msg);
	    mContactDestination = (TextView) findViewById(R.id.chat_contact_destination);	    
	    mContactPic = (ImageView) findViewById(R.id.chat_contact_pic);
	    mMessagesListView = (ListView) findViewById(R.id.chat_messages);
	    mMessagesListView.setAdapter(mMessagesListAdapter);
	    mInputField = (EditText) findViewById(R.id.chat_input);		
		mInputField.requestFocus();
		mSendButton = (Button) findViewById(R.id.chat_send_message);
		mSendButton.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
			sendMessage();
		    }
		});
		mThisUserChatID = ThisUserConfig.getInstance().getString(ThisUserConfig.FBUID);
}

@Override
public void onResume() {
	super.onResume();
	mParticipant = getIntent().getStringExtra("participant");
	selfInitiated = getIntent().getBooleanExtra("selfInitiated",true);
	
	//if other party initiates
	if(!selfInitiated)
	{
		String from = getIntent().getStringExtra("from");
		try {
			changeCurrentChat(from);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//String fromMessage = getIntent().getStringExtra("frommessage");
		//mMessagesListAdapter.addMessage(new SBChatMessage(from, from, fromMessage, false, new Date().toString()));    
		//mMessagesListAdapter.notifyDataSetChanged();		
	}
	//setTitle(getString(R.string.conversation_name) +": " +jid);
	bindToService();
}
	    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    	releaseService();
    }   
    
    protected void onPause() {
    	super.onPause();
    	releaseService();
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
	super.onNewIntent(intent);
	setIntent(intent);
    }
    
	    private void bindToService() {
	        if(mChatServiceConnection == null)
	        	Toast.makeText(this, "conn obj null!!", Toast.LENGTH_SHORT);
	        	 Log.d( TAG, "binding chat to service" );        
	        	
	           Intent i = new Intent(getApplicationContext(),SBChatService.class);
	          
	           getApplicationContext().bindService(i, mChatServiceConnection, BIND_AUTO_CREATE);	           
	           Log.d( TAG, "service bound" );	        
	        
	   }
	    
	    private void releaseService() {
    		if(mChatServiceConnection != null) {
    			unbindService(mChatServiceConnection);    			   			
    			Log.d( TAG, "chat Service released from chatwindow" );
    		} else {
    			 ToastTracker.showToast("Cannot unbind - service not bound", Toast.LENGTH_SHORT);
    		}
	    }
	    
	    private void sendMessage() {
		final String inputContent = mInputField.getText().toString();		
		if(!"".equals(inputContent))
		{
			Message newMessage = new Message(mParticipant,Message.MSG_TYPE_CHAT);
			newMessage.setBody(inputContent);
			
			//send msg to xmpp
			 try {
					if (chatAdapter == null) {
						if(mChatManager == null)
						{
							try {
								mChatManager = xmppApis.getChatManager();
			    			if (mChatManager != null) {
			    			    mChatManager.addChatCreationListener(mChatManagerListener);
			    			    //changeCurrentChat(thisUserID);
			    			}
			    		    } catch (RemoteException e) {
			    			Log.e(TAG, e.getMessage());
			    		    }   
						}							
						chatAdapter = mChatManager.createChat(mParticipant, mMessageListener);
						chatAdapter.setOpen(true);
					}
					chatAdapter.sendMessage(newMessage);
				    } catch (RemoteException e) {
					Log.e(TAG, e.getMessage());
				    }
			 
			 	//now update on our view
			 	final String self = "me";
			    SBChatMessage lastMessage = null;
			    if (mMessagesListAdapter.getCount() != 0)
			    lastMessage = (SBChatMessage) mMessagesListAdapter.getItem(mMessagesListAdapter.getCount() - 1);

			    if (lastMessage != null && lastMessage.getParticipant().equals(self)) {
				lastMessage.setMessage(lastMessage.getMessage().concat("\n" + inputContent));
				lastMessage.setTimestamp(new Date().toString());
				mMessagesListAdapter.setMessage(mMessagesListAdapter.getCount() - 1, lastMessage);
			    } else{
			    mMessagesListAdapter.addMessage(new SBChatMessage(self, self, inputContent, false, new Date().toString()));
			    }
			    mMessagesListAdapter.notifyDataSetChanged();
		   
		}			   
		    mInputField.setText(null);
		}
	    
	    private void changeCurrentChat(String participant) throws RemoteException {
	    	if (chatAdapter != null) {
	    		chatAdapter.setOpen(false);
	    		chatAdapter.removeMessageListener(mMessageListener);
	    	}
	    	chatAdapter = mChatManager.getChat(participant);
	    	if (chatAdapter != null) {
	    		chatAdapter.setOpen(true);
	    		chatAdapter.addMessageListener(mMessageListener);
	    	    mChatManager.deleteChatNotification(chatAdapter);
	    	    
	    	}
	    	mContactNameTextView.setText(participant);
	    	playRegisteredTranscript();
	        }
	    
	    /**
	     * Get all messages from the current chat and refresh the activity with them.
	     * @throws RemoteException If a Binder remote-invocation error occurred.
	     */
	    private void playRegisteredTranscript() throws RemoteException {
	    	mMessagesListAdapter.clearList();
		if (chatAdapter != null) {
		    List<SBChatMessage> msgList = convertMessagesList(chatAdapter.getMessages());
		    mMessagesListAdapter.addAllToList(msgList);
		    mMessagesListAdapter.notifyDataSetChanged();
		}
	    }

	    /**
	     * Convert a list of Message coming from the service to a list of MessageText that can be displayed in UI.
	     * @param chatMessages the list of Message
	     * @return a list of message that can be displayed.
	     */
	    private List<SBChatMessage> convertMessagesList(List<Message> chatMessages) {
		List<SBChatMessage> result = new ArrayList<SBChatMessage>(chatMessages.size());
		String remoteName = "me";
		String localName = "me";
		SBChatMessage lastMessage = null;
		for (Message m : chatMessages) {
		    String name = remoteName;		    
		    if (m.getType() == Message.MSG_TYPE_ERROR) {
			lastMessage = null;
			result.add(new SBChatMessage(name, name, m.getBody(), true, m.getTimestamp()));
		    } else if  (m.getType() == Message.MSG_TYPE_INFO) {
			lastMessage = new SBChatMessage("", "", m.getBody(), false);
			result.add(lastMessage);

		    } else if (m.getType() == Message.MSG_TYPE_CHAT) {
			if (m.getFrom() == null) { //nofrom or from == yours
			    name = localName;			    
			}

			if (m.getBody() != null) {
			    if (lastMessage == null ) {
				lastMessage = new SBChatMessage(localName, name, m.getBody(), false, m.getTimestamp());
				result.add(lastMessage);
			    } else {
				lastMessage.setMessage(lastMessage.getMessage().concat("\n" + m.getBody()));
			    }
			}
		    }
		}
		return result;
	    }
	    
	    
	    private final class ChatServiceConnection implements ServiceConnection{
	    	
	    	public void ChatServiceConnection(){
	    		 Log.d( TAG, "ChatServiceConnection mades" );
	    		ToastTracker.showToast("build on connection obj", Toast.LENGTH_SHORT);
	    	}
	    	
	    	@Override
	    	public void onServiceConnected(ComponentName className, IBinder boundService) {
	    		ToastTracker.showToast("onServiceConnected called", Toast.LENGTH_SHORT);
	    		Log.d(TAG,"onServiceConnected called");
	    		xmppApis = IXMPPAPIs.Stub.asInterface((IBinder)boundService);
	    		    try {	    			
	    			mChatManager = xmppApis.getChatManager();
	    			if (mChatManager != null) {
	    			    mChatManager.addChatCreationListener(mChatManagerListener);
	    			    //changeCurrentChat(thisUserID);
	    			}
	    		    } catch (RemoteException e) {
	    			Log.e(TAG, e.getMessage());
	    		    }   
	    		
	    		Log.d(TAG,"service connected");
	    	}

	    	@Override
	    	public void onServiceDisconnected(ComponentName arg0) {
	    		xmppApis = null;	    		
	    	    try {	    		
	    		mChatManager.removeChatCreationListener(mChatManagerListener);
	    	    } catch (RemoteException e) {
	    		Log.e(TAG, e.getMessage());
	    	    }
	    		Log.d(TAG,"service disconnected");
	    	}

	    } 
	    
 
//this is callback method executed on client when ChatService receives a message	    
private class SBOnChatMessageListener extends IMessageListener.Stub {
	@Override
	public void processMessage(IChatAdapter chat, final Message msg)
			throws RemoteException {		
		
				mHandler.post(new Runnable() {

				    @Override
				    public void run() {
					if (msg.getType() == Message.MSG_TYPE_ERROR) {
					   // mMessagesListAdapter.notifyDataSetChanged();
					} else if (msg.getBody() != null) {
					    SBChatMessage lastMessage = null;
					    String self = "me";
					    if (mMessagesListAdapter.getCount() != 0)
					    	lastMessage = (SBChatMessage) mMessagesListAdapter.getItem(mMessagesListAdapter.getCount()-1);

					    if (lastMessage != null && !lastMessage.getParticipant().equals(self)) {
					    	lastMessage.setMessage(lastMessage.getMessage().concat("\n" + msg.getBody()));
					    	lastMessage.setTimestamp(msg.getTimestamp());					    
					    	mMessagesListAdapter.setMessage(mMessagesListAdapter.getCount() - 1, lastMessage);
					    
					    } else if (msg.getBody() != null){
					    	mMessagesListAdapter.addMessage(new SBChatMessage(msg.getFrom(), msg.getFrom(), msg.getBody(),
						    false, msg.getTimestamp()));}
					    mMessagesListAdapter.notifyDataSetChanged();
					
				    }}
				    
				});
	
	    }
	}

//this is the callback class to track chatmanger on ChatService
private class ChatManagerListener extends IChatManagerListener.Stub {

	@Override
	public void chatCreated(IChatAdapter chat, boolean locally) {
	    if (locally)
		return;
	    try {
	    	mParticipant = chat.getParticipant();
	    	//changeCurrentChat(mParticipant);
		//String chatJid = chat.getParticipant().getJIDWithRes();
		
		    if (chatAdapter != null) {
		    	chatAdapter.setOpen(false);
		    	chatAdapter.removeMessageListener(mMessageListener);
		    }
		    chatAdapter = chat;
		    chatAdapter.setOpen(true);
		    chatAdapter.addMessageListener(mMessageListener);
		    mChatManager.deleteChatNotification(chatAdapter);
		
	    } catch (RemoteException ex) {
		Log.e(TAG, "A remote exception occurs during the creation of a chat", ex);
	    }
	}
    }


	    
}
