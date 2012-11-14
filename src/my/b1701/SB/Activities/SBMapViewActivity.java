package my.b1701.SB.Activities;

import my.b1701.SB.R;
import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.FacebookHelpers.FacebookConnector;
import my.b1701.SB.HelperClasses.Constants;
import my.b1701.SB.HelperClasses.ThisAppConfig;
import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.LocationHelpers.SBLocation;
import my.b1701.SB.LocationHelpers.SBLocationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class SBMapViewActivity extends MapActivity { 	
		
	private static final String TAG = "SBMapViewActivity";	
	private MapListActivityHandler mapListActivityHandler;
	private MapView mymapview;	
	private boolean mapInitialized = false;
	
		
	private SBLocation location;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view_old);
        /*serachBuddiesButton = (Button)findViewById(R.id.searchBuddiesButton);
        fromLocationView = (EditText)findViewById(R.id.fromLocation);
        desLocationView = (EditText)findViewById(R.id.desLocation);    */
        mymapview = (MapView)findViewById(R.id.mapview);
        Log.i(TAG,"initialize handler");
        MapListActivityHandler.getInstance().setMapView(mymapview); 
        MapListActivityHandler.getInstance().setUnderlyingActivity(this);
        Log.i(TAG,"initialize mylocation");
        ToastTracker.showToast("Updating location..",1);
        MapListActivityHandler.getInstance().initMyLocation();
    }     
   
    
    public void onClickMapViewButtons(View v) {
    	Log.i(TAG,"button clicked:"+v.getId());
        //mapActivityHandler.handleOnClick(v);    
    	switch (v.getId()) {
       // case R.id.searchBuddiesButton:
        	//getLocationListFromAddressString();
        	//updateNearbyBuddies();  
        	//break;
        case R.id.desLocation:
        	Log.i(TAG,"destination edit test clicked starting addres list activity");
    		Intent showAddressListActivity = new Intent(this, AddressListViewActivity.class);    		
    		startActivity(showAddressListActivity);
		
	}    
    }

	public void onResume(){
    	super.onResume();
    	//we update realtime when on map activity
    	SBLocationManager.getInstance().StartListeningtoNetwork(); 
    	MapListActivityHandler.getInstance().setUpdateMapRealTime(true);
    	if(MapListActivityHandler.getInstance().isMapInitialized())
    		MapListActivityHandler.getInstance().updateThisUserMapOverlay();
    }

    //test
	public void onPause(){
    	super.onPause();
    	MapListActivityHandler.getInstance().setUpdateMapRealTime(false);
    	SBLocationManager.getInstance().StopListeningtoGPS();    	
        SBLocationManager.getInstance().StopListeningtoNetwork();
    	//mymapview.getOverlays().clear();
    	//mymapview.postInvalidate();
    }


	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	// Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.mapactivity_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
 
        switch (item.getItemId())
        {
        case R.id.menu_fb_logout:
        	FacebookConnector fbconnect = new FacebookConnector(this, Constants.FB_PERMISSIONS);
        	fbconnect.logoutFromFB();
        }
		return true;
    }
	
   
}