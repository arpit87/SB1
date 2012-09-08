package my.b1701.SB.Users;

import org.json.JSONArray;
import java.lang.Number;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.provider.Settings.Secure;
import android.util.Log;

import com.google.android.maps.GeoPoint;

public class NearbyUser {
	
		 
	private String userName;	
	private String latitude;
	private String longitude;
	private GeoPoint geoPoint;
	private String userDestination;
	private String atTime;
	 
	public NearbyUser(JSONObject thisOtherUser)
	{
		try {
			userName=thisOtherUser.getString(UserAttributes.USER_ID);
			latitude=thisOtherUser.getString(UserAttributes.LATITUDE);
			longitude=thisOtherUser.getString(UserAttributes.LONGITUDE);
			userDestination=thisOtherUser.getString(UserAttributes.DESTINATION);
			atTime=thisOtherUser.getString(UserAttributes.TIME);
			geoPoint = GetUserGeopoint();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
		
	public String getUsername()
	{
		return userName;
	}
	
	public String getUserDestination()
	{		
		return userDestination;
	}
	
	public String getAtTime()
	{		
		return atTime;
	}
	
	public GeoPoint GetUserGeopoint()
	{
		return new GeoPoint((int)(Double.parseDouble(latitude)*1E6),(int)(Double.parseDouble(longitude)*1E6));
	}
	
}
