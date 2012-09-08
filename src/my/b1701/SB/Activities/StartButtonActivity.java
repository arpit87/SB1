package my.b1701.SB.Activities;

import my.b1701.SB.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartButtonActivity extends Activity {
    /** Called when the activity is first created. */
	
	Button buttonlogin;
	Button buttoncarpool;
	Button buttonhirenow;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startbuttons);
        
        
    }
    
    public void onClickStartButtons(View v) {
        switch (v.getId()) {
        case R.id.buttonhirenow:
        	startActivity(new Intent(this,SBMapViewActivity.class));
        	break;
        }
    }
        

	
}