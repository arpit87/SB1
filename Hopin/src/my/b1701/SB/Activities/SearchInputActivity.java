package my.b1701.SB.Activities;

import java.util.Calendar;

import my.b1701.SB.R;
import my.b1701.SB.Adapter.AddressAdapter;
import my.b1701.SB.HttpClient.AddThisUserScrDstCarPoolRequest;
import my.b1701.SB.HttpClient.AddThisUserSrcDstRequest;
import my.b1701.SB.HttpClient.SBHttpClient;
import my.b1701.SB.HttpClient.SBHttpRequest;
import my.b1701.SB.LocationHelpers.SBGeoPoint;
import my.b1701.SB.Users.ThisUser;
import my.b1701.SB.provider.CustomSuggestionProvider;
import my.b1701.SB.provider.GeoAddress;
import my.b1701.SB.provider.SearchRecentSuggestions;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SearchInputActivity extends Activity implements SeekBar.OnSeekBarChangeListener{
    private static final String TAG = "my.b1701.SB.Activities.SearchInputActivity";

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
    CheckBox offerRide;
    CheckBox dailyCarPool;
    
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
        offerRide = (CheckBox)findViewById(R.id.checkbox_rideoffer);
        dailyCarPool = (CheckBox)findViewById(R.id.checkbox_dailycarpool);
        am_pm_toggle = (ToggleButton) findViewById(R.id.btn_am_pm_toggle);
        timeView = (TextView) findViewById(R.id.time);
      
        //set source to our found location, if not found user can enter himself
        SBGeoPoint currGeopoint = ThisUser.getInstance().getSourceGeoPoint();
        if(currGeopoint!=null)
        {
        	String foundAddress = currGeopoint.getAddress();        
        	source.setText(foundAddress);
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
        		SBHttpRequest addThisUserSrcDstRequest;
        		if(dailyCarPool.isChecked())        		
        			addThisUserSrcDstRequest = new AddThisUserScrDstCarPoolRequest();        		
        		else
        			addThisUserSrcDstRequest = new AddThisUserSrcDstRequest();        		
                 
                SBHttpClient.getInstance().executeRequest(addThisUserSrcDstRequest);
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

        ThisUser.getInstance().setSourceGeoPoint(new SBGeoPoint(lat, lon, subLocality, address));
       
    }
	
	public void setDestination(GeoAddress geoAddress) {
        
        int lat = (int) (geoAddress.getLatitude() * 1e6);
        int lon = (int) (geoAddress.getLongitude() * 1e6);
        String subLocality = geoAddress.getSubLocality();
        String address = geoAddress.getAddressLine();

        ThisUser.getInstance().setDestinationGeoPoint(new SBGeoPoint(lat, lon, subLocality, address));
        
    }
	
}