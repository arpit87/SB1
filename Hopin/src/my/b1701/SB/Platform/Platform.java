package my.b1701.SB.Platform;

import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.HttpClient.SBHttpClient;
import my.b1701.SB.LocationHelpers.SBLocationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class Platform {
	
	private final String TAG = "my.b1701.SB.Platform.Platform";
	private static Platform instance = new Platform();
	private Context context;
	private SBLocationManager locManager;
	private SBHttpClient httpClient;
	private Handler handler;
	public boolean SUPPORTS_NEWAPI = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD;
	
		
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
		
		
	}
	
	
	 public void stopChatService() {		
	          Intent i = new Intent("my.b1701.SB.ChatService.SBChatService");
	          context.stopService(i);	          
	          ToastTracker.showToast("service stopped ");
	          Log.d( TAG, "Service stopped" );	         
	             
 }

}
