package my.b1701.SB.HttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import my.b1701.SB.Server.AddThisUserSrcDstCarPoolResponse;
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

public class AddThisUserScrDstCarPoolRequest extends SBHttpRequest{
	
	private final String TAG = "my.b1701.SB.HttpClient.AddThisUserSrcDstRequest";
	HttpPost httpQueryAddRequest;	
	JSONObject jsonobjAddRequest;
	HttpClient httpclient = new DefaultHttpClient();
	AddThisUserSrcDstCarPoolResponse addThisUserResponse;
	String jsonStr;
	
	public AddThisUserScrDstCarPoolRequest()
	{
		//we will post 2 requests here
		//1)addrequest to add source and destination
		//2) getUsersRequest to get users
		super();
		//ProgressHandler.showInfiniteProgressDialoge(MapListActivityHandler.getInstance().getUnderlyingActivity(), "Fetching..", "Please wait");
		queryMethod = QueryMethod.Post;
		url1 = ServerConstants.SERVER_ADDRESS + ServerConstants.REQUESTSERVICE + "/addCarpoolRequest/";		
		jsonobjAddRequest=GetServerAuthenticatedJSON();
		httpQueryAddRequest =  new HttpPost(url1);		
		try {
			jsonobjAddRequest.put(UserAttributes.USERID, ThisUser.getInstance().getUserID());	
			jsonobjAddRequest.put(UserAttributes.SHAREOFFERTYPE, ThisUser.getInstance().get_Take_Offer_Type());
			jsonobjAddRequest.put(UserAttributes.SRCLATITUDE, ThisUser.getInstance().getSourceGeoPoint().getLatitude());
			jsonobjAddRequest.put(UserAttributes.SRCLONGITUDE, ThisUser.getInstance().getSourceGeoPoint().getLongitude());
            jsonobjAddRequest.put(UserAttributes.SRCLOCALITY, ThisUser.getInstance().getSourceGeoPoint().getSubLocality());
            jsonobjAddRequest.put(UserAttributes.SRCADDRESS, ThisUser.getInstance().getSourceGeoPoint().getAddress());
			jsonobjAddRequest.put(UserAttributes.DSTLATITUDE, ThisUser.getInstance().getDestinationGeoPoint().getLatitude());
			jsonobjAddRequest.put(UserAttributes.DSTLONGITUDE, ThisUser.getInstance().getDestinationGeoPoint().getLongitude());
            jsonobjAddRequest.put(UserAttributes.DSTLOCALITY,ThisUser.getInstance().getDestinationGeoPoint().getSubLocality());
            jsonobjAddRequest.put(UserAttributes.DSTADDRESS, ThisUser.getInstance().getDestinationGeoPoint().getAddress());
            jsonobjAddRequest.put(UserAttributes.TIME, ThisUser.getInstance().getTimeOfRequest());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringEntity postEntityAddRequest = null;
		try {
			postEntityAddRequest = new StringEntity(jsonobjAddRequest.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		postEntityAddRequest.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		Log.d(TAG, "calling server:" + jsonobjAddRequest.toString());	
		httpQueryAddRequest.setEntity(postEntityAddRequest);
		
				
	}
	
	public ServerResponseBase execute() {
			try {
				response=httpclient.execute(httpQueryAddRequest);
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
						
			addThisUserResponse = new AddThisUserSrcDstCarPoolResponse(response,jsonStr);			
			return addThisUserResponse;
		
	}
	
	

}
