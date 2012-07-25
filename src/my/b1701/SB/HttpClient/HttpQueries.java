package my.b1701.SB.HttpClient;

import java.io.IOException;

import my.b1701.SB.Server.ServerResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;


import android.util.Log;

public class HttpQueries {
	
	public enum QueryMethod {
		Get,
		Post,
		Put,
		Delete
	}

	public static ServerResponse QueryServer(final QueryMethod queryMethod,String query,HttpEntity queryEntity){
		HttpClient httpclient = new DefaultHttpClient();
		
		HttpRequestBase httpQuery = null;
		switch(queryMethod){
			case Get:
				httpQuery =  new HttpGet(query);
				break;
			case Post:
				HttpPost postQuery = new HttpPost(query);
				
				// set entity
				if(queryEntity != null){
					postQuery.setEntity(queryEntity);
				}
				
				httpQuery =  postQuery;
				break;
		}
		
		HttpResponse response = null;
		try {
			Log.d("debug", "query:"+httpQuery.toString());
			response = httpclient.execute(httpQuery);
		} catch (ClientProtocolException e) {
			Log.e("makeQuery, ClientProtocolException:", e.getMessage().toString());
			//return null;
		} catch (IOException e) {
			Log.e("makeQuery, IOException:", e.getMessage().toString());
			//return null;
		}
		
		ServerResponse serverResponse= new ServerResponse(response);	
				
		return serverResponse;
		
	}
	

}
