package my.b1701.SB.FacebookHelpers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import my.b1701.SB.HelperClasses.ProgressHandler;
import my.b1701.SB.HelperClasses.Store;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.HttpClient.ChatServiceCreateUser;
import my.b1701.SB.HttpClient.SBHttpClient;
import my.b1701.SB.HttpClient.SBHttpRequest;
import my.b1701.SB.HttpClient.SaveFBInfoRequest;
import my.b1701.SB.Util.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

public class FacebookConnector {
	
	private static final String TAG = "FacebookConnector";
	
	public static String [] FB_PERMISSIONS = {"user_about_me","user_education_history","user_hometown","user_work_history"};
	public static String FB_APP_ID = "486912421326659";
	
	
	public static Facebook facebook = new Facebook(FB_APP_ID);
	public static AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
    private String [] permissions ;
    Activity underlying_activity = null;
    

    
    public FacebookConnector(Activity underlying_activity)
    {
    	this.underlying_activity =  underlying_activity;
    	this.permissions = FB_PERMISSIONS;
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
			ProgressHandler.showInfiniteProgressDialoge(underlying_activity,"Logging out from facebook", "Wait a moment");
		    AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(facebook);
		    asyncRunner.logout(underlying_activity.getBaseContext(), new LogoutRequestListener());		    
		} 
		else
			ToastTracker.showToast("Fb session already expired");
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
        	ThisUserConfig.getInstance().putBool(ThisUserConfig.FBLOGGEDIN, true);
        	ToastTracker.showToast("Authentication successsful");  
        	
        	requestUserData();        	
        }    
	    

		public void onFacebookError(FacebookError error) {
	    	ToastTracker.showToast("Authentication with Facebook failed!");
	    	
	    }
	    public void onError(DialogError error) {
	    	ToastTracker.showToast("Authentication with Facebook failed!");
	    	
	    }
	    public void onCancel() {
	    	ToastTracker.showToast("Authentication with Facebook cancelled!");
	    	
	    }
	}

    private void sendAddFBAndChatInfoToServer() {
    	//this should only be called from fbpostloginlistener to ensure we have fbid
    	Log.i(TAG,"in sendAddFBAndChatInfoToServer");
    	 SBHttpRequest chatServiceAddUserRequest = new ChatServiceCreateUser();
     	SBHttpClient.getInstance().executeRequest(chatServiceAddUserRequest);
		SBHttpRequest sendFBInfoRequest = new SaveFBInfoRequest(ThisUserConfig.getInstance().getString(ThisUserConfig.USERID), ThisUserConfig.getInstance().getString(ThisUserConfig.FBUID), ThisUserConfig.getInstance().getString(ThisUserConfig.FBACCESSTOKEN));
		SBHttpClient.getInstance().executeRequest(sendFBInfoRequest);			
	}
		
	private void requestUserData() {
        ToastTracker.showToast("Fetching user name, profile pic...");
        Bundle params = new Bundle();
        params.putString("fields", "name, picture");
        mAsyncRunner.request("me", params, new FBUserRequestListener());
    }
	
	/*
	 * Callback for fetching current user's name, picture, uid.
	 */

	public class FBUserRequestListener extends FBBaseRequestListener {	     
	    public void onComplete(final String response, final Object state) {
	        JSONObject jsonObject;
	        try {
	            jsonObject = new JSONObject(response);	  
	            String picurl,name,id;
	            id = jsonObject.getString("id");
	            name  = jsonObject.getString("name");
	            picurl = "http://graph.facebook.com/" + id + "/picture?type=small";
	            ThisUserConfig.getInstance().putString(ThisUserConfig.FBPICURL, picurl);
	            ThisUserConfig.getInstance().putString(ThisUserConfig.FBNAME, name);
                if (StringUtils.isEmpty(ThisUserConfig.getInstance().getString(ThisUserConfig.USERNAME))) {
                    ThisUserConfig.getInstance().putString(ThisUserConfig.USERNAME, name);
                }
	            ThisUserConfig.getInstance().putString(ThisUserConfig.FBUID,id );	           
	            sendAddFBAndChatInfoToServer();
	            Log.i(TAG,"fbpicurl:"+jsonObject.getString("picture"));
	            ToastTracker.showToast("fbpicurl:"+jsonObject.getString("picture"));
	            //Bitmap bmp = FBUtility.getBitmap(ThisUserConfig.getInstance().getString(ThisUserConfig.FBPICURL));
	            //Store.getInstance().saveBitmapToFile(bmp,ThisUserConfig.FBPICFILENAME);
	        } catch (JSONException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	}
	
	class LogoutRequestListener implements RequestListener {
		  public void onComplete(String response, Object state) {
			  //remove all fb info of this user	
			  
			  ThisUserConfig.getInstance().putString(ThisUserConfig.FBACCESSTOKEN, "");
			  ThisUserConfig.getInstance().putLong(ThisUserConfig.FBACCESSEXPIRES,-1);
			  ThisUserConfig.getInstance().putBool(ThisUserConfig.FBLOGGEDIN,false);
			  ThisUserConfig.getInstance().putString(ThisUserConfig.FBPICURL, "");
			  ThisUserConfig.getInstance().putString(ThisUserConfig.FBNAME, "");
			  ThisUserConfig.getInstance().putString(ThisUserConfig.FBUID, "");
			  Store.getInstance().deleteFile(ThisUserConfig.FBPICFILENAME);
			  ProgressHandler.dismissDialoge();
			  ToastTracker.showToast("Successfully logged out");		
			 
		  }
		  
		  public void onIOException(IOException e, Object state) {}
		  
		  public void onFileNotFoundException(FileNotFoundException e,
		        Object state) {}
		  
		  public void onMalformedURLException(MalformedURLException e,
		        Object state) {}
		  
		  public void onFacebookError(FacebookError e, Object state) {}
		}
	
	
    
}
	


