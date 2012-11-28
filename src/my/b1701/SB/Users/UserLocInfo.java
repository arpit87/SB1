package my.b1701.SB.Users;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;

public class UserLocInfo {
	
	private String userName = "";	
	private String srclatitude = "";
	private String srclongitude = "";
	private String dstlatitude = "";
	private String dstlongitude = "";
	private GeoPoint geoPoint = null;
	private String userDestination  = "";	
		
	
	public UserLocInfo(JSONObject thisUserJobj)
	{
				
		try {									
			userName=thisUserJobj.getString(UserAttributes.USERID);			
		} catch (JSONException e) {}
		
		try {									
			srclatitude=thisUserJobj.getString(UserAttributes.SRCLATITUDE);			
		} catch (JSONException e) {}
		
		try {									
			srclongitude=thisUserJobj.getString(UserAttributes.SRCLONGITUDE);			
		} catch (JSONException e) {}
		
		try {									
			userDestination=thisUserJobj.getString(UserAttributes.DESTINATION);			
		} catch (JSONException e) {}
		
		try {									
			dstlatitude=thisUserJobj.getString(UserAttributes.DSTLATITUDE);			
		} catch (JSONException e) {}
		
		try {									
			dstlongitude=thisUserJobj.getString(UserAttributes.DSTLONGITUDE);			
		} catch (JSONException e) {}
		
		getUserGeopoint();
		
	}


	public String getUsername()
	{
		return userName;
	}	


	public String getUserDestination()
	{		
		return userDestination;
	}
	
	public String getSrcLatitude()
	{
		return srclatitude;
	}
	
	public String getSrcLongitude()
	{
		return srclongitude;
	}
	
	public GeoPoint getGeoPoint() {
		return geoPoint;
	}


	private void getUserGeopoint()
	{
		if(srclatitude != "" && srclongitude != "")
			geoPoint =  new GeoPoint((int)(Double.parseDouble(srclatitude)*1E6),(int)(Double.parseDouble(srclongitude)*1E6));
	}
	
	
}
