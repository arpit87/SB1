package my.b1701.SB.HttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import my.b1701.SB.HelperClasses.ThisAppConfig;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.HttpClient.SBHttpRequest.QueryMethod;
import my.b1701.SB.Server.GetUsersResponse;
import my.b1701.SB.Server.SendFBTokenToServerResponse;
import my.b1701.SB.Server.ServerQueries;
import my.b1701.SB.Server.ServerResponseBase;
import my.b1701.SB.Users.ThisUser;
import my.b1701.SB.Users.UserAttributes;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SendFBTokenToServer extends SBHttpRequest{
	
	HttpPost httpQuery;
	JSONObject jsonobj;
	
	public SendFBTokenToServer()
	{
		super();
		queryMethod = QueryMethod.Post;
		url = ServerQueries.SENDFBTOKEN_QUERY;
		jsonobj=new JSONObject();
		httpQuery =  new HttpPost(url);
		try {
			jsonobj.put(UserAttributes.USERID, ThisUser.getInstance().getUniqueID());
			jsonobj.put(ThisUserConfig.FBACCESSTOKEN, ThisUserConfig.getInstance().getString(ThisUserConfig.FBACCESSTOKEN));
			jsonobj.put(ThisUserConfig.FBACCESSEXPIRES, ThisUserConfig.getInstance().getLong(ThisUserConfig.FBACCESSEXPIRES));		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	};
	
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
		 
		return new SendFBTokenToServerResponse(response);
		
	}
	
	

}
