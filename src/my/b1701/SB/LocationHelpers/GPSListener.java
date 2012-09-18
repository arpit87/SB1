package my.b1701.SB.LocationHelpers;

import my.b1701.SB.Platform.Platform;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class GPSListener implements LocationListener{

	long minTime=0L;
	float minDistance=0F;
	private static final String TAG = "GPSListener";
	private Location prevLocation = null;
	private Location thisWindowBestLocation = null;	
	private boolean newWindow = false;
	public static final int OUT_OF_SERVICE = 0;	
	public static final int TEMPORARILY_UNAVAILABLE = 1;	
	public static final int AVAILABLE = 2;
	
	public void start()
	{		
		SBLocationManager.getInstance().locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	}
	
	public void start(long minTime,float minDistance)
	{
		this.minTime=minTime;
		this.minDistance=minDistance;
		Log.i(TAG,"starting gps");
		SBLocationManager.getInstance().locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);
	}
	
	public void stop()
	{
		SBLocationManager.getInstance().locManager.removeUpdates(this);
	}	
	
	public void onLocationChanged(Location location) {
		//we see that location bursts come in windows n then idle for given time
		//so we find most accurate location of a window and set it to thisWindowBest
		Log.i(TAG,"gps location changed");
		Log.i(TAG,"newlocation :"+ location.toString());
		if(thisWindowBestLocation == null)
		{		
			//window starting
			thisWindowBestLocation = location;
			return;
		}
		else if(location.getAccuracy() > thisWindowBestLocation.getAccuracy() )
		{
			//window continuing
			Log.i(TAG,"thiswindowbest location:"+thisWindowBestLocation.toString());
			thisWindowBestLocation = location;			
		}				
		
	}

	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.i(TAG,"gps status:"+status);
		//Toast toast = Toast.makeText(Platform.getInstance().getContext(), "GPS status change to"+status, Toast.LENGTH_SHORT);       
		//toast.show();
		if(status == AVAILABLE) //window starting
			thisWindowBestLocation = null;
		if(status == TEMPORARILY_UNAVAILABLE) //window ending
			LocationUpdater.getInstance().UpdateCurrentLocation(new SBLocation(thisWindowBestLocation));
		
	}	

}
