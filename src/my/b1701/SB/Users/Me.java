package my.b1701.SB.Users;

import android.location.Location;
import android.util.Log;


public class Me {
	
	/*
	 * Singleton
	 */
	private static Me instance;
	    
	public static synchronized Me getInstance() {
		if (instance == null) {
			instance = new Me();
	    } 
	    return instance;
	}
	 
	/*
	 * Fields
	 */
	private String uniqueID;	
	
	private Location currentLocation;
	
	 
	/*
     * Properties
     */
	
	
	public Location getCurrentLocation() {
		return currentLocation;
	}
	public void setCurrentLocation(Location currentLocation) {
		if(currentLocation != null){
			Log.i("setCurrentLocation", "Set!");
		}
		this.currentLocation = currentLocation;
	}	
	
	
}