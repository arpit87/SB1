package my.b1701.SB.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;

import java.io.IOException;
import java.util.List;


public class GeoAddressProvider extends ContentProvider {
    private static final int MAX_RESULT = 10;
    public static final String AUTHORITY = "my.b1701.SB.provider";
    private static final String BASE_PATH = "addresses";
    private static final int _ID = 20;

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/*", _ID);
    }

    private Geocoder geocoder;

    @Override
    public boolean onCreate() {
        geocoder = new Geocoder(this.getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriType = uriMatcher.match(uri);

        MatrixCursor cursor;
        if (uriType == _ID){
            cursor  = new MatrixCursor(new String[] {"GEO_ADDRESS"});
            String searchAddress = uri.getLastPathSegment();
            List<Address> addressList = null;
            try {
                addressList = geocoder.getFromLocationName(searchAddress, MAX_RESULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addressList != null && !addressList.isEmpty()) {
                for (Address address : addressList){
                    cursor.addRow(new Object[]{new GeoAddress(address)});
                }
            }
        }
        else {
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
