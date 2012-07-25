package my.b1701.SB.Activities;

import my.b1701.SB.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;


public class LoginActivity extends Activity  {

	private ProgressBar queryProgressBar;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_view);	

		queryProgressBar = (ProgressBar) findViewById(R.id.queryLogin_progressBar);
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

	
	