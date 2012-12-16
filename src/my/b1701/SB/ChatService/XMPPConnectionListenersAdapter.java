package my.b1701.SB.ChatService;

import my.b1701.SB.HelperClasses.ToastTracker;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class XMPPConnectionListenersAdapter {
	
	private final XMPPConnection mXMPPConnection;
	private String TAG = "XMPPConnectionListenersAdapter";
	SBChatService mService = null;
	private final String mLogin;
    private final String mPassword;
    private SBChatConnectionListener mConnectionListener = new SBChatConnectionListener();
    private String mErrorMsg = "";   
    public boolean isConnected = false;
    public boolean isLoggedIn = false;
	private ConnectToChatServerTask connectToServer = null;
	private LoginToChatServerTask loginToServer = null;
	private SBChatManager mChatManager = null;
	
	
 public XMPPConnectionListenersAdapter(final ConnectionConfiguration config,  final SBChatService service) {
		this(new XMPPConnection(config), service);
	    }
	

	 public XMPPConnectionListenersAdapter(final XMPPConnection con,
			     final SBChatService service) {
		 mXMPPConnection = con;	
		//mLogin = ThisUserConfig.getInstance().getString(ThisUserConfig.FBUID);		
		//mPassword = ThisUserConfig.getInstance().getString(ThisUserConfig.PASSWORD);
		mLogin = "test";
		mPassword = "test";
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
	
	    public boolean login(String login,String password) throws RemoteException {
	/*	if (mXMPPConnection.isAuthenticated())
		{
			isLoggedIn = true;
		    return isLoggedIn;
		}*/
	    	//ToastTracker.showToast("tryin login but xmppconnected?"+mXMPPConnection.isConnected(), Toast.LENGTH_SHORT);
		if (!mXMPPConnection.isConnected())
		{
			isLoggedIn = false;
			Log.d(TAG, "tryin login but xmpp not connected");
			//Toast.makeText(mService, "tryin login but xmpp not connected", Toast.LENGTH_SHORT).show();
		    return isLoggedIn;
		}
		try {
			mXMPPConnection.login(login, password);		    
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
		Toast.makeText(mService, "connected to xmpp", Toast.LENGTH_SHORT).show();
		loginToServer = new LoginToChatServerTask();
		loginToServer.execute(adapter);
		
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
		    if (!adapter.login(mLogin,mPassword)) {				
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
		 	mChatManager = new SBChatManager(mXMPPConnection, mService);
		 	Presence presence = new Presence(Presence.Type.available);
		 	mXMPPConnection.sendPacket(presence);
		}

	
}


	

}
