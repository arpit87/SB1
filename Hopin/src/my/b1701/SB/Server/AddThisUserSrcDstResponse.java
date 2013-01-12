package my.b1701.SB.Server;

import my.b1701.SB.HelperClasses.ToastTracker;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class AddThisUserSrcDstResponse extends ServerResponseBase{


	String user_id;
		
	private static final String TAG = "my.b1701.SB.Server.AddUserResponse";
	public AddThisUserSrcDstResponse(HttpResponse response,String jobjStr) {
		super(response,jobjStr);
		
				
	}
	
	@Override
	public void process() {
		//this process is not called if u make syncd consecutive requests,that time only last process called
		Log.i(TAG,"processing AddUsersResponse response.status:"+this.getStatus());	
		
		//jobj = JSONHandler.getInstance().GetJSONObjectFromHttp(serverResponse);
		
		try {
			body = jobj.getJSONObject("body");
			ToastTracker.showToast("added this user src,dst");
		} catch (JSONException e) {
			Log.e(TAG, "Error returned by server on user add scr dst");
			ToastTracker.showToast("Unable to add this user src,dst");
			e.printStackTrace();
		}
		
	}
	
}