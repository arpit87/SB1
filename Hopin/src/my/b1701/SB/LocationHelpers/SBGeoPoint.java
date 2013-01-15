package my.b1701.SB.LocationHelpers;

import com.google.android.maps.GeoPoint;
import my.b1701.SB.HelperClasses.Constants;


public class SBGeoPoint extends GeoPoint{

	int lati;
	int longi;
    String subLocality;
    String address;

	public SBGeoPoint(int lati, int longi, String subLocality, String address) {
		super(lati, longi);
		this.lati=lati;
		this.longi = longi;
        this.subLocality = subLocality;
        this.address = address;
	}

	public SBGeoPoint(SBLocation location) {
		super((int)(location.getLatitude()*1e6), (int)(location.getLongitude()*1e6));
		lati = (int)(location.getLatitude()*1e6);
		longi = (int)(location.getLongitude()*1e6);
        subLocality = location.getSubLocality();
        address = location.getAddress();
	}
	
	public double getLatitude()
	{
		return lati/1e6;
	}
	
	public double getLongitude()
	{
		return longi/1e6;
	}

    public String getSubLocality(){
        return subLocality;
    }

    public String getAddress(){
        return address;
    }
    
    public double distanceFromSBGeoPoint(SBGeoPoint other) {
        if (other == null) {
            throw new RuntimeException("Got other location as null");
        }

        double x = ((this.getLongitudeE6() - other.getLongitudeE6())/1e6) * Math.cos((this.getLatitudeE6() + other.getLatitudeE6()) / (2*1e6));
        double y = (this.getLatitudeE6() - other.getLatitudeE6())/1e6;
        return Math.sqrt(x * x + y * y) * Constants.EARTHRADIUS;
    }
}
