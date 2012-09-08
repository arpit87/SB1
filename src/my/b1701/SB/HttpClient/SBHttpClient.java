package my.b1701.SB.HttpClient;

import my.b1701.SB.Server.ServerResponseBase;

import org.apache.http.HttpResponse;

//Singleton class
//this class will have a thread pool to its disposal
//??all threads will be async???
//??if we want a synced job then we can customize the request ???
public class SBHttpClient {
	
	private static SBHttpClient uniqueClient = new SBHttpClient();
	private SBHttpClient(){};	
	
	public static SBHttpClient getInstance()
	{		
		return uniqueClient; 		
	}
	
	public ServerResponseBase executeRequest(SBHttpRequest request)
	{
		//add code to handle failure of request		 
		return request.execute();
	}	

}
