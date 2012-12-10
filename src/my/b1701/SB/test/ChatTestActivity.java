package my.b1701.SB.test;

import my.b1701.SB.R;
import my.b1701.SB.ChatClient.ChatWindow;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.Platform.Platform;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

@SuppressLint("ParserError")
public class ChatTestActivity extends Activity{
	
	private String TAG = "ChatTestActivity";
	private Button chatbutton;
	EditText usernameTextView;
	EditText passwrodTextView;	
	String username = "";
	boolean mServiceStarted = false;
	String passwrod = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chattestlayout);
		
		chatbutton =  (Button)findViewById(R.id.chattestbutton);
		usernameTextView =  (EditText)findViewById(R.id.chattestlogin);
		passwrodTextView =  (EditText)findViewById(R.id.chattestpassword);
		ThisUserConfig.getInstance().putString(ThisUserConfig.CHATUSERID, "test");
		ThisUserConfig.getInstance().putString(ThisUserConfig.CHATPASSWORD, "test");
		
		//startChatService();
		
		chatbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				username = usernameTextView.getText().toString();				
				
				Intent startChatIntent = new Intent();			
				startChatIntent.setClass(ChatTestActivity.this, ChatWindow.class);
				startChatIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP
			 			| Intent.FLAG_ACTIVITY_NEW_TASK);
				startChatIntent.putExtra("participant", username);
				startActivity(startChatIntent);
				
			}
		});
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		stopChatService();
	}
	
	
	
	
	 private void stopChatService() {
		 if (!mServiceStarted) {
	        	ToastTracker.showToast("service not yet started ");
	        } else {
	          Intent i = new Intent();
	          i.setClassName("my.b1701.Chat", "my.b1701.Chat.SBChatService");
	          stopService(i);
	          mServiceStarted = true;
	          ToastTracker.showToast("service stopped ");
	          Log.d( TAG, "Service started" );
	         }
	             
 }
	
	


 private void startChatService(){
        if (mServiceStarted) {
        	ToastTracker.showToast("service already started ");
        } else {
          Intent i = new Intent();
          i.setClassName("my.b1701.Chat", "my.b1701.Chat.SBChatService");
          startService(i);
          mServiceStarted = true;
          ToastTracker.showToast("service started ");
          Log.d( TAG, "Service started" );
         }
                    
     }

}
