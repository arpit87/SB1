package my.b1701.SB.Platform;

import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.HttpClient.SBHttpClient;
import my.b1701.SB.LocationHelpers.SBLocationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class Platform {
	
	private final String TAG = "Platform"; 
	private static Platform instance = new Platform();
	private Context context;
	private SBLocationManager locManager;
	private SBHttpClient httpClient;
	private Handler handler;
	private boolean mServiceStarted = false;
	
	private Platform() {
	}
	
	public static Platform getInstance()
	{
		return instance;
	}
	
	public Context getContext(){
		return context;
	}	
	
	public Handler getHandler(){
		return handler;
	}
	
	public void initialize(Context context) {
		this.context= context;
		locManager = SBLocationManager.getInstance();
		httpClient = SBHttpClient.getInstance();
		handler = new Handler();
		
		//this might only connect to xmpp server and not login if new user and not yet fb login
		startChatService();
	}
	
	 private void startChatService(){
	        if (mServiceStarted) {
	        	ToastTracker.showToast("service already started ");
	        } else {
	          Intent i = new Intent("my.b1701.SB.ChatService.SBChatService");
	         // i.setClassName("my.b1701.SB.ChatService", "my.b1701.SB.ChatService.SBChatService");
	          context.startService(i);
	          mServiceStarted = true;
	          //ToastTracker.showToast("service started ");
	          //Log.d( TAG, "Service started" );
	         }
	                    
	     }
	
	 private void stopChatService() {
		 if (!mServiceStarted) {
	        	ToastTracker.showToast("service not yet started ");
	        } else {
	          Intent i = new Intent();
	          i.setClassName("my.b1701.Chat", "my.b1701.Chat.SBChatService");
	          context.stopService(i);
	          mServiceStarted = true;
	          ToastTracker.showToast("service stopped ");
	          Log.d( TAG, "Service started" );
	         }
	             
 }

}
