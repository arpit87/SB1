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

public class OtherUser {
	
	
	private static Me instance;
    
	public static synchronized Me getInstance() {
		if (instance == null) {
			instance = new Me();
	    } 
	    return instance;
	}
	 
	private String userName;	
	private String latitude;
	private String longitude;
	private GeoPoint geoPoint;
	private String userDestination;
	private String atTime;
	 
	public OtherUser()
	{}
	
	public OtherUser(String name,String lati,String longi,String destination,String time)
	{
		userName=name;
		latitude=lati;
		longitude=longi;
		this.userDestination=destination;
		this.atTime=time;
		geoPoint = GetUserGeopoint();
		
	}

	public static List<OtherUser> GetUsersInfoFromJSONObject(JSONObject jObj)
	{
		ArrayList<OtherUser> nearbyUsers = new ArrayList<OtherUser>();
		try {
			JSONArray users = jObj.getJSONArray("NearbyUsers");
						
			for(int i=0;i<users.length();i++)
			{
				JSONObject thisUser=users.getJSONObject(i);
				Log.d("json",thisUser.toString());
				OtherUser u = new OtherUser();
				nearbyUsers.add((u));
				u.userName=thisUser.getString(UserAttributes.USER_ID);
				u.latitude=thisUser.getString(UserAttributes.LATITUDE);
				u.longitude=thisUser.getString(UserAttributes.LONGITUDE);
				u.userDestination=thisUser.getString(UserAttributes.DESTINATION);
				u.atTime=thisUser.getString(UserAttributes.TIME);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nearbyUsers;
		
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
