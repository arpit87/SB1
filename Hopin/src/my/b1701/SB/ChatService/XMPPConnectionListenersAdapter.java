package my.b1701.SB.ChatService;

import my.b1701.SB.ChatClient.ISBChatConnAndMiscListener;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.HelperClasses.ToastTracker;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class XMPPConnectionListenersAdapter {
	
	private final XMPPConnection mXMPPConnection;
	private String TAG = "XMPPConnectionListenersAdapter";
	SBChatService mService = null;
	private String mLogin;
    private String mPassword;
    private SBChatConnectionListener mConnectionListener = new SBChatConnectionListener();
    private String mErrorMsg = "";   
    public boolean isConnected = false;
    public boolean isLoggedIn = false;
    public boolean tryinLogging = false;
	private ConnectToChatServerTask connectToServer = null;
	private LoginToChatServerTask loginToServer = null;
	private SBChatManager mChatManager = null;
	private Handler handler = new Handler();
	private final RemoteCallbackList<ISBChatConnAndMiscListener> mRemoteMiscListeners = new RemoteCallbackList<ISBChatConnAndMiscListener>();
	
	
 public XMPPConnectionListenersAdapter(final ConnectionConfiguration config,  final SBChatService service) {
		this(new XMPPConnection(config), service);
	    }
	
 public void addMiscCallBackListener(ISBChatConnAndMiscListener listener) throws RemoteException {
	if (listener != null)
		mRemoteMiscListeners.register(listener);
 }
	
public void removeMiscCallBackListener(ISBChatConnAndMiscListener listener) throws RemoteException {
	if (listener != null)
		mRemoteMiscListeners.unregister(listener);
 }


	 public XMPPConnectionListenersAdapter(final XMPPConnection con,
			     final SBChatService service) {
		 mXMPPConnection = con;	
		mLogin = ThisUserConfig.getInstance().getString(ThisUserConfig.CHATUSERID);		
		mPassword = ThisUserConfig.getInstance().getString(ThisUserConfig.CHATPASSWORD);
		//mLogin = "test";
		//mPassword = "test";
		mService = service;	
		Log.d(TAG, "xmpp connection listener will connect");
		connectToServer = new ConnectToChatServerTask();
		connectToServer.execute(this);		
		
		
		//try {			
			Toast.makeText(mService, "connecting to xmpp", Toast.LENGTH_SHORT).show();
		//this.connect();
			Log.d(TAG, "connecting to xmpp");
		//} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		//}
		
		//login only if not new user
		//if(ThisUserConfig.getInstance().getBool(ThisUserConfig.FBCHECK))
		//{
	//	Log.d(TAG, "xmpp connection listener will login");
			//try {
			//	Toast.makeText(mService, "logging to xmpp", Toast.LENGTH_SHORT).show();
				//this.login(mLogin, mPassword);
			//	Log.d(TAG, "xmpp connection listener will login");
		//	} catch (RemoteException e) {
				// TODO Auto-generated catch block
		//		e.printStackTrace();
			//}
			
		//}
	 }
	 
	 
	 public SBChatManager getChatManager() {
			return mChatManager;
		}
	 
	 	 
	 public XMPPConnection getmXMPPConnection() {
		return mXMPPConnection;
	}


	public boolean connect() throws RemoteException {
		if (mXMPPConnection.isConnected())
		{
			isConnected = false;	
		    return isConnected;
		}
		else {
		    try {
		    	mXMPPConnection.connect();
		    	mXMPPConnection.addConnectionListener(mConnectionListener);
		    	isConnected = true;			
		    } catch (XMPPException e) {
			Log.e(TAG, "Error while connecting", e);
			mErrorMsg = e.getMessage();
		    }
		    return isConnected;
		    }
	    }

	
	public boolean isAuthenticated()
	{
		if (mXMPPConnection.isAuthenticated())		
			return true;
		else
			return false;
	}
	
	public boolean disconnect() {
		if (mXMPPConnection != null && mXMPPConnection.isConnected())
			mXMPPConnection.disconnect();
		return true;
	    }
	
	
	public void loginAsync(String login,String password)
	{		
		if(tryinLogging)
			return;
		mLogin = login;
		mPassword = password;
		tryinLogging = true;
		if(!isConnected)
		{
			connectToServer = new ConnectToChatServerTask();
			connectToServer.execute(this);
		}
		else
		{			
			loginToServer = new LoginToChatServerTask();
			loginToServer.execute(this);
		}
	}
	
	//this should be called in separate thread
	    private boolean login() throws RemoteException {
	    if( isLoggedIn == true)
	    		return true;
		if (mXMPPConnection.isAuthenticated())
		{
			isLoggedIn = true;
		    return isLoggedIn;
		}
	    	//ToastTracker.showToast("tryin login but xmppconnected?"+mXMPPConnection.isConnected(), Toast.LENGTH_SHORT);
		if (!mXMPPConnection.isConnected())
		{
			isLoggedIn = false;
			Log.d(TAG, "tryin login but xmpp not connected");
			//Toast.makeText(mService, "tryin login but xmpp not connected", Toast.LENGTH_SHORT).show();
		    return isLoggedIn;
		}
		try {
			mXMPPConnection.login(mLogin, mPassword);		    
		    isLoggedIn = true;
		    
		} catch (XMPPException e) {
		    Log.e(TAG, "Error while log in", e);
		    mErrorMsg = "Error while log in";
		    isLoggedIn = false;
		}
		return isLoggedIn;
	    }
	    
	    public String getErrorMessage() {
	    	return mErrorMsg;
	        }
	 
	
private class SBChatConnectionListener implements ConnectionListener {
		
		@Override
		public void connectionClosed() {
		    Log.d(TAG, "closing connection,stopping service");		    
		    //Intent intent = new Intent(BeemBroadcastReceiver.BEEM_CONNECTION_CLOSED);
		    //intent.putExtra("message", mService.getString(R.string.BeemBroadcastReceiverDisconnect));
		    //intent.putExtra("normally", true);
		    //mService.sendBroadcast(intent);
		    mService.stopSelf();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void connectionClosedOnError(Exception exception) {
		    Log.d(TAG, "connectionClosedOnError,stopping service");
		   
		    //Intent intent = new Intent(BeemBroadcastReceiver.BEEM_CONNECTION_CLOSED);
		    //intent.putExtra("message", exception.getMessage());
		    //mService.sendBroadcast(intent);
		    mService.stopSelf();
		}

		/**
		 * Connection failed callback.
		 * @param errorMsg smack failure message
		 */
		public void connectionFailed(String errorMsg) {
		    Log.d(TAG, "Connection Failed");
		  /*  final int n = mRemoteConnListeners.beginBroadcast();

		    for (int i = 0; i < n; i++) {
			IBeemConnectionListener listener = mRemoteConnListeners.getBroadcastItem(i);
			try {
			    if (listener != null)
				listener.connectionFailed(errorMsg);
			} catch (RemoteException e) {
			    // The RemoteCallbackList will take care of removing the
			    // dead listeners.
			    Log.w(TAG, "Error while triggering remote connection listeners", e);
			}
		    }
		    mRemoteConnListeners.finishBroadcast();*/
		   // mService.stopSelf();
		    
		}
		    @Override
			public void reconnectingIn(int paramInt) {
		    	Log.d(TAG, "reconnectingIn"+paramInt);
				
			}

			@Override
			public void reconnectionSuccessful() {
				Log.d(TAG, "reconnection success");
				
			}

			@Override
			public void reconnectionFailed(Exception paramException) {
				Log.d(TAG, "reconnectionFailed Failed");
				
			}
		}

private class ConnectToChatServerTask extends AsyncTask<XMPPConnectionListenersAdapter, Integer, Boolean>
{
	XMPPConnectionListenersAdapter adapter;
	@Override
	protected Boolean doInBackground(XMPPConnectionListenersAdapter... connection) {
		boolean result = true;	
		adapter = connection[0];
		Log.d(TAG, "connecting on separate thread");
		try {
		    publishProgress(25);			    
		    if (!adapter.connect()) {				
			return false;
		    }		    
		    publishProgress(100);			    
		   
		} catch (RemoteException e) {			    
		    result = false;
		}
		return result;
	}	
	
	protected void onPostExecute(Boolean connected) {
	if(connected)
	{		
		if(mLogin != "" && mPassword != ""){
			loginToServer = new LoginToChatServerTask();
			loginToServer.execute(adapter);
			Toast.makeText(mService, "connected to xmpp,logging", Toast.LENGTH_SHORT).show();
			
		}
		else
		{
			Toast.makeText(mService, "connected to xmpp but not logging", Toast.LENGTH_SHORT).show();
			tryinLogging = false;
		}
		
		
		
	}
	}

	
}

private class LoginToChatServerTask extends AsyncTask<XMPPConnectionListenersAdapter, Integer, Boolean>
{
	
	@Override
	protected Boolean doInBackground(XMPPConnectionListenersAdapter... connection) {
		boolean result = true;	
		XMPPConnectionListenersAdapter adapter = connection[0];	
		Log.d(TAG, "logging on separate thread");
		   try{ 
		    if (!adapter.login()) {				
			publishProgress(25);
			return false;
		    }
		    ToastTracker.showToast("logged in to xmpp");
		    publishProgress(100);
		} catch (RemoteException e) {			    
		    result = false;
		}
		return result;
	}	
	
	protected void onPostExecute(Boolean connected) {
			Toast.makeText(mService, "logged in  to xmpp", Toast.LENGTH_SHORT).show();
			tryinLogging = false;
		 	mChatManager = new SBChatManager(mXMPPConnection, mService);
		 	Runnable sendPresence = new Runnable() {
				
				@Override
				public void run() {
					Presence presence = new Presence(Presence.Type.available);
				 	mXMPPConnection.sendPacket(presence);					
				}
			};
			sendPresence.run();
		 	
		 	int n = mRemoteMiscListeners.beginBroadcast();

			for (int i = 0; i < n; i++) {
				ISBChatConnAndMiscListener listener = mRemoteMiscListeners.getBroadcastItem(i);
			    try {
					listener.loggedIn();
					//??can remove before finishbroadcast?
					
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			mRemoteMiscListeners.finishBroadcast();		
			
			
		}

	
}


	

}
