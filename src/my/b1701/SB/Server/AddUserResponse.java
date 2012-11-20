package my.b1701.SB.Server;

import my.b1701.SB.Activities.MapListViewTabActivity;
import my.b1701.SB.HelperClasses.JSONHandler;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Users.ThisUser;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class AddUserResponse extends ServerResponseBase{

	JSONObject jobj;
	JSONObject body;
	String user_id;
	
	
	private static final String TAG = "AddUserResponse";
	public AddUserResponse(HttpResponse response) {
		super(response);
				
	}
	
	@Override
	public void process() {
		Log.i(TAG,"processing AddUsersResponse response.status:"+this.getStatus());	
		
		jobj = JSONHandler.getInstance().GetJSONObjectFromHttp(serverResponse);
		
		try {
			body = jobj.getJSONObject("body");
			user_id = body.getString(ThisUserConfig.USERID);
			ThisUserConfig.getInstance().putString(ThisUserConfig.USERID, user_id);
			ThisUser.getInstance().setUserID(user_id);	
			ToastTracker.showToast("got user_id:"+user_id);
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
			e.printStackTrace();
		}
		
	}
	
}