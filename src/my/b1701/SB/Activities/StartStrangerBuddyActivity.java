package my.b1701.SB.Activities;


import my.b1701.SB.R;
import my.b1701.SB.LocationHelpers.SBLocationManager;
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
        mProgress = (ProgressBar) findViewById(R.id.authorization_progressBar); 
        
        //Log.i(TAG,"Requesting single update");
        //SBLocationManager.getInstance().requestSingleLocationUpdate();
        //Log.i(TAG,"passed single update");
        SBLocationManager.getInstance().StartListeningtoNetwork();
        Log.i(TAG,"started network listening and map activity in 2 sec");
        final Intent showSBMapViewActivity = new Intent(this, SBMapViewActivity.class);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
          public void run() {        	  
              startActivity(showSBMapViewActivity);
          }
        }, (1000 * 2));
        
      // startActivity(intent);        
        
    }
}