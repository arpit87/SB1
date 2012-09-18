package FacebookHelpers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import my.b1701.SB.HelperClasses.Constants;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class FacebookConnector {
	
	Facebook facebook = new Facebook(Constants.FB_APP_ID); 
    private String [] permissions ;
    Activity underlying_activity = null;
    
    public FacebookConnector(Activity underlying_activity,String[] permissions)
    {
    	this.underlying_activity =  underlying_activity;
    	this.permissions = permissions;
    }
    
    public void logoutFromFB()
    { 
    	String access_token = ThisUserConfig.getInstance().getString(ThisUserConfig.FBACCESSTOKEN);
	    long expires = ThisUserConfig.getInstance().getLong(ThisUserConfig.FBACCESSEXPIRES);
	 
	    if (access_token != "") {
	        facebook.setAccessToken(access_token);
	    }
	 
	    if (expires != -1 && expires != 0) {
	        facebook.setAccessExpires(expires);
	    }
		if (facebook.isSessionValid()) {
		    AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(facebook);
		    asyncRunner.logout(underlying_activity.getBaseContext(), new LogoutRequestListener());		    
		} 
		else
			showToast("Fb session already expired");
    }
    
    public void loginToFB()
    { 		
	    String access_token = ThisUserConfig.getInstance().getString(ThisUserConfig.FBACCESSTOKEN);
	    long expires = ThisUserConfig.getInstance().getLong(ThisUserConfig.FBACCESSEXPIRES);
	 
	    if (access_token != "") {
	        facebook.setAccessToken(access_token);
	    }
	 
	    if (expires != 0 &&  expires != -1) {
	        facebook.setAccessExpires(expires);
	    }
    	
    	 if (!facebook.isSessionValid()) {
    		 facebook.authorize(underlying_activity, permissions, new LoginDialogListener());  
    	 }
    }  
    
    public void authorizeCallback(int requestCode, int resultCode,Intent data)
    {
    	facebook.authorizeCallback(requestCode, resultCode, data);
    }
    
    class LoginDialogListener implements DialogListener {
	    public void onComplete(Bundle values) {
	    	ThisUserConfig.getInstance().putString(ThisUserConfig.FBACCESSTOKEN, facebook.getAccessToken());
        	ThisUserConfig.getInstance().putLong(ThisUserConfig.FBACCESSEXPIRES, facebook.getAccessExpires()); 
        	ThisUserConfig.getInstance().putBool(ThisUserConfig.FBCHECK, true);
        	showToast("Authentication successsful");
        	underlying_activity.finish();
        }
	    
	    public void onFacebookError(FacebookError error) {
	    	showToast("Authentication with Facebook failed!");
	    	underlying_activity.finish();
	    }
	    public void onError(DialogError error) {
	    	showToast("Authentication with Facebook failed!");
	    	underlying_activity.finish();
	    }
	    public void onCancel() {
	    	showToast("Authentication with Facebook cancelled!");
	    	underlying_activity.finish();
	    }
	}

	private void showToast(String message){
		Toast.makeText(underlying_activity, message, Toast.LENGTH_SHORT).show();
	}
	
	class LogoutRequestListener implements RequestListener {
		  public void onComplete(String response, Object state) {
			  ThisUserConfig.getInstance().putString(ThisUserConfig.FBACCESSTOKEN, "");
			  ThisUserConfig.getInstance().putLong(ThisUserConfig.FBACCESSEXPIRES,-1);
			  ThisUserConfig.getInstance().putBool(ThisUserConfig.FBCHECK,false);
			  underlying_activity.runOnUiThread(new Runnable(){
				public void run() {
					showToast("Successfully logged out");
					
				}});
			  underlying_activity.finish();
		  }
		  
		  public void onIOException(IOException e, Object state) {}
		  
		  public void onFileNotFoundException(FileNotFoundException e,
		        Object state) {}
		  
		  public void onMalformedURLException(MalformedURLException e,
		        Object state) {}
		  
		  public void onFacebookError(FacebookError e, Object state) {}
		}
    
}
	


