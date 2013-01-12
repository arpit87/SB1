package my.b1701.SB.test;

import my.b1701.SB.R;
import my.b1701.SB.ChatClient.ISBChatConnAndMiscListener;
import my.b1701.SB.ChatService.IXMPPAPIs;
import my.b1701.SB.ChatService.SBChatService;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.HelperClasses.ToastTracker;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChatLoginActivity extends Activity{
	
	private String TAG = "ChatTestActivity";
	private Button chatbutton;
	private Button loginbutton;
	EditText usernameTextView;
	EditText passwrodTextView;	
	EditText chatToTextView;
	String mUsername = "";
	boolean mServiceStarted = false;
	String mPassword = "";
	private ProgressDialog progressDialog;
	private IXMPPAPIs xmppApis = null;
	private ISBChatConnAndMiscListener mCharServiceConnMiscListener = new SBChatServiceConnAndMiscListener();
	private boolean mBinded = false;
	private final ChatServiceConnection mChatServiceConnection = new ChatServiceConnection();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.chattestloginlayout);

		loginbutton =  (Button)findViewById(R.id.chatloginbutton);
		usernameTextView =  (EditText)findViewById(R.id.chattestlogin);
		passwrodTextView =  (EditText)findViewById(R.id.chattestpassword);
		
		mPassword = ThisUserConfig.getInstance().getString(ThisUserConfig.CHATPASSWORD);
		if(mPassword != "")
		{
			mUsername = ThisUserConfig.getInstance().getString(ThisUserConfig.CHATUSERID);
			mPassword = ThisUserConfig.getInstance().getString(ThisUserConfig.CHATPASSWORD);
			usernameTextView.setText(mUsername);
			passwrodTextView.setText(mPassword);
			progressDialog = ProgressDialog.show(ChatLoginActivity.this, "Logging in", "Please wait..", true);
			progressDialog.setCancelable(true);
			if (!mBinded) 
				bindToServiceAndLoginWithCallBack();
			else
				loginWithCallBack();
			return;
		}
		
		
		
		
		
		//startChatService();
		
		loginbutton.setOnClickListener(new OnClickListener() {

			

			@Override
			public void onClick(View paramView) {
				mUsername = usernameTextView.getText().toString();	
				mPassword = passwrodTextView.getText().toString();
				
				if(mUsername == "" || mPassword == "" )
				{
					ToastTracker.showToast("Enter All Fields", Toast.LENGTH_SHORT);
					return;
				}
				
				progressDialog = ProgressDialog.show(ChatLoginActivity.this, "Logging in", "Please wait..", true);
				ThisUserConfig.getInstance().putString(ThisUserConfig.CHATUSERID, mUsername);
				ThisUserConfig.getInstance().putString(ThisUserConfig.CHATPASSWORD, mPassword);
				if (!mBinded) 
					bindToServiceAndLoginWithCallBack();
				//Intent i = new Intent();
				//i.setAction(my.b1701.SB.ChatService.SBChatService.SBLOGIN_TO_CHAT);
				//sendBroadcast(i);
				
			}
		
		});
		
	}
	
	private void loginWithCallBack()
	{
		if(xmppApis != null)
		{
		try {
			xmppApis.loginWithCallBack(mUsername, mPassword, mCharServiceConnMiscListener);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  	
		}
		else
		{
			if(progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}
			ToastTracker.showToast("unable to login");
		}
			
	}
	
	private void bindToServiceAndLoginWithCallBack() {
        Log.d( TAG, "binding chat to service" );        
    	
       Intent i = new Intent(getApplicationContext(),SBChatService.class);
      
       getApplicationContext().bindService(i, mChatServiceConnection, BIND_AUTO_CREATE);	  
       mBinded = true;    
}
	
	private void releaseService() {
		if(mChatServiceConnection != null) {
			getApplicationContext().unbindService(mChatServiceConnection);    			   			
			Log.d( TAG, "chat Service released from chatwindow" );
		} else {
			 ToastTracker.showToast("Cannot unbind - service not bound", Toast.LENGTH_SHORT);
		}
    }
	
	@Override
	public void onDestroy(){
		super.onDestroy();		
	}
	
  
 private final class ChatServiceConnection implements ServiceConnection{
 	
 	@Override
 	public void onServiceConnected(ComponentName className, IBinder boundService) {
 		ToastTracker.showToast("onServiceConnected called", Toast.LENGTH_SHORT);
 		Log.d(TAG,"onServiceConnected called");
 		xmppApis = IXMPPAPIs.Stub.asInterface((IBinder)boundService);
 		loginWithCallBack();
 		Log.d(TAG,"service connected");
 	}

 	@Override
 	public void onServiceDisconnected(ComponentName arg0) {
 		ToastTracker.showToast("onService disconnected", Toast.LENGTH_SHORT);
 		xmppApis = null;	    		
 	    
 		Log.d(TAG,"service disconnected");
 	}

 } 
 
 private class SBChatServiceConnAndMiscListener extends ISBChatConnAndMiscListener.Stub{

		

		@Override
		public void loggedIn() throws RemoteException {
			if(progressDialog.isShowing())
			{
				progressDialog.dismiss();		
				releaseService();
				Intent i = new Intent(ChatLoginActivity.this,ChatTestActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(i);
				finish();
			}
			ToastTracker.showToast("chatwindow callback,loggedin");		
		}

		@Override
		public void connectionClosed() throws RemoteException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void connectionClosedOnError() throws RemoteException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void reconnectingIn(int seconds) throws RemoteException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void reconnectionFailed() throws RemoteException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void reconnectionSuccessful() throws RemoteException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void connectionFailed(String errorMsg) throws RemoteException {
			// TODO Auto-generated method stub
			
		}
		
	}

}
