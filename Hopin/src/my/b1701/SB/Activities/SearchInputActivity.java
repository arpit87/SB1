package my.b1701.SB.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import my.b1701.SB.Adapter.AddressAdapter;
import my.b1701.SB.R;
import my.b1701.SB.provider.CustomSuggestionProvider;
import my.b1701.SB.provider.GeoAddress;
import my.b1701.SB.provider.SearchRecentSuggestions;

import java.util.Calendar;

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

        source.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AddressAdapter.Address address = ((AddressAdapter) source.getAdapter()).getAddress(i);
                sourceAddress = address.getGeoAddress();
                if (!address.isSaved()) {
                    saveSuggestion(sourceAddress);
                }
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
            }
        });
        destination.setAdapter(new AddressAdapter(this, android.R.layout.select_dialog_item));

        am_pm_toggle = (ToggleButton) findViewById(R.id.btn_am_pm_toggle);
        timeView = (TextView) findViewById(R.id.time);
        timeSeekbar = (SeekBar) findViewById(R.id.timeseekBar);
        timeSeekbar.setMax(48);
        timeSeekbar.setOnSeekBarChangeListener(this);
        
        //find current time and set seekbar to half hour after current
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
        
        timeSeekbar.setProgress(progress+1);        
        
	}

	@Override
    public void onProgressChanged(SeekBar seekBar, int progress,
    		boolean fromUser) {		
		int hour = progress/4;
		int minutes = (progress%4)*15;
		String hourStr = "";
		String minstr = "";
		
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
	
}