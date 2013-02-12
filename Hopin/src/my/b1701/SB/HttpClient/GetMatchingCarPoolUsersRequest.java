package my.b1701.SB.HttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import my.b1701.SB.Server.GetMatchingCarPoolUsersResponse;
import my.b1701.SB.Server.ServerConstants;
import my.b1701.SB.Server.ServerResponseBase;
import my.b1701.SB.Users.ThisUser;
import my.b1701.SB.Users.UserAttributes;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class GetMatchingCarPoolUsersRequest  extends SBHttpRequest{
	
	
	HttpPost httpQueryGetNearbyUsers;	
	JSONObject jsonobjGetNearbyUsers;
	HttpClient httpclient = new DefaultHttpClient();
	GetMatchingCarPoolUsersResponse getNearbyUsersResponse;
	String jsonStr;
	public GetMatchingCarPoolUsersRequest()
	{		
		super();
		queryMethod = QueryMethod.Post;
				
		//prepare getnearby request		
		url1 = ServerConstants.SERVER_ADDRESS + ServerConstants.REQUESTSERVICE + "/getCarpoolMatches/";
		httpQueryGetNearbyUsers = new HttpPost(url1);
		jsonobjGetNearbyUsers = GetServerAuthenticatedJSON();;
		try {
			jsonobjGetNearbyUsers.put(UserAttributes.USERID, ThisUser.getInstance().getUserID());	
			jsonobjGetNearbyUsers.put(UserAttributes.SRCLATITUDE, ThisUser.getInstance().getCurrentGeoPoint().getLatitude());
			jsonobjGetNearbyUsers.put(UserAttributes.SRCLONGITUDE, ThisUser.getInstance().getCurrentGeoPoint().getLongitude());
			jsonobjGetNearbyUsers.put(UserAttributes.DSTLATITUDE, ThisUser.getInstance().getDestinationGeoPoint().getLatitude());
			jsonobjGetNearbyUsers.put(UserAttributes.DSTLONGITUDE, ThisUser.getInstance().getDestinationGeoPoint().getLongitude());
			jsonobjGetNearbyUsers.put(UserAttributes.SRCADDRESS, ThisUser.getInstance().getSourceGeoAddress());
			jsonobjGetNearbyUsers.put(UserAttributes.DSTADDRESS, ThisUser.getInstance().getDestinationGeoAddress());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringEntity postEntitygetNearbyUsers = null;
		try {
			postEntitygetNearbyUsers = new StringEntity(jsonobjGetNearbyUsers.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		postEntitygetNearbyUsers.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		Log.d("debug", "calling server:"+jsonobjGetNearbyUsers.toString());	
		httpQueryGetNearbyUsers.setEntity(postEntitygetNearbyUsers);
		
	}
	
	public ServerResponseBase execute() {
			try {
				response=httpclient.execute(httpQueryGetNearbyUsers);
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
			
			getNearbyUsersResponse =	new GetMatchingCarPoolUsersResponse(response,jsonStr);
		return getNearbyUsersResponse;
		
	}
	
	

}
