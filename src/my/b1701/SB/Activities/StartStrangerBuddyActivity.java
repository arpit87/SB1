package my.b1701.SB.Activities;


import my.b1701.SB.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StartStrangerBuddyActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
                
       startActivity(new Intent().setClass(this, MyMapView.class));
      // startActivity(intent);
        
        
    }
}