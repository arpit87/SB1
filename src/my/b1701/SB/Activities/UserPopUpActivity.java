package my.b1701.SB.Activities;

import my.b1701.SB.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserPopUpActivity extends Activity{
	private String userName;
	private String destination;
	private TextView userNameTextView;
	private TextView userDestinationTextView;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mappopup);
        Bundle userDetails = getIntent().getExtras();
        if (userDetails == null) {
    		Log.d("userpopup","no userdetails");
    		}
        else
        {
        	userName=userDetails.getString("username");
        	destination=userDetails.getString("destination");
        	setUserDetails(this.userName,this.destination);        	
        }       
            
        
    }
    
    public void onClickChatWindowButtons(View v) {
        switch (v.getId()) {
        case R.id.mapPopup_close_button:
        	finish();
        	break;
        }
    }  
       
    
    public void setUserDetails(String name,String destination)
    {
    	userNameTextView = (TextView) this.findViewById(R.id.mapPopupName);
    	userDestinationTextView = (TextView) this.findViewById(R.id.mapPopupDestination);
    	//TextView userTimeTextView = (TextView) popupView.findViewById(R.id.mapPopup_time);
    	
    	userNameTextView.setText(this.userName);
    	userDestinationTextView.setText(this.destination);
    	//userTimeTextView.setText(time);
    	
    }
    

}
