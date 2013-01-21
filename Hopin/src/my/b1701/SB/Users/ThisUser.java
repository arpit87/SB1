package my.b1701.SB.Users;

import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.LocationHelpers.SBGeoPoint;
import my.b1701.SB.LocationHelpers.SBLocation;
import my.b1701.SB.provider.GeoAddress;
import android.util.Log;


public class ThisUser {
	
	
	private SBLocation currlocation=null;
	//private SBLocation dstlocation=null;

	private SBGeoPoint currentGeoPoint=null;
	private SBGeoPoint shareReqGeoPoint=null;
	private SBGeoPoint destinationGeoPoint=null;
	private GeoAddress currentGeoAddress=null;
	private GeoAddress shareReqGeoAddress=null;
	private GeoAddress destinationGeoAddress=null;
	private String timeOfRequest;
	/*
	 * Singleton
	 */
	
	private static final String TAG = "my.b1701.SB.Users.ThisUser";
	private static ThisUser instance=new ThisUser();
	public static ThisUser getInstance() {
		 return instance;
	}
	 
	/*
	 * Fields
	 */
	private String userID;	
	public void setUserID(String userID) {
		Log.i(TAG,"set user id");
		this.userID = userID;
	}
	
	public String getUserID() {
		Log.i(TAG,"get user id"+this.userID);
		return this.userID;
	}
	
	/**
	 * if offering => 1
	 * seeking => 0
	 * @return
	 */
	public int getrequestType() {
		Log.i(TAG,"get req type"+this.userID);
		if(ThisUserConfig.getInstance().getBool(ThisUserConfig.IsOfferMode))
			return 1;
		else
			return 0;
	}


	
	public SBLocation getLocation() {
		 Log.i(TAG,"getlocation called");
		return currlocation;
	}

	public void setLocation(SBLocation location) {
		this.currlocation = location;		 
		currentGeoPoint = new SBGeoPoint(location);
		Log.i(TAG,"setting location"+currentGeoPoint.toString());
	}
	
	public SBLocation getCurrentLocation()
	{
		return this.currlocation;
	}
	
	//thr is no dst location but only geopoint
	/*public void setDstLocation(SBLocation location) {
		this.dstlocation = location;		 
		destinationGeoPoint = new SBGeoPoint((int)(location.getLatitude()*1e6),(int)(location.getLongitude()*1e6));
		Log.i(TAG,"setting location"+destinationGeoPoint.toString());
	}*/

	
	 
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

	public SBGeoPoint getSourceGeoPoint() {
		Log.i(TAG,"get curr loc");
		
			return currentGeoPoint;
		
			
	}
	public void setSourceGeoPoint(SBGeoPoint currentGeoPoint) {
		if(currentGeoPoint != null){
			Log.i(TAG, "Set!");
		}
		this.currentGeoPoint = currentGeoPoint;
	}

	public SBGeoPoint getShareReqGeoPoint() {
		return shareReqGeoPoint;
	}

	public void setShareReqGeoPoint() {
		if(currlocation!=null)
			this.shareReqGeoPoint = this.currentGeoPoint;
		else
			Log.i(TAG,"current share loc found to be null");
	}

	public String getTimeOfRequest() {
		return timeOfRequest;
	}

	public void setTimeOfRequest(String timeOfRequest) {
		this.timeOfRequest = timeOfRequest;
	}	
	
	
}
