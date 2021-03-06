package my.b1701.SB.Server;

import my.b1701.SB.Activities.MapListViewTabActivity;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Users.ThisUser;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.util.Log;

public class AddUserResponse extends ServerResponseBase{

	
	String user_id;	
	
	private static final String TAG = "my.b1701.SB.Server.AddUserResponse";
	public AddUserResponse(HttpResponse response,String jobjStr) {
		super(response,jobjStr);
	}
	
	@Override
	public void process() {
		Log.i(TAG,"processing AddUsersResponse response.status:"+this.getStatus());
		
				
		try {
			body = jobj.getJSONObject("body");
			user_id = body.getString(ThisUserConfig.USERID);
			ThisUserConfig.getInstance().putString(ThisUserConfig.USERID, user_id);
			ThisUser.getInstance().setUserID(user_id);	
			ToastTracker.showToast("Got user_id:"+user_id);
			//now we will start map activity
			final Intent showSBMapViewActivity = new Intent(Platform.getInstance().getContext(), MapListViewTabActivity.class);	
			showSBMapViewActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 Platform.getInstance().getHandler().post(new Runnable() {
	          public void run() {        	  
	              Platform.getInstance().getContext().startActivity(showSBMapViewActivity);
	          }
	        }); 
		} catch (JSONException e) {
			Log.e(TAG, "Error returned by server on user add");
			ToastTracker.showToast("Unable to communicate to server,try again later");
			e.printStackTrace();
		}
		
	}
	
}