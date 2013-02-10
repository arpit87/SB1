package my.b1701.SB.Server;

import my.b1701.SB.HelperClasses.ProgressHandler;
import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.HttpClient.GetMatchingCarPoolUsersRequest;
import my.b1701.SB.HttpClient.SBHttpClient;
import my.b1701.SB.HttpClient.SBHttpRequest;

import org.apache.http.HttpResponse;
import org.json.JSONException;

import android.util.Log;

public class AddThisUserSrcDstCarPoolResponse extends ServerResponseBase{

    private static final String TAG = "my.b1701.SB.Server.AddUserResponse";
    

	String user_id;
		
	public AddThisUserSrcDstCarPoolResponse(HttpResponse response,String jobjStr) {
		super(response,jobjStr);

				
	}
	
	@Override
	public void process() {
		//this process is not called if u make syncd consecutive requests,that time only last process called
		Log.i(TAG,"processing AddUsersResponse response.status:"+this.getStatus());	
		
		//jobj = JSONHandler.getInstance().GetJSONObjectFromHttp(serverResponse);
		
		try {
			body = jobj.getJSONObject("body");
			ToastTracker.showToast("added this user src,dst for car pool,fetching match");
			
			SBHttpRequest getNearbyUsersRequest = new GetMatchingCarPoolUsersRequest();
	        SBHttpClient.getInstance().executeRequest(getNearbyUsersRequest);
			
		} catch (JSONException e) {
			Log.e(TAG, "Error returned by server on user add scr dst");
			ToastTracker.showToast("Network error,try again");
			ProgressHandler.dismissDialoge();
			e.printStackTrace();
		}
		
	}
	
}