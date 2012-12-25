package my.b1701.SB.Activities;


import my.b1701.SB.Platform.Platform;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.acra.*;
import org.acra.annotation.*;

@ReportsCrashes(formKey = "dFk3X3UtSjI5WVRNR0YzN0NScFk3R2c6MQ",mailTo = "strangerbuddy@googlegroups.com",
customReportContent = { ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT },
logcatArguments = { "-t", "100", "-v", "long", "ActivityManager:I", "MyApp:D", "*:S" }
)
public class StrangerBuddy extends Application{
	
	private Context context;
	private Platform platform;	
	private static final String TAG = "StrangerBuddy";
		
	@Override
	public void onCreate()
	{		
		super.onCreate();
		ACRA.init(this);
		Log.i(TAG,"App start");
		context = getApplicationContext();
		platform=Platform.getInstance();
		platform.initialize(this);
		//we check on userid which we wipe out on fb logout. User may login as another user
		//for which we will provide different userid
		Toast.makeText(context, "Platfoem init", Toast.LENGTH_SHORT);
		Log.i(TAG,"Platform initialized");
		
	}

	
	

}
