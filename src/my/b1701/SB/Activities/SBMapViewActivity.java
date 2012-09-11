package my.b1701.SB.Activities;

import my.b1701.SB.R;
import my.b1701.SB.ActivityHandlers.MapActivityHandler;
import my.b1701.SB.LocationHelpers.SBLocation;
import my.b1701.SB.LocationHelpers.SBLocationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class SBMapViewActivity extends MapActivity { 	
		
	private static final String TAG = "SBMapViewActivity";	
	private MapActivityHandler mapActivityHandler;
	private MapView mymapview;	
	
		
	private SBLocation location;

	

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);
        /*serachBuddiesButton = (Button)findViewById(R.id.searchBuddiesButton);
        fromLocationView = (EditText)findViewById(R.id.fromLocation);
        desLocationView = (EditText)findViewById(R.id.desLocation);    */
        mymapview = (MapView)findViewById(R.id.mapview);
        Log.i(TAG,"initialize handler");
        MapActivityHandler.getInstance().setMapView(mymapview); 
        MapActivityHandler.getInstance().setUnderlyingActivity(this);
        Log.i(TAG,"initialize mylocation");
        MapActivityHandler.getInstance().initMyLocation();
    }     
   
    
    public void onClickMapViewButtons(View v) {
    	Log.i(TAG,"button clicked:"+v.getId());
        //mapActivityHandler.handleOnClick(v);    
    	switch (v.getId()) {
        case R.id.searchBuddiesButton:
        	//getLocationListFromAddressString();
        	//updateNearbyBuddies();  
        	break;
        case R.id.desLocation:
        	Log.i(TAG,"destination edit test clicked starting addres list activity");
    		Intent showAddressListActivity = new Intent(this, AddressListViewActivity.class);    		
    		startActivity(showAddressListActivity);
		
	}    
    }

	public void onResume(){
    	super.onResume();
    }
	
	
   
    

    //test
	public void onPause(){
    	super.onPause();    	
    	SBLocationManager.getInstance().StartListeningtoNetwork();
    	SBLocationManager.getInstance().StartListeningtoGPS();
    	mymapview.getOverlays().clear();
    	mymapview.postInvalidate();
    }


	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
    
	
	
   
}