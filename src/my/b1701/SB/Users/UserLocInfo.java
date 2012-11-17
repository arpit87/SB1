package my.b1701.SB.Users;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;

public class UserLocInfo {
	
	private String userName;	
	private String srclatitude;
	private String srclongitude;
	private String dstlatitude;
	private String dstlongitude;
	private GeoPoint geoPoint;
	private String userDestination;	
	
	
	public UserLocInfo(JSONObject thisUserJobj)
	{
		JSONObject userLocInfo;
		try {
									
			userName=thisUserJobj.getString(UserAttributes.USERID);
			srclatitude=thisUserJobj.getString(UserAttributes.SRCLATITUDE);
			srclongitude=thisUserJobj.getString(UserAttributes.SRCLONGITUDE);
			userDestination=thisUserJobj.getString(UserAttributes.DESTINATION);
			dstlatitude=thisUserJobj.getString(UserAttributes.DSTLATITUDE);
			dstlongitude=thisUserJobj.getString(UserAttributes.DSTLONGITUDE);
			getUserGeopoint();
			
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
		geoPoint =  new GeoPoint((int)(Double.parseDouble(srclatitude)*1E6),(int)(Double.parseDouble(srclongitude)*1E6));
	}
	
	
}
