package my.b1701.SB.HttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import my.b1701.SB.HttpClient.SBHttpRequest.QueryMethod;
import my.b1701.SB.Server.GetUsersResponse;
import my.b1701.SB.Server.PostUserReqDataResponse;
import my.b1701.SB.Server.ServerConstants;
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

public class PostUserReqData extends SBHttpRequest{
	
	HttpPost httpQuery;
	JSONObject jsonobj;
	
	public PostUserReqData()
	{
		super();
		queryMethod = QueryMethod.Post;
		url = ServerConstants.POST_USERREQDATA_QUERY;
		jsonobj=new JSONObject();
		httpQuery =  new HttpPost(url);
		try {
			jsonobj.put(UserAttributes.USERID, ThisUser.getInstance().getUniqueID());
			jsonobj.put(UserAttributes.SRCLATITUDE, ThisUser.getInstance().getCurrentGeoPoint().getLatitudeE6());
			jsonobj.put(UserAttributes.SRCLONGITUDE, ThisUser.getInstance().getCurrentGeoPoint().getLongitudeE6());
			jsonobj.put(UserAttributes.DSTLATITUDE, ThisUser.getInstance().getCurrentGeoPoint().getLatitudeE6());
			jsonobj.put(UserAttributes.DSTLONGITUDE, ThisUser.getInstance().getCurrentGeoPoint().getLongitudeE6());			
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
	 
	return new PostUserReqDataResponse(response);
	
}

}
