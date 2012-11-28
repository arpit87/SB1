package my.b1701.SB.Users;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;

public class NearbyUser {
	
		 
	private UserLocInfo userLocInfo;
	private UserFBInfo userFBInfo;	
	
	 
	public NearbyUser(JSONObject thisOtherUserJObj)
	{			
			getUserLocInfo(thisOtherUserJObj);
			getUserFBInfo(thisOtherUserJObj);
	}
	
	public UserFBInfo getUserFBInfo() {
		return userFBInfo;
	}
	
	public UserLocInfo getUserLocInfo() {
		return userLocInfo;
	}
	
	private void getUserLocInfo(JSONObject thisOtherUserJObj) {
		
		JSONObject userLocInfoJObj;
		try {
			userLocInfoJObj = thisOtherUserJObj.getJSONObject(UserAttributes.LOCINFO);
			userLocInfo = new UserLocInfo(userLocInfoJObj);			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
		
	private void getUserFBInfo(JSONObject thisOtherUserJObj) {
				
		JSONObject userFBInfoJObj;
		try {
			userFBInfoJObj = thisOtherUserJObj.getJSONObject(UserAttributes.FBINFO);
			userFBInfo = new UserFBInfo(userFBInfoJObj);			
		} catch (JSONException e) {
			userFBInfo = new UserFBInfo();
			e.printStackTrace();
		}
		
		
	}


	
}
