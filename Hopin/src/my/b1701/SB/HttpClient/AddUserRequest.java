package my.b1701.SB.HttpClient;

import android.util.Log;
import my.b1701.SB.HelperClasses.ThisAppConfig;
import my.b1701.SB.Server.AddUserResponse;
import my.b1701.SB.Server.ServerConstants;
import my.b1701.SB.Server.ServerResponseBase;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class AddUserRequest extends SBHttpRequest{
	public static final String URL = ServerConstants.SERVER_ADDRESS+ServerConstants.USERSERVICE+"/addUser/";

	HttpPost httpQuery;
	JSONObject jsonobj;	
	String uuid;
	HttpClient httpclient = new DefaultHttpClient();
	AddUserResponse addUserResponse;
	String jsonStr;
	
	public AddUserRequest(String uuid)
	{
		super();
		this.uuid=uuid;		
		queryMethod = QueryMethod.Get;	

		jsonobj=new JSONObject();
		httpQuery =  new HttpPost(URL);
		
		try {
			jsonobj.put(ThisAppConfig.APPUUID, uuid);		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/*Uri.Builder builder = new Builder();
		builder.scheme("http");
		builder.authority(ServerConstants.SERVER_ADDRESS);
		builder.path(ServerConstants.USERSERVICE+"/addUser/");
		builder.appendQueryParameter(ThisAppConfig.APPUUID, uuid);
		url = builder.build().toString();
		httpQuery =  new HttpGet(url);*/
		
		StringEntity postEntityUser = null;
		try {
			postEntityUser = new StringEntity(jsonobj.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		postEntityUser.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		Log.d("debug", "calling server:"+jsonobj.toString());	
		httpQuery.setEntity(postEntityUser);
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
					
			addUserResponse =	new AddUserResponse(response,jsonStr);
			return addUserResponse;
		
	}
	
	

}

