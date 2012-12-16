package my.b1701.SB.provider;

import android.location.Address;
import org.json.JSONException;
import org.json.JSONObject;

public class GeoAddress {
    private String addressLine;
    private double latitude;
    private double longitude;
    private String json;
    private String subLocality;

    public String getAddressLine() {
        return addressLine;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getSubLocality() {
        return subLocality;
    }

    public String getJson(){
        return json;
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

    private String constructJson(String addressLine, double lat, double lon, String subLocality) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("NAME", addressLine);
        jsonObject.put("LAT", lat);
        jsonObject.put("LONG", lon);
        jsonObject.put("SUBLOCALITY", subLocality);
        return jsonObject.toString();
    }


    public static String getName(String geoAddressJson) throws JSONException {
        JSONObject jsonObject = new JSONObject(geoAddressJson);
        return jsonObject.getString("NAME");
    }

    public GeoAddress(Address address) throws JSONException {
        this.addressLine = constructAddressLine(address);
        this.latitude = address.getLatitude();
        this.longitude = address.getLongitude();
        this.subLocality = address.getSubLocality();
        this.json = constructJson(addressLine, latitude, longitude, subLocality);
    }
    public GeoAddress(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        this.addressLine = jsonObject.getString("NAME");
        this.latitude = jsonObject.getDouble("LAT");
        this.longitude = jsonObject.getDouble("LONG");
        this.subLocality = jsonObject.getString("SUBLOCALITY");
        this.json = json;
    }

    @Override
    public String toString() {
        return addressLine;
    }
}
