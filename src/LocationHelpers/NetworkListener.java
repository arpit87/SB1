package LocationHelpers;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class NetworkListener implements LocationListener{
	
	long minTime=0L;
	float minDistnce=0F;
	private static final String TAG = "NetworkListener";
	
	public void start()
	{
		Log.i(TAG,"strted listening to network");
		SBLocationManager.getInstance().locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistnce, this);
	}
	
	public void start(long minTime,float minDistance)
	{
		this.minTime=minTime;
		this.minDistnce=minDistance;
		SBLocationManager.getInstance().locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistnce, this);
	}
	
	public void stop()
	{
		Log.i(TAG,"stopped listening to network");
		SBLocationManager.getInstance().locManager.removeUpdates(this);
	}	

	public void onLocationChanged(Location location) {
		Log.i(TAG,"network location changed");
		location = SBLocationManager.getInstance().getCurrentBestLocation(location);
		LocationUpdater.getInstance().UpdateCurrentLocation(new SBLocation(location));
		
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
