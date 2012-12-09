package my.b1701.SB.LocationHelpers;

import java.util.List;

import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.HelperClasses.Constants;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Users.ThisUser;
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
	NetworkListener networkListener = null;
	PassiveListener passiveListener = null;
	LocationUpdater locationUpdater = null;
	
	private static  SBLocationManager instance = new SBLocationManager();
	public LocationManager locManager = (LocationManager) Platform.getInstance().getContext().getSystemService(Context.LOCATION_SERVICE);
	
	private SBLocationManager() {
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
	
	public void requestSingleLocationUpdate()
	{
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_LOW);
		Intent updateIntent = new Intent(Constants.SINGLE_LOCATION_UPDATE);  
        PendingIntent singleUpatePI = PendingIntent.getBroadcast(Platform.getInstance().getContext(), 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        IntentFilter locIntentFilter = new IntentFilter(Constants.SINGLE_LOCATION_UPDATE);
        Platform.getInstance().getContext().registerReceiver((BroadcastReceiver)locationUpdater, locIntentFilter);      
        //locManager.requestSingleUpdate(criteria, singleUpatePI);		
	}

	//need to start listening first
	public SBLocation getLastXMinBestLocation(long xMin ) {
	    Location bestResult = null;
	    float bestAccuracy = Float.MAX_VALUE;
	    long bestTime = Long.MIN_VALUE;	    
	    long minTime = System.currentTimeMillis() - xMin*60*1000;
	    List<String> matchingProviders = locManager.getAllProviders();
	    for (String provider: matchingProviders) {
	      Location location = locManager.getLastKnownLocation(provider);
	      if (location != null) {
	          float accuracy = location.getAccuracy();
	          long time = location.getTime();
	          
	          if ((time > minTime && accuracy < bestAccuracy)) {
	            bestResult = location;
	            bestAccuracy = accuracy;
	            bestTime = time;
	          }
	          /*else if (time < minTime && bestAccuracy == Float.MAX_VALUE && time > bestTime) {
	            bestResult = location;
	            bestTime = time;
	          }*/
	        
	        }
	      }
	        
	    if(bestResult!=null)
	      return new SBLocation(bestResult);
	    else return null;
		
	}
	
	public void enableMyLocation()
	{
		StartListeningtoNetwork();
		StartListeningtoGPS();
		
	}

	public SBLocation getCurrentBestLocation(Location location) {
		
		if (location != null) {
	        float accuracy = location.getAccuracy();
	        long time = location.getTime();
	        SBLocation currentLocation = ThisUser.getInstance().getLocation();
	        
	        if(currentLocation!=null)
	        {
	        	if ((time > currentLocation.getTime() && accuracy < currentLocation.getAccuracy()))
	        	{
	        		ThisUser.getInstance().setLocation(new SBLocation(location));
	        		MapListActivityHandler.getInstance().updateThisUserMapOverlay();
	        		return new SBLocation(location);
	        	}
	        
	        }
	        else
	        	ThisUser.getInstance().setLocation(new SBLocation(location));
	                		        
		}
		return ThisUser.getInstance().getLocation();
	}	
	
}

