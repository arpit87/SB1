package my.b1701.SB.LocationHelpers;

import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.Users.ThisUser;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class NetworkListener implements LocationListener{
	
	long minTime=0L;
	float minDistnce=0F;
	private static final String TAG = "NetworkListener";
	
	private Location thisWindowBestLocation = null;	
	private Location lastWindowBestLocation = null;

	public static final int OUT_OF_SERVICE = 0;	
	public static final int TEMPORARILY_UNAVAILABLE = 1;	
	public static final int AVAILABLE = 2;
	
	public void start()
	{
		Log.i(TAG,"strted listening to network");
		ToastTracker.showToast("strted listning to network");
		//thisWindowBestLocation = null;
		SBLocationManager.getInstance().locManager.removeUpdates(this);
		SBLocationManager.getInstance().locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0F, this);
	}
	
	public void start(long minTime,float minDistance)
	{
		Log.i(TAG,"strted listening to network wid mintim"+minTime+",mindist:"+minDistance);
		this.minTime=minTime;
		this.minDistnce=minDistance;
		SBLocationManager.getInstance().locManager.removeUpdates(this);
		SBLocationManager.getInstance().locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistnce, this);
	}
	
	public void stop()
	{
		if(thisWindowBestLocation != null)
		{
			Log.i(TAG,"stopped listening to network,best loc thiswindowbestacc:"+thisWindowBestLocation.getAccuracy());
			lastWindowBestLocation = thisWindowBestLocation;
		}
		thisWindowBestLocation = null;
		SBLocationManager.getInstance().locManager.removeUpdates(this);
	}	

	public void onLocationChanged(Location location) {
		//we see that location bursts come in windows n then idle for given time
		//so we find most accurate location of a window and set it to thisWindowBest
		Log.i(TAG,"network location changed");
		Log.i(TAG,"new network location acc:"+ location.getAccuracy());
		ToastTracker.showToast("loc changed:acc:"+location.getAccuracy());
		if(thisWindowBestLocation == null)
		{		
			//window starting
			thisWindowBestLocation = location;
			return;
		}
		else if(location.getAccuracy() < thisWindowBestLocation.getAccuracy() )
		{
			//window continuing
			//Log.i(TAG,"thiswindowbest location:"+thisWindowBestLocation.toString());
			thisWindowBestLocation = location;		
			if(MapListActivityHandler.getInstance().isUpdateMapRealTime())
			{
				ThisUser.getInstance().setLocation(new SBLocation(location));
				MapListActivityHandler.getInstance().updateThisUserMapOverlay();				
			}
				
			Log.i(TAG,"thiswindowbest location:"+thisWindowBestLocation.toString());
		}		
		//location = SBLocationManager.getInstance().getCurrentBestLocation(location);
		//LocationUpdater.getInstance().UpdateCurrentLocation(new SBLocation(location));
		
		
	}
	
	public SBLocation getLastWindowBestLocation()
	{
		return new SBLocation(lastWindowBestLocation);
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		/*Log.i(TAG,"network status changed to:"+status);
		//Toast toast = Toast.makeText(Platform.getInstance().getContext(), "Network status change to:"+status, Toast.LENGTH_SHORT);       
		//toast.show();
		if(thisWindowBestLocation!=null)
		LocationUpdater.getInstance().UpdateToBestCurrentLocation(new SBLocation(thisWindowBestLocation));
		if(status == AVAILABLE) //window starting
			thisWindowBestLocation = null;
		//if(status == TEMPORARILY_UNAVAILABLE) //window ending*/			
		
	}

}
