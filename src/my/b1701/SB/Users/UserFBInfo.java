package my.b1701.SB.Users;

import my.b1701.SB.HelperClasses.JSONHandler;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserFBInfo {

	JSONObject allInfo = null;
	
	public UserFBInfo(JSONObject jsonObject) {
		allInfo = jsonObject;
	}
	
	public String getImageUrl()
	{
		return JSONHandler.getInstance().getFBPicURLFromJSON(allInfo);		 
	}
	
		

}
