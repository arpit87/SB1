package my.b1701.SB.Activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import my.b1701.SB.HttpClient.AddThisUserSrcDstRequest;
import my.b1701.SB.HttpClient.SBHttpClient;
import my.b1701.SB.HttpClient.SBHttpRequest;
import my.b1701.SB.LocationHelpers.SBGeoPoint;
import my.b1701.SB.R;
import my.b1701.SB.Users.ThisUser;
import my.b1701.SB.provider.CustomSuggestionProvider;
import my.b1701.SB.provider.GeoAddress;
import my.b1701.SB.provider.GeoAddressProvider;
import my.b1701.SB.provider.SearchRecentSuggestions;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class SearchableActivity extends Activity {
    public static final String TAG = "my.b1701.SB.Activities.SearchableActivity";

    private TextView textView;
    private ListView listView;

    SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(this, CustomSuggestionProvider.AUTHORITY, CustomSuggestionProvider.MODE);

    AdapterView.OnItemClickListener listviewResultOnItemClickListener
            = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i(TAG, "address item clicked");
            GeoAddress geoAddress = (GeoAddress) parent.getItemAtPosition(position);
            geoAddress.resetLocalityIfNull();
            searchRecentSuggestions.saveRecentQuery(geoAddress.getAddressLine(), geoAddress.getJson());
            setDestination(geoAddress);
        }
    };

    public void setDestination(GeoAddress geoAddress) {
        ThisUser.getInstance().setShareReqGeoPoint();
        int lat = (int) (geoAddress.getLatitude() * 1e6);
        int lon = (int) (geoAddress.getLongitude() * 1e6);
        String subLocality = geoAddress.getSubLocality();
        String address = geoAddress.getAddressLine();

        ThisUser.getInstance().setDestinationGeoPoint(new SBGeoPoint(lat, lon, subLocality, address));
        Log.i(TAG, "user destination set... querying server");
        SBHttpRequest addThisUserSrcDstRequest = new AddThisUserSrcDstRequest();
        SBHttpClient.getInstance().executeRequest(addThisUserSrcDstRequest);
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
                            GeoAddress geoAddress = new GeoAddress(cursor.getString(0));
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
            String geoAddressJson = intent.getDataString();
            Log.d(TAG,geoAddressJson);
            boolean isSuggestionSaved = "true".equals(intent.getStringExtra(SearchManager.EXTRA_DATA_KEY));
            try {
                GeoAddress geoAddress = new GeoAddress(geoAddressJson);
                if (!isSuggestionSaved){
                    geoAddress.resetLocalityIfNull();
                    searchRecentSuggestions.saveRecentQuery(geoAddress.getAddressLine(), geoAddress.getJson());
                }                
                setDestination(geoAddress);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }
}
