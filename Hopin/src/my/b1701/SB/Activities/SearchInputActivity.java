package my.b1701.SB.Activities;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.google.analytics.tracking.android.EasyTracker;
import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.Adapter.AddressAdapter;
import my.b1701.SB.Adapter.HistoryAdapter;
import my.b1701.SB.HelperClasses.ProgressHandler;
import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.HttpClient.AddThisUserScrDstCarPoolRequest;
import my.b1701.SB.HttpClient.AddThisUserSrcDstRequest;
import my.b1701.SB.HttpClient.SBHttpClient;
import my.b1701.SB.HttpClient.SBHttpRequest;
import my.b1701.SB.LocationHelpers.SBGeoPoint;
import my.b1701.SB.R;
import my.b1701.SB.Users.ThisUser;
import my.b1701.SB.Util.StringUtils;
import my.b1701.SB.provider.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
            "freq",
            "reqDate",
            "srclati",
            "srclongi",
            "dstlati",
            "dstlongi"};

    SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(this, CustomSuggestionProvider.AUTHORITY, CustomSuggestionProvider.MODE);

    AutoCompleteTextView source;
    AutoCompleteTextView destination;
	ToggleButton am_pm_toggle;
	TextView timeView;
	SeekBar timeSeekbar;	
    GeoAddress sourceAddress;
    GeoAddress destinationAddress;
    Button cancelFindUsers;
    Button takeRideButton;
    Button offerRideButton;
    ToggleButton daily_insta_toggle;
    RadioGroup radio_group;
   
    //these change as user chooses time
    String hourStr = "";
	String minstr = "";	
	int hour;
	int minutes;
	String mDateProgress = "Today";
	
	boolean dailyCarPool = false;
	boolean takeRide = false;
	boolean destinationSet = false;

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
        takeRideButton = (Button)findViewById(R.id.btn_takeride);
        offerRideButton = (Button)findViewById(R.id.btn_offerride);
        cancelFindUsers = (Button)findViewById(R.id.btn_cancelfindusers);
        radio_group = (RadioGroup)findViewById(R.id.search_input_radio_group);
        daily_insta_toggle = (ToggleButton)findViewById(R.id.daily_intsa_toggle_btn);
        am_pm_toggle = (ToggleButton) findViewById(R.id.btn_am_pm_toggle);
        timeView = (TextView) findViewById(R.id.time);        
        dailyCarPool = daily_insta_toggle.isChecked();
        radio_group.check(R.id.radiobutton_today);
        Intent i = getIntent();
        if(i.hasExtra("source"))
        {
        	//though setting this but we are not parsing this text to get latitude longi
        	//it should be set before firing this intent from whereever
        	Bundle b = i.getExtras();
        	String sourceFromIntent = i.getStringExtra("source");
        	source.setText(sourceFromIntent);
        	MapListActivityHandler.getInstance().setUpdateMap(false);
        }else
        {       
	        //set source to our found location, if not found user can enter himself
	        SBGeoPoint currGeopoint = ThisUser.getInstance().getCurrentGeoPoint();
	        if(currGeopoint!=null)
	        {
	        	//String foundAddress = currGeopoint.getAddress();        
	        	source.setHint("My Location");
	        	MapListActivityHandler.getInstance().setUpdateMap(true);
	            ThisUser.getInstance().setSourceGeoPoint(currGeopoint,true);            
	        }
        }
        
        if(i.hasExtra("destination"))
        {
        	//though setting this but we are not parsing this text to get latitude longi
        	//it should be set before firing this intent from whereever
        	Bundle b = i.getExtras();
        	String destinationFromIntent = i.getStringExtra("destination");
        	destination.setText(destinationFromIntent);    
        	destinationSet = true;
        }
        
        daily_insta_toggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EasyTracker.getTracker().sendEvent("ui_action", "toggle_press", "daily_instant_toggle", 1L);
				if(isChecked)
				{
					radio_group.setEnabled(false);
					dailyCarPool = true;
				}
				else
				{
					radio_group.setEnabled(true);
					dailyCarPool = false;
				}
				
			}
		});
        
        takeRideButton.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View v) {
                EasyTracker.getTracker().sendEvent("ui_action", "button_press", "takeRide_button", 1L);
				if(!destinationSet)
				{
					ToastTracker.showToast("Please set destination");
					return;
				}
        		takeRide = true;
        		findUsers();
        		MapListActivityHandler.getInstance().updateSrcDstTimeInListView();
				finish();
			}
		});
        
        offerRideButton.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View v) {
                EasyTracker.getTracker().sendEvent("ui_action", "button_press", "offerRide_button", 1L);
				if(!destinationSet)
				{
					ToastTracker.showToast("Please set destination");
					return;
				}
				takeRide = false;				
        		findUsers();
        		MapListActivityHandler.getInstance().updateSrcDstTimeInListView();
				finish();
			}
		});
        
        cancelFindUsers.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
                EasyTracker.getTracker().sendEvent("ui_action", "button_press", "cancelFindUsers_button", 1L);
				finish();		
			}
		});
        
        source.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EasyTracker.getTracker().sendEvent("ui_action", "autocomplete_text", "setSource", 1L);
                AddressAdapter.Address address = ((AddressAdapter) source.getAdapter()).getAddress(i);
                sourceAddress = address.getGeoAddress();
                if (!address.isSaved()) {
                    saveSuggestion(sourceAddress);
                }
                setSource(sourceAddress);
                //if user put some other location then we stop updating map
                //though we keep listening to network listener when on mapview
                //this helps in putting my location in search source
                MapListActivityHandler.getInstance().updateThisUserMapOverlay();
                MapListActivityHandler.getInstance().setUpdateMap(false);
                hideSoftKeyboard();
                source.setSelection(0);
                source.clearFocus();
            }
        });
        source.setAdapter(new AddressAdapter(this, R.layout.address_suggestion_layout));

        destination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EasyTracker.getTracker().sendEvent("ui_action", "autocomplete_text", "setDestination", 1L);
                AddressAdapter.Address address= ((AddressAdapter) destination.getAdapter()).getAddress(i);
                destinationAddress = address.getGeoAddress();
                if (!address.isSaved()) {
                    saveSuggestion(destinationAddress);
                }
                setDestination(destinationAddress);
                hideSoftKeyboard();
                destination.setSelection(0);
                destination.clearFocus();
                destinationSet = true;
            }
        });
        destination.setAdapter(new AddressAdapter(this, R.layout.address_suggestion_layout));

        
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
    public void onStart(){
        super.onStart();
        EasyTracker.getInstance().activityStart(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        EasyTracker.getInstance().activityStop(this);
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
	
	public void findUsers()
	{
		String time24HrFormat ;
		if(am_pm_toggle.isChecked())
			time24HrFormat = Integer.toString(hour) +":" + Integer.toString(minutes);
		else 
			time24HrFormat = Integer.toString(hour+12) + ":" + Integer.toString(minutes);
		
		dailyCarPool = daily_insta_toggle.isChecked(); 
		ThisUser.getInstance().setTimeOfRequest(time24HrFormat);
		ThisUser.getInstance().setDateOfRequest(getDate());
		ThisUser.getInstance().set_Daily_Instant_Type(dailyCarPool?0:1);//0 daily pool,1 instant share
		ThisUser.getInstance().set_Take_Offer_Type(takeRide?0:1);//0 take ,1 offer
		
		Log.i(TAG, "user destination set... querying server");
		ProgressHandler.showInfiniteProgressDialoge(MapListActivityHandler.getInstance().getUnderlyingActivity(), "Fetching users", "Please wait..");
		SBHttpRequest addThisUserSrcDstRequest;
		if(dailyCarPool)        		
			addThisUserSrcDstRequest = new AddThisUserScrDstCarPoolRequest();        		
		else
			addThisUserSrcDstRequest = new AddThisUserSrcDstRequest();        		
         
        SBHttpClient.getInstance().executeRequest(addThisUserSrcDstRequest);
        saveSearch();
        
        //moveTaskToBack(true);			
	}
	
	
	public void setSource(GeoAddress geoAddress) {
		//track user only if real time req
				
        int lat = (int) (geoAddress.getLatitude() * 1e6);
        int lon = (int) (geoAddress.getLongitude() * 1e6);
        String subLocality = geoAddress.getSubLocality();
        String address = geoAddress.getAddressLine();

        ThisUser.getInstance().setSourceGeoAddress(geoAddress);
        ThisUser.getInstance().setSourceGeoPoint(new SBGeoPoint(lat, lon, subLocality, address), false);
       
    }
	
	public void setDestination(GeoAddress geoAddress) {
        
        int lat = (int) (geoAddress.getLatitude() * 1e6);
        int lon = (int) (geoAddress.getLongitude() * 1e6);
        String subLocality = geoAddress.getSubLocality();
        String address = geoAddress.getAddressLine();

        ThisUser.getInstance().setDestinationGeoAddress(geoAddress);
        ThisUser.getInstance().setDestinationGeoPoint(new SBGeoPoint(lat, lon, subLocality, address), false);
        
    }
	
	public String getDate()
	{	
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();  
		Calendar cal = Calendar.getInstance();  
		cal.setTime(now);  
		Date travelDate = cal.getTime();
		int dateProgress = 0;
		if(!dailyCarPool)
		{
			int radio_button_id = radio_group.getCheckedRadioButtonId();
			switch(radio_button_id)
			{
				case R.id.radiobutton_t1:
					dateProgress = 1;
					break;
				case R.id.radiobutton_t2:
					dateProgress = 2;
					break;
				case R.id.radiobutton_t3:
					dateProgress = 3;
					break;
			}
			if(dateProgress>0)
			{
				cal.add(Calendar.DATE, dateProgress);   
				travelDate = cal.getTime();
				mDateProgress = "T+"+dateProgress;
				ToastTracker.showToast("T+"+dateProgress +" request");	
			}
			
		}
		else
		{
			mDateProgress = "Daily";
			ToastTracker.showToast("Daily car pool request");
		}
		String date = dateFormat.format(travelDate);
		return date;
	}

	public void hideSoftKeyboard() {
	    InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minutes);
        if(am_pm_toggle.isChecked())
            calendar.set(Calendar.AM_PM, Calendar.AM);
        else
            calendar.set(Calendar.AM_PM, Calendar.PM);

        ContentResolver cr = getContentResolver();
        String time12Hr = dateFormat.format(calendar.getTime());
        ThisUser thisUser = ThisUser.getInstance();

        // Use content resolver (not cursor) to insert/update this query
        try {
            ContentValues values = new ContentValues();
            values.put(columns[0], thisUser.getSourceGeoAddress().getAddressLine());
            values.put(columns[1], thisUser.getDestinationGeoAddress().getAddressLine());
            values.put(columns[2], time12Hr );
            values.put(columns[3], thisUser.get_Daily_Instant_Type());
            values.put(columns[4], thisUser.get_Take_Offer_Type());
            values.put(columns[5], mDateProgress);
            values.put(columns[6], StringUtils.gettodayDateInFormat("d MMM"));
            values.put(columns[7], thisUser.getSourceGeoPoint().getLatitudeE6());
            values.put(columns[8], thisUser.getSourceGeoPoint().getLongitudeE6());
            values.put(columns[9], thisUser.getDestinationGeoPoint().getLatitudeE6());
            values.put(columns[10], thisUser.getDestinationGeoPoint().getLongitudeE6());
            cr.insert(mHistoryUri, values);
        } catch (RuntimeException e) {
            Log.e(TAG, "saveHistoryQueryerror", e);
        }

        // Shorten the list (if it has become too long)
        truncateHistory(cr, MAX_HISTORY_COUNT);

        //update the inmemory cache
        HistoryAdapter.HistoryItem historyItem = new HistoryAdapter.HistoryItem(thisUser.getSourceGeoAddress().getAddressLine(),
                thisUser.getDestinationGeoAddress().getAddressLine(),
                time12Hr,
                thisUser.get_Daily_Instant_Type(),
                thisUser.get_Take_Offer_Type(),
                mDateProgress,
                StringUtils.gettodayDateInFormat("d MMM"),
                thisUser.getSourceGeoPoint().getLatitudeE6(),
                thisUser.getSourceGeoPoint().getLongitudeE6(),
                thisUser.getDestinationGeoPoint().getLatitudeE6(),
                thisUser.getDestinationGeoPoint().getLongitudeE6());

        addHistoryToMemory(historyItem);
        
    }

    private void addHistoryToMemory(HistoryAdapter.HistoryItem historyItem) {
        List<HistoryAdapter.HistoryItem> historyItemList = ThisUser.getInstance().getHistoryItemList();
        if (historyItemList != null) {
            historyItemList.add(0, historyItem);
            if (historyItemList.size() > MAX_HISTORY_COUNT) {
                for (int i = historyItemList.size(); i >= MAX_HISTORY_COUNT; i--) {
                    historyItemList.remove(i);
                }
            }
        }
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
                       // " ORDER BY date DESC" +
                        " LIMIT -1 OFFSET " + String.valueOf(maxEntries) + ")";
            }
            cr.delete(mHistoryUri, selection, null);
        } catch (RuntimeException e) {
            Log.e(TAG, "truncateHistory", e);
        }
    }

}