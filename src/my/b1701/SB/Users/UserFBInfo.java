package my.b1701.SB.Users;

import my.b1701.SB.HelperClasses.JSONHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserFBInfo {

	JSONObject allInfo = null;
	private String imageURL = "";
	private String worksAt = "";
	private String livesIn = "";
	private String studiedAt = "" ;
	private String hometown = "";
	private String name = "";	
	private String fbid = "";
	
	public UserFBInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public UserFBInfo(JSONObject jsonObject) {
		allInfo = jsonObject;
				
		try {
			name = allInfo.getString(UserAttributes.NAME);			
		} catch (JSONException e) {	}
		
		try {
			fbid = allInfo.getString(UserAttributes.FBID);			
		} catch (JSONException e) {	}
		
		try {
			imageURL = allInfo.getString(UserAttributes.IMAGEURL);		
		} catch (JSONException e) {	}
		
		try {
			worksAt = allInfo.getString(UserAttributes.WORKSAT);			
		} catch (JSONException e) {	}
		
		try {
			livesIn = allInfo.getString(UserAttributes.LIVESIN);		
		} catch (JSONException e) {	}
		
		try {
			studiedAt = allInfo.getString(UserAttributes.STUDIEDAT);					
		} catch (JSONException e) {	}
		
		try {
			hometown = allInfo.getString(UserAttributes.HOMETOWN);			
		} catch (JSONException e) {	}
	}


	


	public String getName() {
		return name;
	}


	public String getFbid() {
		return fbid;
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
