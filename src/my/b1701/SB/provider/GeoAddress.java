package my.b1701.SB.provider;

import android.location.Address;
import org.json.JSONException;
import org.json.JSONObject;

public class GeoAddress {
    private String addressLine;
    private String locationJson;
    private double latitude;
    private double longitude;

    public String getAddressLine() {
        return addressLine;
    }

    public String getLocationJSon() {
        return locationJson;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public static String constructAddressLine(Address address) {
        int maxAddressLineIndex = address.getMaxAddressLineIndex();
        String addressLine = "";
        for (int j = 0; j < maxAddressLineIndex; j++) {
            addressLine += address.getAddressLine(j) + ",";
        }
        addressLine += address.getAddressLine(maxAddressLineIndex);
        return addressLine;
    }

    public static String constructLocationJSon(Address address) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("LAT", address.getLatitude());
        jsonObject.put("LONG", address.getLongitude());

        return jsonObject.toString();
    }

    public static double[] getLatLongFromJson(String locationJson) throws JSONException {
        JSONObject jsonObject = new JSONObject(locationJson);
        return new double[]{jsonObject.getDouble("LAT"), jsonObject.getDouble("LONG")};
    }

    public GeoAddress(String addressLine, String locationJson) throws JSONException {
        this.addressLine = addressLine;
        this.locationJson = locationJson;

        JSONObject jsonObject = new JSONObject(locationJson);
        this.latitude = jsonObject.getDouble("LAT");
        this.longitude = jsonObject.getDouble("LONG");
    }

    @Override
    public String toString() {
        return addressLine;
    }
}
