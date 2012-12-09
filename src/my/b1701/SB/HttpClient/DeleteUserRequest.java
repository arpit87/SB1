package my.b1701.SB.HttpClient;

import java.io.IOException;

import my.b1701.SB.Server.DeleteUserResponse;
import my.b1701.SB.Server.ServerConstants;
import my.b1701.SB.Server.ServerResponseBase;
import my.b1701.SB.Users.ThisUser;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;

public class DeleteUserRequest extends SBHttpRequest{
			
	public DeleteUserRequest()
	{
		super();
		queryMethod = QueryMethod.Post;
		url = ServerConstants.SERVER_ADDRESS+ "\\"+ ThisUser.getInstance().getUniqueID();
	};
	
	public ServerResponseBase execute() {
	
		httpQuery =  new HttpDelete(url);
	
			try {
				response=httpclient.execute(httpQuery);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return new DeleteUserResponse(response);
	}

}
