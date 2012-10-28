package my.b1701.SB.LocationHelpers;

import com.google.android.maps.GeoPoint;

public class SBGeoPoint extends GeoPoint{

	int lati;
	int longi;
	public SBGeoPoint(int lati, int longi) {		
		super(lati, longi);
		this.lati=lati;
		this.longi = longi;
		
	}

	public SBGeoPoint(SBLocation location) {
		super((int)(location.getLatitude()*1e6), (int)(location.getLongitude()*1e6));
		lati = (int)(location.getLatitude()*1e6);
		longi = (int)(location.getLongitude()*1e6);
		
		
	}
}
