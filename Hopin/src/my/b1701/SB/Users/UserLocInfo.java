package my.b1701.SB.Users;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;


/**
 * 
 * @author arpit87
 * this class parses the nearbyuser array that we get
 */
public class UserLocInfo {
	
	private String userID = "";	
	private String firstName = "";	
	private String lastName = "";	
	private String srclatitude = "";
	private String srclongitude = "";
	private String dstlatitude = "";
	private String dstlongitude = "";
	private GeoPoint geoPoint = null;
	private String srcaddress = "";
	private String srclocality = "";
	private String dstaddress = "";
	private String dstlocality = "";
	
	
	
	private JSONObject srcLocjObj = null;
	private JSONObject dstLocjObj = null;
		
	
	public UserLocInfo(JSONObject thisUserJobj)
	{
		try {
			srcLocjObj = thisUserJobj.getJSONObject(UserAttributes.SRCINFO);
		} catch (JSONException e) {}
		
		try {
			dstLocjObj = thisUserJobj.getJSONObject(UserAttributes.DSTINFO);
		} catch (JSONException e) {}
				
		try {									
			userID=thisUserJobj.getString(UserAttributes.USERID);			
		} catch (JSONException e) {}
		
		try {									
			firstName=thisUserJobj.getString(UserAttributes.FIRSTNAME);			
		} catch (JSONException e) {}
		
		try {									
			lastName=thisUserJobj.getString(UserAttributes.LASTNAME);			
		} catch (JSONException e) {}
		
		try {									
			srclatitude=srcLocjObj.getString(UserAttributes.SRCLATITUDE);			
		} catch (JSONException e) {}
		
		try {									
			srclongitude=srcLocjObj.getString(UserAttributes.SRCLONGITUDE);			
		} catch (JSONException e) {}
		
		try {									
			srcaddress = srcLocjObj.getString(UserAttributes.SRCADDRESS);			
		} catch (JSONException e) {}
		
		try {									
			srclocality = srcLocjObj.getString(UserAttributes.SRCLOCALITY);			
		} catch (JSONException e) {}
			
		
		try {									
			dstlatitude=dstLocjObj.getString(UserAttributes.DSTLATITUDE);			
		} catch (JSONException e) {}
		
		try {									
			dstlongitude=dstLocjObj.getString(UserAttributes.DSTLONGITUDE);			
		} catch (JSONException e) {}
		
		try {									
			dstaddress = dstLocjObj.getString(UserAttributes.DSTADDRESS);
		} catch (JSONException e) {}
		
		try {									
			dstlocality = dstLocjObj.getString(UserAttributes.DSTLOCALITY);
		} catch (JSONException e) {}
		
		getUserGeopoint();
		
	}


	public String getUserID()
	{
		return userID;
	}	


	public String getUserSrcLocality()
	{		
		return srclocality;
	}
	
	public String getUserSrcAddress()
	{		
		return srcaddress;
	}
	
	public String getUserDstLocality()
	{		
		return dstlocality;
	}
	
	public String getUserDstAddress()
	{		
		return dstaddress;
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
