package my.b1701.SB.HttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.Server.ChatServiceCreateUserResponse;
import my.b1701.SB.Server.ServerConstants;
import my.b1701.SB.Server.ServerResponseBase;
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

public class ChatServiceCreateUser extends SBHttpRequest{
		
		private final String TAG = "my.b1701.SB.HttpClient.ChatServiceCreateUser";
		HttpPost httpQueryAddRequest;	
		JSONObject jsonobjAddRequest = new JSONObject();
		HttpClient httpclient = new DefaultHttpClient();
		ChatServiceCreateUserResponse chatServiceCreateUserResponse;
		String jsonStr;
		
		public ChatServiceCreateUser()
		{
			//we will post 2 requests here
			//1)addrequest to add source and destination
			//2) getUsersRequest to get users
			super();
			queryMethod = QueryMethod.Post;
			url1 = ServerConstants.SERVER_ADDRESS + ServerConstants.CHATSERVICE + "/createUser/";
			httpQueryAddRequest =  new HttpPost(url1);		
			try {
				jsonobjAddRequest.put(UserAttributes.CHATUSERID, ThisUserConfig.getInstance().getString(ThisUserConfig.USERID));
				jsonobjAddRequest.put(UserAttributes.CHATUSERNAME, ThisUserConfig.getInstance().getString(ThisUserConfig.FBUID));
					
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
							
				chatServiceCreateUserResponse = new ChatServiceCreateUserResponse(response,jsonStr);			
				return chatServiceCreateUserResponse;
			
		}
		
		

	}

