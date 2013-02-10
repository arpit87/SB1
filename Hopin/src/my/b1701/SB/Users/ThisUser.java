package my.b1701.SB.Users;

import android.util.Log;
import my.b1701.SB.LocationHelpers.SBGeoPoint;
import my.b1701.SB.LocationHelpers.SBLocation;
import my.b1701.SB.provider.GeoAddress;


public class ThisUser {
	
	
	private SBLocation currlocation=null;
	//private SBLocation dstlocation=null;

	private SBGeoPoint currentGeoPoint=null;
	private SBGeoPoint sourceGeoPoint=null;
	private SBGeoPoint destinationGeoPoint=null;	
    private GeoAddress sourceGeoAddress = null;
	private GeoAddress destinationGeoAddress=null;
	private String timeOfRequest = "";
	private String dateOfRequest = "";
	private int take_offer_type = 0; //0=>offer 1=>share
	private int daily_instant_type = 0;//pool 0.instant 1
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
	 * take => 0
	 * @return
	 */
	public int get_Take_Offer_Type() {
		return take_offer_type;
	}
	
	public void set_Take_Offer_Type(int i)
	{
		take_offer_type = i;
	}
	/**
	 * if instant => 1
	 * pool => 0
	 * @return
	 */
	public int get_Daily_Instant_Type() {
		Log.i(TAG,"get getCarPoolInstantShareType type"+this.userID);
			return daily_instant_type;
	}

	public void set_Daily_Instant_Type(int i)
	{
		daily_instant_type = i;
	}

	
	public SBLocation getLocation() {
		 Log.i(TAG,"getlocation called");
		return currlocation;
	}

	public void setCurrentLocation(SBLocation location) {
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

	public SBGeoPoint getCurrentGeoPoint() {
		return currentGeoPoint;
	}
	
	public void setCurrentGeoPoint(SBGeoPoint currentGeoPoint) {
		this.currentGeoPoint = currentGeoPoint;
	}

	public SBGeoPoint getSourceGeoPoint() {
		return sourceGeoPoint;
	}

	public void setSourceGeoPoint(SBGeoPoint srcGeoPoint) {		
			this.sourceGeoPoint = srcGeoPoint;		
	}

	public String getTimeOfRequest() {
		return timeOfRequest;
	}

	public void setDateOfRequest(String dateOfRequest) {
		this.dateOfRequest = timeOfRequest;
	}	
	
	public String getDateOfRequest() {
		return dateOfRequest;
	}

	public void setTimeOfRequest(String timeOfRequest) {
		this.timeOfRequest = timeOfRequest;
	}
	
	public String getDateAndTimeOfRequest() {
		return dateOfRequest + " " + timeOfRequest;
	}

    public GeoAddress getSourceGeoAddress(){
        return sourceGeoAddress;
    }

    public GeoAddress getDestinationGeoAddress() {
        return destinationGeoAddress;
    }

    public void setDestinationGeoAddress(GeoAddress geoAddress) {
        this.destinationGeoAddress = geoAddress;
    }

    public void setSourceGeoAddress(GeoAddress geoAddress) {
        this.sourceGeoAddress = geoAddress;
    }
}
