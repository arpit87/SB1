package my.b1701.SB.HttpClient;

import my.b1701.SB.Server.ServerResponse;
import my.b1701.SB.Server.ServerResponseBase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

//Singleton class
//this class will have a thread pool to its disposal
//??all threads will be async???
//??if we want a synced job then we can customize the request ???
public class SBHttpClient {
	
	private static final String TAG = "SBHttpClient";
	private static SBHttpClient uniqueClient = new SBHttpClient();
	private SBHttpClient(){};
	private ServerResponseBase response;
	
	public static SBHttpClient getInstance()
	{		
		return uniqueClient; 		
	}
	
	public void executeRequest(SBHttpRequest... request)
	{
		//currently allowing max 3 sync requests
		int count = request.length;
		if (count<=5)
			new NewAsyncTask().execute(request);
		else
			Log.e(TAG, "Max 3 http request per thread allowed");
	}	
	
	private class NewAsyncTask extends AsyncTask <SBHttpRequest, ProgressBar, ServerResponseBase>
	{		
		protected ServerResponseBase doInBackground(SBHttpRequest... request) {
			 int count = request.length;	         
	         for(int i=0;i<count;i++)
	         {
	        	 response =  request[i].execute();
	        	 if(response.getStatus()!=ServerResponseBase.ResponseStatus.HttpStatus200)
	        		 break;
	         }
	         return response;
		}
		
		protected void onPostExecute(ServerResponseBase response) {
			response.process();
		}

				
	}

}
