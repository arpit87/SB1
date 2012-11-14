package my.b1701.SB.Users;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;

public class NearbyUser {
	
		 
	private String userName;	
	private String srclatitude;
	private String srclongitude;
	private String dstlatitude;
	private String dstlongitude;
	private GeoPoint geoPoint;
	private String userDestination;	
	private UserFBInfo userFBInfo;
	 
	public NearbyUser(JSONObject thisOtherUser)
	{
		try {
			userName=thisOtherUser.getString(UserAttributes.USERID);
			srclatitude=thisOtherUser.getString(UserAttributes.SRCLATITUDE);
			srclongitude=thisOtherUser.getString(UserAttributes.SRCLONGITUDE);
			userDestination=thisOtherUser.getString(UserAttributes.DESTINATION);
			dstlatitude=thisOtherUser.getString(UserAttributes.DSTLATITUDE);
			dstlongitude=thisOtherUser.getString(UserAttributes.DSTLONGITUDE);	
			//userFBInfo = new UserFBInfo(thisOtherUser.getJSONObject(UserAttributes.FBINFO));
			geoPoint = GetUserGeopoint();
		} catch (JSONException e) {
			// TODO Auto-generated catch bloOck
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
	
	public GeoPoint GetUserGeopoint()
	{
		return new GeoPoint((int)(Double.parseDouble(srclatitude)*1E6),(int)(Double.parseDouble(srclongitude)*1E6));
	}
	
}
