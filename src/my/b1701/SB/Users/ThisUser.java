package my.b1701.SB.Users;

import my.b1701.SB.LocationHelpers.SBGeoPoint;
import my.b1701.SB.LocationHelpers.SBLocation;
import android.location.Location;
import android.util.Log;


public class ThisUser {
	
	/*
	 * Singleton
	 */
	
	private static final String TAG = "ThisUser";  
	private static ThisUser instance=new ThisUser();
	public static ThisUser getInstance() {
		 return instance;
	}
	 
	/*
	 * Fields
	 */
	private String uniqueID;	
	public void setUniqueID(String uniqueID) {
		Log.i(TAG,"set unique id");
		this.uniqueID = uniqueID;
	}

	private SBLocation location=null;

	private SBGeoPoint currentGeoPoint=null;
	private SBGeoPoint shareReqGeoPoint=null;
	private SBGeoPoint destinationGeoPoint=null;
	
	public SBLocation getLocation() {
		 Log.i(TAG,"getlocation called");
		return location;
	}

	public void setLocation(SBLocation location) {
		this.location = location;		 
		currentGeoPoint = new SBGeoPoint((int)(location.getLatitude()*1e6),(int)(location.getLongitude()*1e6));
		Log.i(TAG,"setting location"+currentGeoPoint.toString());
	}

	
	 
	/*
     * Properties
     */
	
	public SBGeoPoint getDestinationGeoPoint() {
		return destinationGeoPoint;
	}

	public void setDestinationGeoPoint(SBGeoPoint sbGeoPoint) {
		Log.i(TAG,"setting destination location to:"+sbGeoPoint.toString());
		this.destinationGeoPoint = sbGeoPoint;
	}

	public String getUniqueID() {
		Log.i(TAG,"get uniqueid");
		return uniqueID;
	}
	
	public SBGeoPoint getCurrentGeoPoint() {
		Log.i(TAG,"get curr loc");
		
			return currentGeoPoint;
		
			
	}
	public void setCurrentGeoPoint(SBGeoPoint currentGeoPoint) {
		if(currentGeoPoint != null){
			Log.i(TAG, "Set!");
		}
		this.currentGeoPoint = currentGeoPoint;
	}

	public SBGeoPoint getShareReqGeoPoint() {
		return shareReqGeoPoint;
	}

	public void setShareReqLocation(SBGeoPoint shareReqLocation) {
		this.shareReqGeoPoint = shareReqLocation;
	}	
	
	
}