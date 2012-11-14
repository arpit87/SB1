package my.b1701.SB.Activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import my.b1701.SB.HttpClient.GetUsersRequest;
import my.b1701.SB.HttpClient.SBHttpClient;
import my.b1701.SB.HttpClient.SBHttpRequest;
import my.b1701.SB.LocationHelpers.LocationService;
import my.b1701.SB.LocationHelpers.SBGeoPoint;
import my.b1701.SB.R;
import my.b1701.SB.Users.ThisUser;
import my.b1701.SB.provider.CustomSuggestionProvider;
import my.b1701.SB.provider.GeoAddress;
import my.b1701.SB.provider.GeoAddressProvider;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class SearchableActivity extends Activity {
    public static final String TAG = "SearchableActivity";

    private TextView textView;
    private ListView listView;

    AdapterView.OnItemClickListener listviewResultOnItemClickListener
            = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i(TAG, "address item clicked");
            int lat = (int) (((GeoAddress) parent.getItemAtPosition(position)).getLatitude() * 1e6);
            int lon = (int) (((GeoAddress) parent.getItemAtPosition(position)).getLongitude() * 1e6);
            setDestination(lat, lon);
        }
    };

    public void setDestination(int lat, int lon) {
        ThisUser.getInstance().setShareReqGeoPoint();
        ThisUser.getInstance().setDestinationGeoPoint(new SBGeoPoint(lat, lon));
        Log.i(TAG, "user destination set... querying server");
        SBHttpRequest request = new GetUsersRequest();
        SBHttpClient.getInstance().executeRequest(request);
        Log.i(TAG, "got response, processing");
        Log.i(TAG, "processed response, finish activity n starting service");
        Intent startLocService = new Intent(SearchableActivity.this, LocationService.class);
        startService(startLocService);
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        textView = (TextView) findViewById(R.id.resultsStatus);
        listView = (ListView) findViewById(R.id.resultsList);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String searchAddress = intent.getStringExtra(SearchManager.QUERY);

            if (intent.getDataString() == null) { // if not null already in suggestions table
                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, CustomSuggestionProvider.AUTHORITY, CustomSuggestionProvider.MODE);
                suggestions.saveRecentQuery(searchAddress, null);
                //TODO clear history
            }

            Uri uri = Uri.parse(GeoAddressProvider.CONTENT_URI + "/" + searchAddress);
            Cursor cursor = getContentResolver().query(uri, null, null, null, "");

            if (cursor == null || cursor.getCount() == 0) {
                textView.setText(getString(R.string.no_results, new Object[]{searchAddress}));
            } else {
                int count = cursor.getCount();
                String countString = getResources().getQuantityString(R.plurals.search_results, count, new Object[]{count, searchAddress});
                textView.setText(countString);

                List<GeoAddress> address = new ArrayList<GeoAddress>(count);
                if (cursor.moveToFirst()) {
                    do {
                        try {
                            GeoAddress geoAddress = new GeoAddress(cursor.getString(0), cursor.getString(1));
                            address.add(geoAddress);
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    } while (cursor.moveToNext());
                }

                ArrayAdapter<GeoAddress> adapter = new ArrayAdapter<GeoAddress>(this, android.R.layout.simple_list_item_1, address);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(listviewResultOnItemClickListener);
            }
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String locationJson = intent.getDataString();
            try {
                double[] latLong = GeoAddress.getLatLongFromJson(locationJson);
                int lat = (int) (latLong[0] * 1e6);
                int lon = (int) (latLong[1] * 1e6);
                setDestination(lat, lon);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }
}