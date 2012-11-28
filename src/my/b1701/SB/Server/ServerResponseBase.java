package my.b1701.SB.Server;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class ServerResponseBase {
	
	public enum ResponseStatus{
		HttpStatus200, // OK
		HttpStatus201, // CREATED
		HttpStatus202, // ACCEPTED
		HttpStatus302, // found
		HttpStatus401, // authentication error
		HttpStatus403, // access denied
		HttpStatus404, // not found
		HttpStatus422, // validation error
		
	}	
	
	JSONObject jobj;
	JSONObject body;
	JSONObject error;
	protected ResponseStatus status;			
	
	public ServerResponseBase(HttpResponse response,String jobjStr) {
		
		switch(response.getStatusLine().getStatusCode()){
		case 200:
			status = ResponseStatus.HttpStatus200;
		break;
		case 201:
			status = ResponseStatus.HttpStatus201;
		break;
		case 202:
			status = ResponseStatus.HttpStatus202;
		break;
		case 302:
			status = ResponseStatus.HttpStatus302;	
		break;
		case 401:
			status = ResponseStatus.HttpStatus401;
		break;
		case 403:
			status = ResponseStatus.HttpStatus403;
			break;
		case 404:
			status = ResponseStatus.HttpStatus404;
			break;
		case 422:
			status = ResponseStatus.HttpStatus422;
			break;
	}
		
		try {
			jobj= new JSONObject(jobjStr);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public ResponseStatus getStatus()
	{
		return status;
	}
	
	public abstract void process();
	
	

}
