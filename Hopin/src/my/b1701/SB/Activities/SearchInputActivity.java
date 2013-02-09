package my.b1701.SB.Activities;


import java.util.Calendar;
import java.util.List;

import my.b1701.SB.R;
import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.Adapter.AddressAdapter;
import my.b1701.SB.HelperClasses.ProgressHandler;
import my.b1701.SB.HttpClient.AddThisUserScrDstCarPoolRequest;
import my.b1701.SB.HttpClient.AddThisUserSrcDstRequest;
import my.b1701.SB.HttpClient.SBHttpClient;
import my.b1701.SB.HttpClient.SBHttpRequest;
import my.b1701.SB.LocationHelpers.SBGeoPoint;
import my.b1701.SB.Users.ThisUser;
import my.b1701.SB.provider.CustomSuggestionProvider;
import my.b1701.SB.provider.GeoAddress;
import my.b1701.SB.provider.GeoAddressProvider;
import my.b1701.SB.provider.HistoryContentProvider;
import my.b1701.SB.provider.SearchRecentSuggestions;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SearchInputActivity extends Activity implements SeekBar.OnSeekBarChangeListener{
    private static final String TAG = "my.b1701.SB.Activities.SearchInputActivity";
    private static final int MAX_HISTORY_COUNT = 10;
    private static Uri mHistoryUri = Uri.parse("content://" + HistoryContentProvider.AUTHORITY + "/history");
    private static Uri mGeoAddressUri = Uri.parse("content://" + GeoAddressProvider.AUTHORITY + "/history");

    private static String[] columns = new String[]{ "sourceLocation",
            "destinationLocation",
            "timeOfRequest",
            "dailyInstantType",
            "takeOffer",
            "date"};

    SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(this, CustomSuggestionProvider.AUTHORITY, CustomSuggestionProvider.MODE);

    AutoCompleteTextView source;
    AutoCompleteTextView destination;
	ToggleButton am_pm_toggle;
	TextView timeView;
	SeekBar timeSeekbar;
    GeoAddress sourceAddress;
    GeoAddress destinationAddress;
    Button cancelFindUsers;
    Button findUsers;
    ToggleButton offerRide;
    ToggleButton dailyCarPool;
    
    //these change as user chooses time
    String hourStr = "";
	String minstr = "";
	int hour;
	int minutes;
    

	public void saveSuggestion(GeoAddress geoAddress) {
        geoAddress.resetLocalityIfNull();
        searchRecentSuggestions.saveRecentQuery(geoAddress.getAddressLine(), geoAddress.getJson());
    }

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getuser_request_dialog);
        source = (AutoCompleteTextView) findViewById(R.id.getuserpopupsource);
        destination = (AutoCompleteTextView) findViewById(R.id.getuserpopupdestination);
        findUsers = (Button)findViewById(R.id.btn_findusers);
        cancelFindUsers = (Button)findViewById(R.id.btn_cancelfindusers);
        //offerRide = (ToggleButton)findViewById(R.id.btn_toggle_offer);
        //dailyCarPool = (ToggleButton)findViewById(R.id.btn_toggle_dailypool);
        am_pm_toggle = (ToggleButton) findViewById(R.id.btn_am_pm_toggle);
        timeView = (TextView) findViewById(R.id.time);
      
        //set source to our found location, if not found user can enter himself
        SBGeoPoint currGeopoint = ThisUser.getInstance().getSourceGeoPoint();
        if(currGeopoint!=null)
        {
        	String foundAddress = currGeopoint.getAddress();        
        	source.setText(foundAddress);
            try {
                SBGeoPoint sourceGeoPoint = ThisUser.getInstance().getSourceGeoPoint();
                List<Address> addressList = GeoAddressProvider.geocoder.getFromLocation(sourceGeoPoint.getLatitude(), sourceGeoPoint.getLongitude(), 1);
                ThisUser.getInstance().setSourceGeoAddress(new GeoAddress(addressList.get(0)));
            } catch (Exception e) {
                Log.e("GeoAddress", e.getMessage());
            }
        }
        
        findUsers.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View v) {       
        		String time24HrFormat ;
        		if(am_pm_toggle.isChecked())
        			time24HrFormat = Integer.toString(hour) +":" + Integer.toString(minutes);
        		else 
        			time24HrFormat = Integer.toString(hour+12) + ":" + Integer.toString(minutes);
        		
        		ThisUser.getInstance().setTimeOfRequest(time24HrFormat);
        		ThisUser.getInstance().set_Daily_Instant_Type(dailyCarPool.isChecked()?0:1);//0 daily pool,1 instant share
        		ThisUser.getInstance().set_Take_Offer_Type(offerRide.isChecked()?1:0);//0 share ,1 offer
        		
        		Log.i(TAG, "user destination set... querying server");
        		ProgressHandler.showInfiniteProgressDialoge(MapListActivityHandler.getInstance().getUnderlyingActivity(), "Fetching users", "Please wait..");
        		SBHttpRequest addThisUserSrcDstRequest;
        		if(dailyCarPool.isChecked())        		
        			addThisUserSrcDstRequest = new AddThisUserScrDstCarPoolRequest();        		
        		else
        			addThisUserSrcDstRequest = new AddThisUserSrcDstRequest();        		
                 
                SBHttpClient.getInstance().executeRequest(addThisUserSrcDstRequest);
                saveSearch();
                finish();				
			}
		});
        
        cancelFindUsers.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				finish();				
			}
		});
        
        source.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AddressAdapter.Address address = ((AddressAdapter) source.getAdapter()).getAddress(i);
                sourceAddress = address.getGeoAddress();
                if (!address.isSaved()) {
                    saveSuggestion(sourceAddress);
                }
                setSource(sourceAddress);
            }
        });
        source.setAdapter(new AddressAdapter(this, android.R.layout.select_dialog_item));

        destination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AddressAdapter.Address address= ((AddressAdapter) destination.getAdapter()).getAddress(i);
                destinationAddress = address.getGeoAddress();
                if (!address.isSaved()) {
                    saveSuggestion(destinationAddress);
                }
                setDestination(destinationAddress);
            }
        });
        destination.setAdapter(new AddressAdapter(this, android.R.layout.select_dialog_item));

       
        timeSeekbar = (SeekBar) findViewById(R.id.timeseekBar);
        timeSeekbar.setMax(48);
        timeSeekbar.setOnSeekBarChangeListener(this);
        
        //find current time and set seekbar to just after current
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR);        
        int min = now.get(Calendar.MINUTE);        
        if(hour == 12)
        	hour = 0;
        int progress  = hour*4 + (int)(min/15);
        if(now.get(Calendar.AM_PM) == 0)
        	am_pm_toggle.setChecked(true);
        else
        	am_pm_toggle.setChecked(false);
        
        timeSeekbar.setProgress((progress+1)%48);        
        
	}

	@Override
    public void onProgressChanged(SeekBar seekBar, int progress,
    		boolean fromUser) {		
		hour = progress/4;
		minutes = (progress%4)*15;
		
		
		if(hour == 0)
			hourStr = "12";
		else if (hour > 0 && hour < 10)
		    hourStr = "0" + Integer.toString(hour);
		else
			hourStr = Integer.toString(hour);
		
		if(minutes == 0)
			minstr = "00";	
		else
			minstr = Integer.toString(minutes);
		
		timeView.setText(hourStr+":"+minstr);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void setSource(GeoAddress geoAddress) {
		//track user only if real time req
		if(!dailyCarPool.isChecked())
			ThisUser.getInstance().setShareReqGeoPoint();
		
        int lat = (int) (geoAddress.getLatitude() * 1e6);
        int lon = (int) (geoAddress.getLongitude() * 1e6);
        String subLocality = geoAddress.getSubLocality();
        String address = geoAddress.getAddressLine();

        ThisUser.getInstance().setSourceGeoAddress(geoAddress);
        ThisUser.getInstance().setSourceGeoPoint(new SBGeoPoint(lat, lon, subLocality, address));
       
    }
	
	public void setDestination(GeoAddress geoAddress) {
        
        int lat = (int) (geoAddress.getLatitude() * 1e6);
        int lon = (int) (geoAddress.getLongitude() * 1e6);
        String subLocality = geoAddress.getSubLocality();
        String address = geoAddress.getAddressLine();

        ThisUser.getInstance().setDestinationGeoAddress(geoAddress);
        ThisUser.getInstance().setDestinationGeoPoint(new SBGeoPoint(lat, lon, subLocality, address));
        
    }

    private void saveSearch() {

        new Thread("saveSearch") {
            @Override
            public void run() {
                saveHistoryBlocking();
            }
        }.start();
    }

    private void saveHistoryBlocking() {
        ContentResolver cr = getContentResolver();
        long now = System.currentTimeMillis();

        // Use content resolver (not cursor) to insert/update this query
        try {
            ContentValues values = new ContentValues();
            ThisUser thisUser = ThisUser.getInstance();
            values.put(columns[0], thisUser.getSourceGeoAddress().getSubLocality());
            values.put(columns[1], thisUser.getDestinationGeoAddress().getSubLocality());
            values.put(columns[2], thisUser.getTimeOfRequest());
            values.put(columns[3], thisUser.get_Daily_Instant_Type());
            values.put(columns[4], thisUser.get_Take_Offer_Type());
            values.put(columns[5], now);
            cr.insert(mHistoryUri, values);
        } catch (RuntimeException e) {
            Log.e(TAG, "saveHistoryQuery", e);
        }

        // Shorten the list (if it has become too long)
        truncateHistory(cr, MAX_HISTORY_COUNT);
    }

    private void truncateHistory(ContentResolver cr, int maxEntries) {
        if (maxEntries < 0) {
            throw new IllegalArgumentException();
        }

        try {
            // null means "delete all".  otherwise "delete but leave n newest"
            String selection = null;
            if (maxEntries > 0) {
                selection = "_id IN " +
                        "(SELECT _id FROM history" +
                        " ORDER BY date DESC" +
                        " LIMIT -1 OFFSET " + String.valueOf(maxEntries) + ")";
            }
            cr.delete(mHistoryUri, selection, null);
        } catch (RuntimeException e) {
            Log.e(TAG, "truncateHistory", e);
        }
    }

}