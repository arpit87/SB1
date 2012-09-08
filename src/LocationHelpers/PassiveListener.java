package LocationHelpers;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class PassiveListener implements LocationListener {
	
	long minTime=0L;
	float minDistnce=0F;
	
	public void start()
	{
		SBLocationManager.getInstance().locManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, minTime, minDistnce, this);
	}
	
	public void start(long minTime,float minDistance)
	{
		this.minTime=minTime;
		this.minDistnce=minDistance;
		SBLocationManager.getInstance().locManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, minTime, minDistnce, this);
	}
	
	public void stop()
	{
		SBLocationManager.getInstance().locManager.removeUpdates(this);
	}	

	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
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
