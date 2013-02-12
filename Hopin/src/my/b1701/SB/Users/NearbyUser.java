package my.b1701.SB.Users;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;

public class NearbyUser {
	
	/**
	 * this class has all info for any nearby user returned by server	 * 	
	 */
	private UserLocInfo mUserLocInfo;
	private UserFBInfo mUserFBInfo;
	private UserOtherInfo mUserOtherInfo; 
	
	 
	public NearbyUser(JSONObject thisOtherUserJObj)
	{			
			getUserLocInfo(thisOtherUserJObj);
			getUserFBInfo(thisOtherUserJObj);
			getUserOtherInfo(thisOtherUserJObj);
	}
	
	private void getUserOtherInfo(JSONObject thisOtherUserJObj) {
		

		JSONObject userOtherInfoJObj;
		try {
			userOtherInfoJObj = thisOtherUserJObj.getJSONObject(UserAttributes.OTHERINFO);
			mUserOtherInfo = new UserOtherInfo(userOtherInfoJObj);					
		} catch (JSONException e) {
			mUserOtherInfo = new UserOtherInfo();
			e.printStackTrace();			
		}		
		
	}

	public UserFBInfo getUserFBInfo() {
		return mUserFBInfo;
	}
	
	public UserLocInfo getUserLocInfo() {
		return mUserLocInfo;
	}
	
	public UserOtherInfo getUserOtherInfo() {
		return mUserOtherInfo;
	}
	
	private void getUserLocInfo(JSONObject thisOtherUserJObj) {
		
		JSONObject userLocInfoJObj;
		try {
			userLocInfoJObj = thisOtherUserJObj.getJSONObject(UserAttributes.LOCINFO);
			mUserLocInfo = new UserLocInfo(userLocInfoJObj);			
		} catch (JSONException e) {
			mUserLocInfo = new UserLocInfo();
			e.printStackTrace();
		}		
		
	}
		
	private void getUserFBInfo(JSONObject thisOtherUserJObj) {
				
		JSONObject userFBInfoJObj;
		try {
			userFBInfoJObj = thisOtherUserJObj.getJSONObject(UserAttributes.FBINFO);
			mUserFBInfo = new UserFBInfo(userFBInfoJObj);			
		} catch (JSONException e) {
			mUserFBInfo = new UserFBInfo();
			e.printStackTrace();
		}
		
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mUserFBInfo == null) ? 0 : mUserFBInfo.hashCode());
		result = prime * result
				+ ((mUserLocInfo == null) ? 0 : mUserLocInfo.hashCode());
		result = prime * result
				+ ((mUserOtherInfo == null) ? 0 : mUserOtherInfo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NearbyUser other = (NearbyUser) obj;
		if (mUserFBInfo == null) {
			if (other.mUserFBInfo != null)
				return false;
		} else if (!mUserFBInfo.equals(other.mUserFBInfo))
			return false;
		if (mUserLocInfo == null) {
			if (other.mUserLocInfo != null)
				return false;
		} else if (!mUserLocInfo.equals(other.mUserLocInfo))
			return false;
		if (mUserOtherInfo == null) {
			if (other.mUserOtherInfo != null)
				return false;
		} else if (!mUserOtherInfo.equals(other.mUserOtherInfo))
			return false;
		return true;
	}
	
	
	
}
