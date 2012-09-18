package my.b1701.SB.Activities;

import my.b1701.SB.R;
import my.b1701.SB.HelperClasses.Constants;
import FacebookHelpers.FacebookConnector;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;


public class LoginActivity extends Activity  {

	private ProgressBar queryProgressBar;
	private Button fbloginbutton;
	private FacebookConnector fbconnect;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_view);
		queryProgressBar = (ProgressBar) findViewById(R.id.queryLogin_progressBar);
		fbloginbutton = (Button)findViewById(R.id.signInViaFacebook);
		fbconnect = new FacebookConnector(this, Constants.FB_PERMISSIONS);
		
	}
	
	public void onClickButtons(View v)
	{
		switch(v.getId())
		{
			case R.id.signInViaFacebook:
				fbconnect.loginToFB();
		}
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbconnect.authorizeCallback(requestCode, resultCode, data);
    }
	

	public void onStart() {
		super.onStart();
	}

	public void onStop() {
		super.onStop();
		
	}

	public void onResume() {
		super.onResume();
		
	}
	
	
	
	}

	
	