package my.b1701.SB.LocationHelpers;

import java.util.List;

import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.HelperClasses.Constants;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Users.ThisUser;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

public class SBLocationManager {
	
	
	GPSListener gpsListener = null;
	NetworkListener networkListener = new NetworkListener();
	PassiveListener passiveListener = null;
	LocationUpdaterFromIntent locationUpdater = new LocationUpdaterFromIntent();
	ILastLocationFinder lastLocationFinder ;
	int MIN_ACCURACY = 2000; //2km this is to be passed to lastbestlocation,is accuracy less than this =>call new loc update
	
	private static  SBLocationManager instance = new SBLocationManager();
	public LocationManager locManager = (LocationManager) Platform.getInstance().getContext().getSystemService(Context.LOCATION_SERVICE);
	
	private SBLocationManager() {
		lastLocationFinder = Platform.getInstance().SUPPORTS_NEWAPI ? new NewAPILastLocationFinder(Platform.getInstance().getContext()) : new LegacyLastLocationFinder(Platform.getInstance().getContext());
		lastLocationFinder.setChangedLocationListener(networkListener);
	}
	
	public static SBLocationManager getInstance()
	{		
		return instance;
	}
	
	public void StartListeningtoGPS()
	{
		if(gpsListener==null)
			gpsListener = new GPSListener();
		gpsListener.start();		
	}
	
	public void StartListeningtoNetwork()
	{
		if(networkListener==null)
			networkListener = new NetworkListener();		
		networkListener.start();		
	}	
	
	
	public void StopListeningtoNetwork()
	{
		if(networkListener!=null)
			networkListener.stop();
	}
	
	public void StopListeningtoGPS()
	{
		if(gpsListener!=null)
			gpsListener.stop();
	}
	
	
	public void StartListeningtoNetwork(long minTime,float minDistance)
	{
		if(networkListener==null)
			networkListener = new NetworkListener();		
		networkListener.start(minTime,minDistance);		
	}
	
	public void StartListeningtoGPS(long minTime,float minDistance)
	{
		if(gpsListener==null)
			gpsListener = new GPSListener();		
		gpsListener.start(minTime,minDistance);		
	}
	
			
	public SBLocation getLastXSecBestLocation(long xSec ) {
	    Location bestResult = lastLocationFinder.getLastBestLocation(MIN_ACCURACY, System.currentTimeMillis()-xSec*1000);    
	        
	    if(bestResult!=null)
	      return new SBLocation(bestResult);
	    else return null;
		
	}
	
	public void enableMyLocation()
	{
		StartListeningtoNetwork();
		StartListeningtoGPS();
		
	}

	/*public SBLocation getCurrentBestLocation(Location location) {
		
		if (location != null) {
	        float accuracy = location.getAccuracy();
	        long time = location.getTime();
	        SBLocation currentLocation = ThisUser.getInstance().getCurrentLocation();
	        
	        if(currentLocation!=null)
	        {
	        	if ((time > currentLocation.getTime() && accuracy < currentLocation.getAccuracy()))
	        	{
	        		ThisUser.getInstance().setCurrentLocation(new SBLocation(location));
	        		MapListActivityHandler.getInstance().updateThisUserMapOverlay();
	        		return new SBLocation(location);
	        	}
	        
	        }
	        else
	        	ThisUser.getInstance().setCurrentLocation(new SBLocation(location));
	                		        
		}
		return ThisUser.getInstance().getCurrentLocation();
	}	*/
	
}

