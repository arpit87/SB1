package my.b1701.SB.Users;

import my.b1701.SB.HelperClasses.JSONHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserFBInfo {

	JSONObject allInfo = null;
	private String imageURL;
	private String worksAt;
	private String livesIn;
	private String studiedAt;
	private String hometown;
	private String name;
	
	
	public UserFBInfo(JSONObject jsonObject) {
		allInfo = jsonObject;
		try {
			imageURL = allInfo.getString(UserAttributes.NAME);
			imageURL = allInfo.getString(UserAttributes.IMAGEURL);
			worksAt = allInfo.getString(UserAttributes.WORKSAT);
			livesIn = allInfo.getString(UserAttributes.LIVESIN);
			studiedAt = allInfo.getString(UserAttributes.STUDIEDAT);
			hometown = allInfo.getString(UserAttributes.HOMETOWN);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public String getName() {
		return name;
	}


	public String getImageURL() {
		return imageURL;
	}


	public String getWorksAt() {
		return worksAt;
	}


	public String getLivesIn() {
		return livesIn;
	}


	public String getStudiedAt() {
		return studiedAt;
	}


	public String getHometown() {
		return hometown;
	}
	
			

}
