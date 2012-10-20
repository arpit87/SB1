package my.b1701.SB.HttpClient;

import my.b1701.SB.Server.ServerResponseBase;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

public abstract class SBHttpRequest {
	
	public enum QueryMethod {
		Get,
		Post,
		Put,
		Delete
	}
	
	QueryMethod queryMethod = null;
	String url = null;
	HttpEntity queryEntity = null;	
	HttpClient httpclient = new DefaultHttpClient();
	HttpRequestBase httpQuery = null;
	HttpResponse response = null;
	
	public ServerResponseBase execute() {
		return null;}
}
