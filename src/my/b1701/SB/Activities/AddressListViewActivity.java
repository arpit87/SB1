package my.b1701.SB.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import my.b1701.SB.HttpClient.GetUsersRequest;
import my.b1701.SB.HttpClient.SBHttpClient;
import my.b1701.SB.HttpClient.SBHttpRequest;
import my.b1701.SB.LocationHelpers.LocationService;
import my.b1701.SB.LocationHelpers.SBGeoPoint;
import my.b1701.SB.R;
import my.b1701.SB.Users.ThisUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class AddressListViewActivity extends Activity {

    private static final String TAG = "AddressListViewActivity";
    private static final int MAX_RESULT = 5;

    private Geocoder myGeocoder;
    private SearchLocationLookupWorker locationLookupWorker;

    private EditText searchEditText;
    private Button searchButton;
    private ListView listviewResult;
    private Handler handler;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addresslist);
        Log.i(TAG, "started addresslist activity");

        searchEditText = (EditText) findViewById(R.id.searchedittext);
        searchButton = (Button) findViewById(R.id.searchbutton);
        listviewResult = (ListView) findViewById(R.id.list);
        myGeocoder = new Geocoder(this);
        locationLookupWorker = new SearchLocationLookupWorker();
        handler = new Handler();

        searchEditText.addTextChangedListener(textWatcher);
        searchButton.setOnClickListener(searchButtonOnClickListener);
        listviewResult.setOnItemClickListener(listviewResultOnItemClickListener);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String searchString = searchEditText.getText().toString();
            locationLookupWorker.lookupLocation(searchString);
        }
    };

    Button.OnClickListener searchButtonOnClickListener = new Button.OnClickListener(){

        @Override
        public void onClick(View view) {
            String searchString = searchEditText.getText().toString();
            locationLookupWorker.lookupLocation(searchString);
        }
    };

    OnItemClickListener listviewResultOnItemClickListener = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i(TAG, "address item clicked");
            int lat = (int) (((Address) parent.getItemAtPosition(position)).getLatitude() * 1e6);
            int lon = (int) (((Address) parent.getItemAtPosition(position)).getLongitude() * 1e6);
            ThisUser.getInstance().setShareReqGeoPoint();
            ThisUser.getInstance().setDestinationGeoPoint(new SBGeoPoint(lat, lon));
            Log.i(TAG, "user desti set..querying server");
            SBHttpRequest request = new GetUsersRequest();
            SBHttpClient.getInstance().executeRequest(request);
            Log.i(TAG, "got response ,processing");
            //response.process();
            Log.i(TAG, "processed response,finish activity n staring service");
            Intent startLocService = new Intent(AddressListViewActivity.this, LocationService.class);
            startService(startLocService);
            finish();
        }
    };

    private void updateResults(List<Address> result) {
        if (result == null) {
            result = new ArrayList<Address>(0);
        } else {
            AddresslistArrayAdapter adapter = new AddresslistArrayAdapter(AddressListViewActivity.this,
                    android.R.layout.simple_list_item_1, result);
            listviewResult.setAdapter(adapter);
        }
    }

    public class AddresslistArrayAdapter extends ArrayAdapter<Address> {

        private final Context mycontext;
        private final List<Address> addressList;

        class ViewHolder {
            public TextView text;
            public ImageView image;
        }

        public AddresslistArrayAdapter(Context context, int textViewResourceId, List<Address> objects) {
            super(context, textViewResourceId, objects);
            // TODO Auto-generated constructor stub
            addressList = objects;
            mycontext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            int maxAddressLineIndex = getItem(position).getMaxAddressLineIndex();
            String addressLine = "";

            for (int j = 0; j <= maxAddressLineIndex; j++) {
                addressLine += getItem(position).getAddressLine(j) + ",";
            }

            TextView rowAddress = new TextView(mycontext);
            rowAddress.setText(addressLine);

            return rowAddress;
        }

    }

    private class SearchLocationLookupWorker {
        private static final int MAX_SIZE = 50;

        private LinkedBlockingQueue<String> searchStrings = new LinkedBlockingQueue<String>(MAX_SIZE);
        private Thread helperThread;

        public SearchLocationLookupWorker() {
            helperThread = new Thread(new Helper());
            helperThread.start();
        }

        public void lookupLocation(String searchString) {
            try {
                searchStrings.put(searchString);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private class Helper implements Runnable {
            private static final long SLEEP_TIME = 100;

            @Override
            public void run() {
                try {
                    while (true) {
                        String name = searchStrings.take();
                        if (searchStrings.isEmpty()) {
                            Thread.sleep(SLEEP_TIME);
                            if (searchStrings.isEmpty()) {
                                try {
                                    final List<Address> result = myGeocoder.getFromLocationName(name, MAX_RESULT);
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            updateResults(result);
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
