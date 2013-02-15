package my.b1701.SB.LocationHelpers;

import java.io.IOException;
import java.util.List;

import android.location.Address;

import com.google.android.maps.GeoPoint;
import my.b1701.SB.HelperClasses.Constants;
import my.b1701.SB.provider.GeoAddress;
import my.b1701.SB.provider.GeoAddressProvider;


public class SBGeoPoint extends GeoPoint{

	int lati;
	int longi;
    String subLocality = "";
    String address = "";

	public SBGeoPoint(int lati, int longi, String subLocality, String address) {
		super(lati, longi);
		this.lati=lati;
		this.longi = longi;
        this.subLocality = subLocality;
        this.address = address;
	}
	
	public SBGeoPoint(int lati, int longi) {
		super(lati, longi);
		this.lati=lati;
		this.longi = longi;
	try {
        List<Address> addressList = GeoAddressProvider.geocoder.getFromLocation(lati/1e6, longi/1e6, 1);
        if (addressList != null  && !addressList.isEmpty()) {
            Address addr = addressList.get(0);
            this.subLocality = addr.getSubLocality();
            this.address = GeoAddress.constructAddressLine(addr);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
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
