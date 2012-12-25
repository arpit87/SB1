package my.b1701.SB.test;

import my.b1701.SB.R;
import my.b1701.SB.ChatClient.ChatWindow;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.HelperClasses.ToastTracker;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint({ "ParserError", "ShowToast" })
public class ChatTestActivity extends Activity{
	
	private String TAG = "ChatTestActivity";
	private Button chatbutton;
	private Button logoutbutton;
		
	EditText chatToTextView;
	
	boolean mServiceStarted = false;
	
	String chatTo = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chattestlayout);
		
		chatbutton =  (Button)findViewById(R.id.chattestbutton);
		logoutbutton =  (Button)findViewById(R.id.chatlogoutbutton);
		
		chatToTextView =  (EditText)findViewById(R.id.chatto);
		
		
		//startChatService();
		
		logoutbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				ThisUserConfig.getInstance().putString(ThisUserConfig.CHATUSERID, "");
				ThisUserConfig.getInstance().putString(ThisUserConfig.CHATPASSWORD, "");
				finish();
			}
		
		});
		
		chatbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				chatTo = chatToTextView.getText().toString();
				
				if(chatTo == "" )
				{
					ToastTracker.showToast("Enter All Fields", Toast.LENGTH_SHORT);
					return;
				}
								
				Intent startChatIntent = new Intent();			
				startChatIntent.setClass(ChatTestActivity.this, ChatWindow.class);
				startChatIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startChatIntent.putExtra("participant", chatTo);
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
