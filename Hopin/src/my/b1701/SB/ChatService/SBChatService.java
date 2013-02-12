package my.b1701.SB.ChatService;

import my.b1701.SB.R;
import my.b1701.SB.ChatClient.ChatWindow;
import my.b1701.SB.HelperClasses.SBConnectivity;
import my.b1701.SB.HelperClasses.ToastTracker;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.XMPPConnection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class SBChatService extends Service {

	private static String TAG = "my.b1701.SB.ChatService.SBChatService";
	private XMPPConnection mXMPPConnection = null;
	NotificationManager mNotificationManager = null;
	private ConnectionConfiguration mConnectionConfiguration = null;
	private XMPPConnectionListenersAdapter mConnectionAdapter;		
	private XMPPAPIs mXMPPAPIs = null;
	private int DEFAULT_XMPP_PORT = 5222;	
	int mPort;
	private SBChatBroadcastReceiver mReceiver = new SBChatBroadcastReceiver();
	private String mHost = "54.243.171.212";
	String mErrorMsg = "";
	private Roster mRoster = null;
	private ConnectionListener connectionListener = null;
	private PacketListener msgListener = null;	
	public static boolean isRunning=false;
	
	 /** Broadcast intent type. */
    public static final String SBCHAT_CONNECTION_CLOSED = "SBConnectionClosed";
    public static final String SBLOGIN_TO_CHAT = "SBLoginToChatServer";

	
	@Override
	public void onCreate(){
		super.onCreate();
		if(isRunning)
		{
			Toast.makeText(this, "Already running ChatService", Toast.LENGTH_SHORT);
			Log.d(TAG, "Already running ChatService");
			return;
		}
		Log.d(TAG, "Started ChatService");
		Toast.makeText(this, "started service", Toast.LENGTH_SHORT).show();		
		registerReceiver(mReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
		registerReceiver(mReceiver, new IntentFilter(SBLOGIN_TO_CHAT));
				
		mPort = DEFAULT_XMPP_PORT;
		
		initializeConfigration();
		//configure(ProviderManager.getInstance());
		mXMPPConnection = new XMPPConnection(mConnectionConfiguration);
		Log.d(TAG, "made xmpp connection");
		//service has connection adapter which has all listeners 
		mConnectionAdapter = new XMPPConnectionListenersAdapter(mXMPPConnection,this);
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);		
		Roster.setDefaultSubscriptionMode(SubscriptionMode.accept_all);	
		
		mXMPPAPIs = new XMPPAPIs(mConnectionAdapter);
		Log.d(TAG, "Created ChatService");
		isRunning = true;
	}
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        ToastTracker.showToast("service strted with id:"+startId);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }
	
	@Override
    public void onDestroy() {
	super.onDestroy();
	ToastTracker.showToast("stopping service and xmpp disconnecting");
	isRunning = false;
	mNotificationManager.cancelAll();
	unregisterReceiver(mReceiver);
	if (mConnectionAdapter.isAuthenticated() && SBConnectivity.isConnected())
		mConnectionAdapter.disconnect();
	Log.i(TAG, "Stopping the service");	
    }
	
	private void initializeConfigration()
	{
		mConnectionConfiguration = new ConnectionConfiguration(mHost, mPort);		
		mConnectionConfiguration.setReconnectionAllowed(true);
		mConnectionConfiguration.setDebuggerEnabled(true);
		mConnectionConfiguration.setSendPresence(false);
	}
	
	@Override
    public IBinder onBind(Intent intent) {
	Log.d(TAG, "ONBIND()");
	return mXMPPAPIs;
    }
	

	public void sendNotification(int id,String participant,String participant_name) {

		 Intent chatIntent = new Intent(this,ChatWindow.class);
		 	chatIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		   chatIntent.putExtra("participant", participant);	
		   chatIntent.putExtra("participant_name", participant_name);
		   Log.i(TAG, "Sending notification") ;	    
		 PendingIntent pintent = PendingIntent.getActivity(this, 0, chatIntent, PendingIntent.FLAG_ONE_SHOT);			
		 
		 Notification notif = new Notification(R.drawable.chat_horn,"New message from:"+participant_name,System.currentTimeMillis());
		 notif.flags |= Notification.FLAG_AUTO_CANCEL;
		 notif.setLatestEventInfo(this, "Tap to open chat", "new message", pintent);
				 /*
         .setContentText("New message from:"+participant)
         .setSmallIcon(R.drawable.chat_horn3)  
         .setAutoCancel(true)
         .setContentIntent(pintent)
         .build();*/
		 
			notif.ledARGB = 0xff0000ff; // Blue color
			notif.ledOnMS = 1000;
			notif.ledOffMS = 1000;
			notif.defaults |= Notification.DEFAULT_LIGHTS;			
			mNotificationManager.notify(id, notif);
			Log.i(TAG, "notification sent") ;
		    }
	 
	 public void deleteNotification(int id) {
			mNotificationManager.cancel(id);
		    }
	
	

class SBChatBroadcastReceiver extends BroadcastReceiver{
   
    @Override
    public void onReceive(final Context context, final Intent intent) {
	String intentAction = intent.getAction();
	if (intentAction.equals(SBCHAT_CONNECTION_CLOSED)) {
	    CharSequence message = intent.getCharSequenceExtra("message");
	    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	    if (context instanceof Activity) {
		Activity act = (Activity) context;
		act.finish();
		// The service will be unbinded in the destroy of the activity.
	    }
	} else if (intentAction.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
	    if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
		Toast.makeText(context, context.getString(R.string.NetworkConnectivityLost),
		    Toast.LENGTH_SHORT).show();
		//context.stopService(new Intent(context, SBChatService.class));
	    }
	} else if(intentAction.equals(SBLOGIN_TO_CHAT))
	{
		String login = intent.getStringExtra("username");
		String password = intent.getStringExtra("password");
		Toast.makeText(context, "tryin loggin from intent",
			    Toast.LENGTH_SHORT).show();
		mConnectionAdapter.loginAsync(login, password);
	}
  }
}
}
