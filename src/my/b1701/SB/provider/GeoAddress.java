package my.b1701.SB.provider;

import android.location.Address;

public class GeoAddress {
    private Address address;
    private String addressLine;

    public String getAddressLine() {
        return addressLine;
    }

    public GeoAddress(Address address) {
        this.address = address;
        this.addressLine = constructAddressLine();
    }

    private String constructAddressLine() {
        int maxAddressLineIndex = address.getMaxAddressLineIndex();
        String addressLine = "";
        for (int j = 0; j < maxAddressLineIndex; j++) {
            addressLine += address.getAddressLine(j) + ",";
        }
        addressLine += address.getAddressLine(maxAddressLineIndex);
        return addressLine;
    }

    @Override
    public String toString() {
        return addressLine;
    }
}
