package my.b1701.SB.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.Log;
import my.b1701.SB.LocationHelpers.SBLocation;
import my.b1701.SB.Users.ThisUser;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;


public class GeoAddressProvider extends ContentProvider {
    private static final String TAG = "my.b1701.SB.provider.GeoAddressProvider";
    private static final int MAX_RESULT = 5;
    public static final String AUTHORITY = "my.b1701.SB.provider.GeoAddressProvider";
    private static final String BASE_PATH = "addresses";
    private static final int _ID = 20;
    private static final double BOUNDS_DELTA = 0.5;

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/*", _ID);
    }

    public static Geocoder geocoder;

    @Override
    public boolean onCreate() {
        geocoder = new Geocoder(this.getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriType = uriMatcher.match(uri);

        MatrixCursor cursor;
        if (uriType == _ID) {
            cursor = new MatrixCursor(new String[]{"GeoAddress"});
            String searchAddress = uri.getLastPathSegment();
            List<Address> addressList = null;
            SBLocation currentLocation = ThisUser.getInstance().getCurrentLocation();
            double latitude = currentLocation.getLatitude();
            double longitude = currentLocation.getLongitude();
            try {
                addressList = geocoder.getFromLocationName(searchAddress, MAX_RESULT,
                        latitude - BOUNDS_DELTA,
                        longitude - BOUNDS_DELTA,
                        latitude + BOUNDS_DELTA,
                        longitude + BOUNDS_DELTA);
            } catch (IOException e) {
            	Log.e(TAG, "search address "+searchAddress);
                Log.e(TAG, e.getMessage());
            }
            if (addressList != null && !addressList.isEmpty()) {
                for (Address address : addressList) {
                    try {
                        cursor.addRow(new Object[]{new GeoAddress(address).getJson()});
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        } else {
            cursor = new MatrixCursor(null);
        }

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
