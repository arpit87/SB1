package my.b1701.SB.HttpClient;

import my.b1701.SB.HelperClasses.ThisAppConfig;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.Server.ServerResponseBase;
import my.b1701.SB.Users.ThisUser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class SBHttpRequest {
	
	public enum QueryMethod {
		Get,
		Post,
		Put,
		Delete
	}
	
	QueryMethod queryMethod = null;
	//we are allowing upto 3 consecutive syncd requests for now
	String url1 = null;
	String url2 = null;
	String url3 = null;
	HttpEntity queryEntity = null;	
	HttpResponse response = null;
	
	// Create a response handler
    ResponseHandler<String> responseHandler = new BasicResponseHandler();
    	
	public ServerResponseBase execute() {
		return null;}
	
	//do not add this to initial add user request
	public JSONObject GetServerAuthenticatedJSON()
	{
		JSONObject jObj = new JSONObject();
		try {
			jObj.put(ThisAppConfig.APPUUID, ThisAppConfig.getInstance().getString(ThisAppConfig.APPUUID));
			jObj.put(ThisUserConfig.USERID, ThisUser.getInstance().getUserID());
		} catch (JSONException e) {			
			e.printStackTrace();
		}		
		return jObj;
	}
}
