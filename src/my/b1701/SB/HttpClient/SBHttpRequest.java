package my.b1701.SB.HttpClient;

import my.b1701.SB.Server.ServerResponseBase;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;

public abstract class SBHttpRequest {
	
	public enum QueryMethod {
		Get,
		Post,
		Put,
		Delete
	}
	
	QueryMethod queryMethod = null;
	//we are allowing upto 3 consecutive syncd requests for now
	String url1 = null;
	String url2 = null;
	String url3 = null;
	HttpEntity queryEntity = null;	
	HttpResponse response = null;
	
	// Create a response handler
    ResponseHandler<String> responseHandler = new BasicResponseHandler();
    	
	public ServerResponseBase execute() {
		return null;}
}
