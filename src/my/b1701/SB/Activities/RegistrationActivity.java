package my.b1701.SB.Activities;

import my.b1701.SB.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;

public class RegistrationActivity extends Activity {

	private EditText editFullName;
	private EditText editLogin;
	private EditText editPassword;
	private EditText editRetypePass;
	private ProgressBar queryProgressBar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration_view);
		
		editFullName = (EditText) findViewById(R.id.edit_full_name);
		editLogin = (EditText) findViewById(R.id.edit_login);
		editPassword = (EditText) findViewById(R.id.edit_password);
		editRetypePass = (EditText) findViewById(R.id.edit_retype_pass);
		queryProgressBar = (ProgressBar)findViewById(R.id.queryRegistration_progressBar);
	}

	public void onStart()
	{
	    super.onStart();
	}
	
	public void onStop()
	{
	    super.onStop();	    
	}
	
	
}