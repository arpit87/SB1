package my.b1701.SB.Server;

import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.Users.CurrentNearbyUsers;

import org.apache.http.HttpResponse;
import org.json.JSONException;

import android.content.Intent;
import android.util.Log;


public class GetMatchingNearbyUsersResponse extends ServerResponseBase{


	private static final String TAG = "my.b1701.SB.Server.GetUsersResponse";
	public static final String NEARBY_USER_UPDATED = "my.b1701.SB.Server.NEARBYUSER_UPDATED";
	
	public GetMatchingNearbyUsersResponse(HttpResponse response,String jobjStr) {
		super(response,jobjStr);
				
	}
	
	@Override
	public void process() {
		Log.i(TAG,"processing GetUsersResponse response..geting json");
		//jobj = JSONHandler.getInstance().GetJSONObjectFromHttp(serverResponse);
		Log.i(TAG,"got json "+jobj.toString());
		try {
			body = jobj.getJSONObject("body");
		} catch (JSONException e) {
			Log.e(TAG, "Error returned by server in fetching nearby user.JSON:"+jobj.toString());
			e.printStackTrace();
			return;
		}
		
		CurrentNearbyUsers.getInstance().updateNearbyUsersFromJSON(body);
		//List<NearbyUser> nearbyUsers = JSONHandler.getInstance().GetNearbyUsersInfoFromJSONObject(body);		
		Log.i(TAG,"updating nearby users");		
		Intent notifyUpdateintent = new Intent();
		notifyUpdateintent.setAction(NEARBY_USER_UPDATED);		
		
		MapListActivityHandler.getInstance().getUnderlyingActivity().sendBroadcast(notifyUpdateintent); 
		MapListActivityHandler.getInstance().updateNearbyUsers(CurrentNearbyUsers.getInstance().getAllNearbyUsers());		
		
	}
	
	
	

}
