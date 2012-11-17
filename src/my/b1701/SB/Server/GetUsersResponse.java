package my.b1701.SB.Server;

import java.util.List;

import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.HelperClasses.JSONHandler;
import my.b1701.SB.Users.NearbyUser;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class GetUsersResponse extends ServerResponseBase{

	JSONObject jobj,data;	
	private static final String TAG = "GetUsersResponse";
	public GetUsersResponse(HttpResponse response) {
		super(response);
		
	}
	@Override
	public void process() {
		Log.i(TAG,"processing GetUsersResponse response..geting json");
		jobj = JSONHandler.getInstance().GetJSONObjectFromHttp(serverResponse);
		
		try {
			data = jobj.getJSONObject("body");
		} catch (JSONException e) {
			Log.e(TAG, "Error returned by server");
			e.printStackTrace();
		}
		Log.i(TAG,"got json "+jobj.toString());
		List<NearbyUser> nearbyUsers = JSONHandler.getInstance().GetNearbyUsersInfoFromJSONObject(data);		
		Log.i(TAG,"updating nearby users");		
		MapListActivityHandler.getInstance().updateNearbyUsers(nearbyUsers);
		
		
	}
	
	
	

}
