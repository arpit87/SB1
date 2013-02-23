package my.b1701.SB.HttpClient;

import my.b1701.SB.Server.DeleteUserResponse;
import my.b1701.SB.Server.ServerConstants;
import my.b1701.SB.Server.ServerResponseBase;
import my.b1701.SB.Users.ThisUser;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class DeleteUserRequest extends SBHttpRequest{
			
	HttpClient httpclient = new DefaultHttpClient();
	HttpDelete httpQuery =  new HttpDelete(url);
	String jsonStr;
	public DeleteUserRequest()
	{
		super();
		queryMethod = QueryMethod.Post;
		url = ServerConstants.SERVER_ADDRESS+ "\\"+ ThisUser.getInstance().getUserID();
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
			
			try {
				jsonStr = responseHandler.handleResponse(response);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}   			
			
		return new DeleteUserResponse(response,jsonStr);
	}

}
