package my.b1701.SB.Users;

import android.annotation.SuppressLint;
import my.b1701.SB.HelperClasses.JSONHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class UserOtherInfo {

	JSONObject allInfo = null;
	private String type = "0";
	private String percent_match = "0";
	private String time ="";
	private String mobile_available="0";
	
		
	public UserOtherInfo(JSONObject jsonObject) {
		allInfo = jsonObject;
				
		try {
			type = allInfo.getString(UserAttributes.SHAREOFFERTYPE);			
		} catch (JSONException e) {	}
		
		try {
			percent_match = allInfo.getString(UserAttributes.PERCENTMATCH);			
		} catch (JSONException e) {	}
		
		try {
			time = allInfo.getString(UserAttributes.TIME);			
		} catch (JSONException e) {	}
		
		try {
			mobile_available = allInfo.getString(UserAttributes.MOBILENUMBER);			
		} catch (JSONException e) {	}
		
	}
	
	public boolean isOfferingRide()
	{
		if(mobile_available.equals("0"))
			return false;
		else
			return true;
	}
	
	public boolean isMobileNumberAvailable()
	{
		if(type.equals("0"))
			return false;
		else
			return true;
	}
	
	public int getPercentMatch()
	{		
		return Integer.parseInt(percent_match);
	}
	
	public String getTime()
	{
		return time;
	}
	
	public String getRideType()
	{
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		UserOtherInfo other = (UserOtherInfo) obj;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
		
}


