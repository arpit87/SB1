package my.b1701.SB.Activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import my.b1701.SB.R;
import my.b1701.SB.provider.CustomSuggestionProvider;
import my.b1701.SB.provider.GeoAddressProvider;

import java.util.ArrayList;
import java.util.List;

public class SearchableActivity extends Activity {

    private TextView textView;
    private ListView listView;
    
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
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, CustomSuggestionProvider.AUTHORITY, CustomSuggestionProvider.MODE);
            suggestions.saveRecentQuery(searchAddress, null);
            //TODO clear history

            Uri uri = Uri.parse(GeoAddressProvider.CONTENT_URI + "/" + searchAddress);
            Cursor cursor = getContentResolver().query(uri, null, null, null, "");

            if (cursor == null || cursor.getCount() == 0){
                textView.setText(getString(R.string.no_results, new Object[] {searchAddress}));
            } else {
                int count = cursor.getCount();
                String countString = getResources().getQuantityString(R.plurals.search_results, count, new Object[]{count, searchAddress});
                textView.setText(countString);

                List<String> address = new ArrayList<String>(count);
                if (cursor.moveToFirst()) {
                    do {
                        address.add(cursor.getString(0));
                    } while (cursor.moveToNext());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, address);
                listView.setAdapter(adapter);
            }
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())){
            Log.e("####", intent.getData() + "/" + intent.getDataString());
        }
    }
}
