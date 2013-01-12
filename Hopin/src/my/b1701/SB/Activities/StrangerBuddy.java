package my.b1701.SB.Activities;

import my.b1701.SB.HelperClasses.SBConnectivity;
import my.b1701.SB.Platform.Platform;
import static org.acra.ReportField.*;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

@ReportsCrashes(formKey = "dDZQYXlhUldnM192YWhpdUhmTm1MLUE6MQ" ,
customReportContent = {APP_VERSION_NAME,
		APP_VERSION_CODE,  PACKAGE_NAME,PHONE_MODEL,BRAND, ANDROID_VERSION,
		TOTAL_MEM_SIZE, AVAILABLE_MEM_SIZE ,CUSTOM_DATA, STACK_TRACE,
		 DISPLAY,USER_APP_START_DATE , USER_CRASH_DATE,LOGCAT },
logcatArguments = { "-t", "100", "-v", "long", "StrangerBuddy:I", "*:D", "*:S" },
mode = ReportingInteractionMode.TOAST,
forceCloseDialogAfterToast = false, // optional, default false
resToastText = my.b1701.SB.R.string.crash_toast_text
) 
public class StrangerBuddy extends Application{
	
	private Context context;
	private Platform platform;	
	private static final String TAG = "my.b1701.SB.Activities.StrangerBuddy";
		
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
		Log.i(TAG,"Platform initialized");
		
	}

	
	

}
