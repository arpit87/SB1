package my.b1701.SB.Activities;

import com.google.analytics.tracking.android.EasyTracker;

import my.b1701.SB.R;
import my.b1701.SB.Fragments.HistoryDailyPoolFragment;
import my.b1701.SB.Fragments.HistoryInstaShareFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class SBHistoryActivity extends FragmentActivity{
	FragmentManager fm = this.getSupportFragmentManager();
	ToggleButton dailyinstatoggle_btn = null;
	Button instaShare = null;	
		
    @Override
    protected void onStart(){
        super.onStart();
        EasyTracker.getInstance().activityStart(this);
    }
    
    @Override
    protected void onStop(){
        super.onStop();
        EasyTracker.getInstance().activityStop(this);
    }
    
	 @Override
	    protected void onCreate(Bundle savedInstanceState){
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.history_layout);
		 dailyinstatoggle_btn = (ToggleButton)findViewById(R.id.history_dailyinsta_toggle_button);
		 showInstaHistory();
		 
		 dailyinstatoggle_btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
					showDailypoolHistory();
				else
					showInstaHistory();
				
			}
		});
				 
	 }
	 
	 public void showInstaHistory()
	    {
	    	if (fm != null) {
	            FragmentTransaction fragTrans = fm.beginTransaction();
	            fragTrans.replace(R.id.historyviewcontent, new HistoryInstaShareFragment());
	            //ft.replace(R.id.historyviewcontent, instaFrag);
	            fragTrans.commit();
	           
	        }
	    }
	    
	    public void showDailypoolHistory()
	    {
	    	if (fm != null) {	    		
	            FragmentTransaction ft = fm.beginTransaction();
	            ft.replace(R.id.historyviewcontent, new HistoryDailyPoolFragment());
	            ft.commit();
	           
	        }
	    }
	    

}
