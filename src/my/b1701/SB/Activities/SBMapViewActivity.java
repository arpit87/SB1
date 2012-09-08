package my.b1701.SB.Activities;

import my.b1701.SB.R;
import my.b1701.SB.ActivityHandlers.MapActivityHandler;
import my.b1701.SB.MapHelpers.BaseItemizedOverlay;
import my.b1701.SB.MapHelpers.BaseOverlayItem;
import my.b1701.SB.MapHelpers.ItemizedOverlayFactory;
import my.b1701.SB.MapHelpers.ThisUserItemizedOverlayFactory;
import my.b1701.SB.MapHelpers.ThisUserOverlayItem;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Users.ThisUser;
import LocationHelpers.SBLocation;
import LocationHelpers.SBLocationManager;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class SBMapViewActivity extends MapActivity { 	
		
	private static final String TAG = "SBMapViewActivity";	
	private MapActivityHandler mapActivityHandler;
	private MapView mymapview;	
	private BaseItemizedOverlay allUsersItemizedOverlay;
	private BaseItemizedOverlay thisUserOverlay;
	private ItemizedOverlayFactory itemizedOverlayFactory;
	private MapController myMapcontroller;	
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
        initMyLocation();
    }  
   
    
    private void initMyLocation() {
    	
    	location = SBLocationManager.getInstance().getLastBestLocation();

    	if(location!=null)
        {
    		Toast.makeText(getApplicationContext(), "Lati:"+location.getLatitude(), Toast.LENGTH_SHORT).show();
    		Toast.makeText(getApplicationContext(), "Longi:"+location.getLongitude(), Toast.LENGTH_SHORT).show();
        }
        else
        {
        	Toast.makeText(getApplicationContext(), "init location null,using ", Toast.LENGTH_SHORT).show();
        	//location = ThisUser.getInstance().getCurrentLocation();
        }    	
    	Log.i(TAG,"location is:"+location.getLatitude()+","+location.getLongitude());
        mymapview.setBuiltInZoomControls(true);
        myMapcontroller = mymapview.getController();
        myMapcontroller.setZoom(14);    
        itemizedOverlayFactory = new ThisUserItemizedOverlayFactory(); 
        Log.i(TAG,"setting myoverlay");        
        thisUserOverlay = itemizedOverlayFactory.createItemizedOverlay();
        thisUserOverlay.addThisUser();
        mymapview.getOverlays().add(thisUserOverlay);
        mymapview.postInvalidate();	
       
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
    	//locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    	//myOverlay.disableMyLocation();
    	//myOverlay.disableCompass();
    	//locationmanager.removeUpdates(this);
    	mymapview.getOverlays().remove(allUsersItemizedOverlay);
    	mymapview.postInvalidate();
    }


	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
    
	
	
   
}