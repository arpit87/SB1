package my.b1701.SB.Activities;

import com.google.analytics.tracking.android.EasyTracker;
import my.b1701.SB.R;
import my.b1701.SB.Fragments.HistoryDailyPoolFragment;
import my.b1701.SB.Fragments.HistoryInstaShareFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SBHistoryActivity extends FragmentActivity{
	FragmentManager fm = this.getSupportFragmentManager();
	Button dailyPool = null;
	Button instaShare = null;
	boolean isShowingDailyPool = false;
		
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
		 dailyPool = (Button)findViewById(R.id.history_dailypool_button);
		 instaShare = (Button)findViewById(R.id.history_instashare_button);
		 showDailypoolHistory();
		 
		 dailyPool.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View paramView) {
                EasyTracker.getTracker().sendEvent("ui_action", "button_press", "dailyPoolHistory_button", 1L);
				showDailypoolHistory();
			}
		});
		 
		 instaShare.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View paramView) {
                    EasyTracker.getTracker().sendEvent("ui_action", "button_press", "instaShareHistory_button", 1L);
					showInstaHistory();
				}
			});
		 
	 }
	 
	 public void showInstaHistory()
	    {
	    	if (fm != null && isShowingDailyPool) {
	            FragmentTransaction fragTrans = fm.beginTransaction();
	            fragTrans.replace(R.id.historyviewcontent, new HistoryInstaShareFragment());
	            //ft.replace(R.id.historyviewcontent, instaFrag);
	            fragTrans.commit();
	            isShowingDailyPool = false;
	        }
	    }
	    
	    public void showDailypoolHistory()
	    {
	    	if (fm != null && !isShowingDailyPool) {	    		
	            FragmentTransaction ft = fm.beginTransaction();
	            ft.replace(R.id.historyviewcontent, new HistoryDailyPoolFragment());
	            ft.commit();
	            isShowingDailyPool = true;
	        }
	    }
	    

}
