package LocationHelpers;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GPSListener implements LocationListener{

	long minTime=0L;
	float minDistnce=0F;
	
	public void start()
	{		
		SBLocationManager.getInstance().locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistnce, this);
	}
	
	public void start(long minTime,float minDistance)
	{
		this.minTime=minTime;
		this.minDistnce=minDistance;
		SBLocationManager.getInstance().locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistnce, this);
	}
	
	public void stop()
	{
		SBLocationManager.getInstance().locManager.removeUpdates(this);
	}	
	
	public void onLocationChanged(Location location) {
		location = SBLocationManager.getInstance().getLastBestLocation();
		LocationUpdater.getInstance().UpdateCurrentLocation(new SBLocation(location));
		
	}

	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

}
