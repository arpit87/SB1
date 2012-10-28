package my.b1701.SB.Activities;


import my.b1701.SB.HelperClasses.ThisAppInstallation;
import my.b1701.SB.LocationHelpers.SBLocationManager;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Users.ThisUser;
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
		Log.i(TAG,"App start");
		super.onCreate();
		context = getApplicationContext();
		platform=Platform.getInstance();
		platform.initialize(context);
		ThisUser.getInstance().setUniqueID(ThisAppInstallation.id(context));
		Log.i(TAG,"Platform initialized");
		
	}

}
