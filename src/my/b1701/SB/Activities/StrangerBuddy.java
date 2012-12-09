package my.b1701.SB.Activities;


import my.b1701.SB.Platform.Platform;
import android.app.Application;
import android.content.Context;
import android.util.Log;

public class StrangerBuddy extends Application{
	
	private Context context;
	private Platform platform;	
	private static final String TAG = "StrangerBuddy";
		
	@Override
	public void onCreate()
	{		
		super.onCreate();
		Log.i(TAG,"App start");
		context = getApplicationContext();
		platform=Platform.getInstance();
		platform.initialize(this);
		//we check on userid which we wipe out on fb logout. User may login as another user
		//for which we will provide different userid
		
		Log.i(TAG,"Platform initialized");
		
	}

	
	

}
