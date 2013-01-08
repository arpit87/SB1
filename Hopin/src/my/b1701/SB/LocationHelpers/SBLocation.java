package my.b1701.SB.LocationHelpers;

import android.location.Address;
import android.location.Location;
import my.b1701.SB.provider.GeoAddress;
import my.b1701.SB.provider.GeoAddressProvider;
import java.io.IOException;
import java.util.List;

public class SBLocation extends Location{

    //sublocality and address may be null if failed to get address from geocoder
    private String subLocality;
    private String address;

	public SBLocation(Location l) {
		super(l);
        try {
            List<Address> addressList = GeoAddressProvider.geocoder.getFromLocation(l.getLatitude(), l.getLongitude(), 1);
            if (addressList != null  && !addressList.isEmpty()) {
                Address addr = addressList.get(0);
                this.subLocality = addr.getSubLocality();
                this.address = GeoAddress.constructAddressLine(addr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public String getSubLocality(){
        return subLocality;
    }

    public String getAddress() {
        return address;
    }
}
