package my.b1701.SB.HttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import my.b1701.SB.Server.SaveFBInfoResponse;
import my.b1701.SB.Server.ServerConstants;
import my.b1701.SB.Server.ServerResponseBase;
import my.b1701.SB.Users.UserAttributes;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SaveFBInfoRequest extends SBHttpRequest{
	
	HttpPost httpQuery;	
	UrlEncodedFormEntity formEntity;
	HttpClient httpclient = new DefaultHttpClient();	
	SaveFBInfoResponse saveFBInfoResponse;
	JSONObject jsonobj;
	String jsonStr;
	
	public SaveFBInfoRequest(String user_id,String fbid,String fbToken)
	{		
		super();
		queryMethod = QueryMethod.Post;
				
		//prepare getnearby request		
		url1 = ServerConstants.SERVER_ADDRESS + ServerConstants.USERDETAILSSERVICE + "/saveFBInfo/";
		httpQuery = new HttpPost(url1);
		jsonobj = new JSONObject();
		try {
			jsonobj.put(UserAttributes.USERID, user_id);	
			jsonobj.put(UserAttributes.FBID, fbid);
			jsonobj.put(UserAttributes.FBTOKEN, fbToken);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringEntity postEntitygetNearbyUsers = null;
		try {
			postEntitygetNearbyUsers = new StringEntity(jsonobj.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		postEntitygetNearbyUsers.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		Log.d("debug", "calling server:"+jsonobj.toString());	
		httpQuery.setEntity(postEntitygetNearbyUsers);
	
	}
	
	
	public ServerResponseBase execute() {
			try {
				response=httpclient.execute(httpQuery);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				jsonStr = responseHandler.handleResponse(response);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 	
			
		saveFBInfoResponse = new SaveFBInfoResponse(response,jsonStr);
		return saveFBInfoResponse;
		
	}
	
	

}


