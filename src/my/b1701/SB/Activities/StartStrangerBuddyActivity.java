package my.b1701.SB.Activities;


import my.b1701.SB.R;
import my.b1701.SB.HelperClasses.ThisAppConfig;
import my.b1701.SB.HelperClasses.ThisAppInstallation;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.HttpClient.AddUserRequest;
import my.b1701.SB.HttpClient.SBHttpClient;
import my.b1701.SB.HttpClient.SBHttpRequest;
import my.b1701.SB.LocationHelpers.SBLocationManager;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Users.ThisUser;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

public class StartStrangerBuddyActivity extends Activity {
	
	private ProgressBar mProgress;
	private static final String TAG = "StartStrangerBuddyActivity";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main); 
        mProgress = (ProgressBar) findViewById(R.id.hopon_progressBar); 
       
        ThisAppConfig.getInstance().putLong(ThisAppConfig.NETWORKFREQ, 30*1000); //.5 min
        ThisAppConfig.getInstance().putLong(ThisAppConfig.GPSFREQ, 2*60*1000);	 //2 min
        ThisAppConfig.getInstance().putLong(ThisAppConfig.USERCUTOFFDIST,1000);  //1000 meter
        ThisAppConfig.getInstance().putLong(ThisAppConfig.USERPOSCHECKFREQ,2*60*1000);  //2min
        SBLocationManager.getInstance().StartListeningtoNetwork();        
        //SBLocationManager.getInstance().StartListeningtoGPS(ThisAppConfig.getInstance().getLong("gpsfreq"),100);
        Log.i(TAG,"started network listening ");
        if(ThisUserConfig.getInstance().getString(ThisUserConfig.USERID) == "")
			firstRun();		
		else	
		{
			ThisUser.getInstance().setUserID(ThisUserConfig.getInstance().getString(ThisUserConfig.USERID));			
	        final Intent showSBMapViewActivity = new Intent(this, MapListViewTabActivity.class);
	        showSBMapViewActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        Platform.getInstance().getHandler().postDelayed(new Runnable() {
	          public void run() {        	  
	              startActivity(showSBMapViewActivity);
	          }
	        }, (1000 * 2)); 
		}        
    }
    
    private void firstRun() {
		//get user_id from the server
		ToastTracker.showToast("Preparing for first run..");
		String uuid = ThisAppInstallation.id(this.getBaseContext());
		ThisAppConfig.getInstance().putString(ThisAppConfig.APPUUID,uuid);
		SBHttpRequest request = new AddUserRequest(uuid);		
		SBHttpClient.getInstance().executeRequest(request);
		
	}
    
    public void onResume()
    {   	
    	super.onResume();    	
    }
    
    public void onPause()
    {
    	super.onPause();
    	//SBLocationManager.getInstance().StopListeningtoGPS();    	
    	//SBLocationManager.getInstance().StopListeningtoNetwork();
    }
    
    
}