package my.b1701.SB.Server;

import java.util.List;

import my.b1701.SB.ActivityHandlers.MapActivityHandler;
import my.b1701.SB.HelperClasses.JSONHandler;
import my.b1701.SB.Users.NearbyUser;

import org.apache.http.HttpResponse;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;


public class GetUsersResponse extends ServerResponseBase{

	JSONObject jobj;	
	private static final String TAG = "GetUsersResponse";
	public GetUsersResponse(HttpResponse response) {
		super(response);
		
	}
	@Override
	public void process() {
		Log.i(TAG,"processing response..geting json");
		jobj = JSONHandler.getInstance().GetJSONObjectFromHttp(serverResponse);
		Log.i(TAG,"got json "+jobj.toString());
		List<NearbyUser> nearbyUsers = JSONHandler.getInstance().GetNearbyUsersInfoFromJSONObject(jobj);		
		Log.i(TAG,"updating nearby users");
		MapActivityHandler.getInstance().updateNearbyUsers(nearbyUsers);
		
	}
	
	
	

}
