package my.b1701.SB.provider;

import android.location.Address;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class GeoAddress {
	private final String TAG = "GeoAddress";
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
        jsonObject.put("SUBLOCALITY", (subLocality != null) ? subLocality : JSONObject.NULL);
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
        String locality = address.getSubLocality();
        locality = locality == null ? address.getPremises() : locality;
        locality = locality == null ? address.getSubAdminArea() : locality;
        locality = locality == null ? address.getLocality() : locality;
        this.subLocality = locality;
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

    public void resetLocalityIfNull(){
    	Log.e(TAG, "resetLocalityIfNull called:"+this.subLocality);
        if (this.subLocality.equals("null")){
            List<Address> addressList = null;
            Log.e(TAG, "sublocality null");
            try {
                addressList = GeoAddressProvider.geocoder.getFromLocation(latitude, longitude, 1);
                Log.e(TAG, "listaddress"+addressList);
            } catch (IOException e) {
                Log.e("GeoAddress", e.getMessage());
                
            }

            if (addressList != null && !addressList.isEmpty()) {
                Address addr = addressList.get(0);
                subLocality = addr.getSubLocality();
                try {
                    json = constructJson(addressLine, latitude, longitude, subLocality);
                    Log.e(TAG,json.toString());
                } catch (JSONException e) {
                    Log.e("GeoAddress", e.getMessage());
                }
            }
        }
    }

    @Override
    public String toString() {
        return addressLine;
    }
}
