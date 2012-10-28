package my.b1701.SB.Server;

import org.apache.http.HttpResponse;
import org.json.JSONObject;

import android.util.Log;

public class AddUserResponse extends ServerResponseBase{

	JSONObject jobj;
	private static final String TAG = "GetUsersResponse";
	public AddUserResponse(HttpResponse response) {
		super(response);
	}
	@Override
	public void process() {
		Log.i(TAG,"processing AddUsersResponse response.status:"+this.getStatus());	
	}
	
}