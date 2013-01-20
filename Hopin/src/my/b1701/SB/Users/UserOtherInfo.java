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
		
	}
	
	public boolean isOfferingRide()
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
	
}


