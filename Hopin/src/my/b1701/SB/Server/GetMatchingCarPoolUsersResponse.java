package my.b1701.SB.Server;

import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Users.CurrentNearbyUsers;

import org.apache.http.HttpResponse;
import org.json.JSONException;

import android.content.Intent;
import android.util.Log;

public class GetMatchingCarPoolUsersResponse extends ServerResponseBase{


	private static final String TAG = "my.b1701.SB.Server.GetUsersResponse";
	
	
	public GetMatchingCarPoolUsersResponse(HttpResponse response,String jobjStr) {
		super(response,jobjStr);
				
	}
	
	@Override
	public void process() {
		Log.i(TAG,"processing GetMatchingCarPoolUsersResponse response..geting json");
		//jobj = JSONHandler.getInstance().GetJSONObjectFromHttp(serverResponse);
		Log.i(TAG,"got json "+jobj.toString());
		try {
			body = jobj.getJSONObject("body");
		} catch (JSONException e) {
			Log.e(TAG, "Error returned by server in fetching nearby carpool user.JSON:"+jobj.toString());
			e.printStackTrace();
			return;
		}
		
		CurrentNearbyUsers.getInstance().updateNearbyUsersFromJSON(body);		
		//MapListActivityHandler.getInstance().updateNearbyUsers();	
		
		Intent notifyUpdateintent = new Intent();
		notifyUpdateintent.setAction(ServerConstants.NEARBY_USER_UPDATED);		
		
		//this broadcast is for chat window which queries for nearby users in case of incoming chat 
		//from user which has not yet been fetched by getmatch request
		Platform.getInstance().getContext().sendBroadcast(notifyUpdateintent);
		
	}
	
	
	

}
