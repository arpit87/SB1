package my.b1701.SB.LocationHelpers;

import java.util.TimerTask;

import my.b1701.SB.HelperClasses.Constants;
import my.b1701.SB.HelperClasses.ThisAppConfig;
import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.HttpClient.DeleteUserRequest;
import my.b1701.SB.HttpClient.SBHttpClient;
import my.b1701.SB.HttpClient.SBHttpRequest;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Server.ServerResponseBase;
import my.b1701.SB.Users.ThisUser;
import android.content.Intent;
import android.util.Log;

public class CheckAndDeleteUserOutOfReqArea extends TimerTask{

	private static final String TAG = "CheckUserOutOfReqArea";
	SBGeoPoint currGeoPoint = null;
	SBGeoPoint shareReqGeoPoint = null;
		
	
	@Override
	public void run() {
		currGeoPoint = ThisUser.getInstance().getCurrentGeoPoint();
		shareReqGeoPoint = ThisUser.getInstance().getShareReqGeoPoint();
		
		ToastTracker.showToast("chking if user out of req area");
		
		//For such short distances and since you are doing the coding yourself,
		//the flat earth equations would probably be a good enough 
		//use EarthRadius = 6371000 meters and your distance will be in meters
		//Lat/Lon will be in radians!
		if(currGeoPoint != null && shareReqGeoPoint != null)
		{
		double x = ((currGeoPoint.getLongitudeE6() - shareReqGeoPoint.getLongitudeE6())/1e6) * Math.cos((currGeoPoint.getLatitudeE6() + shareReqGeoPoint.getLatitudeE6()) / (2*1e6));
		double y = (currGeoPoint.getLatitudeE6() - shareReqGeoPoint.getLatitudeE6())/1e6;
		double distanceFromReqPoint = Math.sqrt(x * x + y * y) * Constants.EARTHRADIUS;
		
		
		double deleteReqDist = ThisAppConfig.getInstance().getLong(ThisAppConfig.USERCUTOFFDIST);
		if(distanceFromReqPoint > deleteReqDist)
		{
		   Log.i(TAG,"user out of req area sending delete req to server");
		   SBHttpRequest request = new DeleteUserRequest();
		   ServerResponseBase response = SBHttpClient.getInstance().executeRequest(request);
		   Log.i(TAG,"got delete user response ,processing");
		   response.process();	
		   Platform.getInstance().getContext().sendBroadcast(new Intent(Platform.getInstance().getContext(),LocationService.class));
		   
		}
		}
		else
		{
			Log.i(TAG,"******chk******how can curgeopoint or sharereqpoint be nullll!!! ");
		}
			
	}

}
