package my.b1701.SB.Server;

import my.b1701.SB.Platform.Platform;

import org.apache.http.HttpResponse;

import android.util.Log;
import android.widget.Toast;

public class PostUserReqDataResponse extends ServerResponseBase {

	private static final String TAG = "PostUserReqDataResponse";
	
	public PostUserReqDataResponse(HttpResponse response,String jobjStr) {
		super(response,jobjStr);
		
	}

	@Override
	public void process() {
		Log.i(TAG,"processing PostUserReqDataResponse");
		//chk wt response coming of post..2xx is success
		if(this.getStatus() == ResponseStatus.HttpStatus201 || this.getStatus() == ResponseStatus.HttpStatus200)
		{
			//this will fail if called processing not done on UI thread..so change this when apply threading
			Toast.makeText(Platform.getInstance().getContext(), "user data posted successfully", Toast.LENGTH_SHORT).show();
		}
		else
			Toast.makeText(Platform.getInstance().getContext(), "user data not posted", Toast.LENGTH_SHORT).show();

	}	
	

}
