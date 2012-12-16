package my.b1701.SB.Platform;

import my.b1701.SB.HttpClient.SBHttpClient;
import my.b1701.SB.LocationHelpers.SBLocationManager;
import android.content.Context;
import android.os.Handler;

public class Platform {
	private static Platform instance = new Platform();
	private Context context;
	private SBLocationManager locManager;
	private SBHttpClient httpClient;
	private Handler handler;
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

}
