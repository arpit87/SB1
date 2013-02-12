package my.b1701.SB.Users;

import org.json.JSONException;
import org.json.JSONObject;

public class UserFBInfo {

    JSONObject allInfo = null;
    private String fbinfoavailable = "0";
    private String firstName = "";
    private String lastName = "";
    private String gender = "";
    private String imageURL = "";
    private String worksAt = "";
    private String livesIn = "";
    private String studiedAt = "";
    private String hometown = "";
    private String fbid = "";
    private String fbusername = "";
    private String phone = "";
    private String email = "";

    public UserFBInfo() {
        // TODO Auto-generated constructor stub
    }

   
	public UserFBInfo(JSONObject jsonObject) {
        allInfo = jsonObject;
        try {
        	fbinfoavailable = allInfo.getString(UserAttributes.FBINFOAVAILABLE);
        } catch (JSONException e) {
        	return;
        }
        
        if(FBInfoAvailable())
        {
	        try {
	            firstName = allInfo.getString(UserAttributes.FB_FIRSTNAME);
	        } catch (JSONException e) {
	        }
	
	        try {
	            lastName = allInfo.getString(UserAttributes.FB_LASTNAME);
	        } catch (JSONException e) {
	        }
	
	        try {
	            gender = allInfo.getString(UserAttributes.GENDER);
	        } catch (JSONException e) {
	        }
	
	        try {
	            fbid = allInfo.getString(UserAttributes.FBID);
	        } catch (JSONException e) {
	        }
	
	        try {
	            imageURL = allInfo.getString(UserAttributes.IMAGEURL);
	        } catch (JSONException e) {
	        }
	
	        try {
	            worksAt = allInfo.getString(UserAttributes.WORKSAT);
	        } catch (JSONException e) {
	        }
	
	        try {
	            livesIn = allInfo.getString(UserAttributes.LIVESIN);
	        } catch (JSONException e) {
	        }
	
	        try {
	            studiedAt = allInfo.getString(UserAttributes.STUDYAT);
	        } catch (JSONException e) {
	        }
	
	        try {
	            hometown = allInfo.getString(UserAttributes.HOMETOWN);
	        } catch (JSONException e) {
	        }
	        
	        try {
	            fbusername = allInfo.getString(UserAttributes.FBUSERNAME);
	        } catch (JSONException e) {
	        }
	        
	        try {
	            phone = allInfo.getString(UserAttributes.PHONE);
	        } catch (JSONException e) {
	        }
	        try {
	            email = allInfo.getString(UserAttributes.EMAIL);
	        } catch (JSONException e) {
	        }
        }
    }
	
	public boolean FBInfoAvailable() {  
		if(fbinfoavailable.equals("1"))
			return true;
		else
			return false;
    }
	
	public boolean isPhoneAvailable() {  
		if(phone!="")
			return true;
		else
			return false;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {    	
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
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
    
    public String getFBUsername() {
        return fbusername;
    }
    
    @Override
   	public int hashCode() {
   		final int prime = 31;
   		int result = 1;
   		result = prime * result + ((fbid == null) ? 0 : fbid.hashCode());
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
   		UserFBInfo other = (UserFBInfo) obj;
   		if (fbid == null) {
   			if (other.fbid != null)
   				return false;
   		} else if (!fbid.equals(other.fbid))
   			return false;
   		return true;
   	}


}
