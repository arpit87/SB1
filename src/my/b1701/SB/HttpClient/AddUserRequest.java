package my.b1701.SB.HttpClient;

import java.io.IOException;
import java.util.ArrayList;

import my.b1701.SB.HelperClasses.ThisAppConfig;
import my.b1701.SB.Server.AddUserResponse;
import my.b1701.SB.Server.ServerConstants;
import my.b1701.SB.Server.ServerResponseBase;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import android.app.Activity;
import android.net.Uri;
import android.net.Uri.Builder;

public class AddUserRequest extends SBHttpRequest{
	
	HttpGet httpQuery;	
	UrlEncodedFormEntity formEntity;
	String uuid;	
	ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
	
	
	public AddUserRequest(String uuid)
	{
		super();
		this.uuid=uuid;		
		queryMethod = QueryMethod.Get;	
		
		Uri.Builder builder = new Builder();
		builder.scheme("http");
		builder.authority(ServerConstants.SERVER_ADDRESS);
		builder.path(ServerConstants.USERSERVICE+"/addUser/");
		builder.appendQueryParameter(ThisAppConfig.APPUUID, uuid);
		url = builder.build().toString();		
		httpQuery =  new HttpGet(url);
		
		/*StringEntity postEntityUser = null;
		try {
			postEntityUser = new StringEntity(jsonobj.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		postEntityUser.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		Log.d("debug", "calling server:"+jsonobj.toString());	
		httpQuery.setEntity(postEntityUser);*/
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
		 
		return new AddUserResponse(response);
		
	}
	
	

}

