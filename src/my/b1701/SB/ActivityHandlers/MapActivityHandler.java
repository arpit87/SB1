package my.b1701.SB.ActivityHandlers;

import java.util.List;

import my.b1701.SB.LocationHelpers.SBGeoPoint;
import my.b1701.SB.LocationHelpers.SBLocation;
import my.b1701.SB.LocationHelpers.SBLocationManager;
import my.b1701.SB.MapHelpers.BaseItemizedOverlay;
import my.b1701.SB.MapHelpers.ItemizedOverlayFactory;
import my.b1701.SB.MapHelpers.NearbyUsersItemizedOverlayFactory;
import my.b1701.SB.MapHelpers.ThisUserItemizedOverlayFactory;
import my.b1701.SB.Users.NearbyUser;
import my.b1701.SB.Users.ThisUser;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.google.android.maps.MapController;
import com.google.android.maps.MapView;


public class MapActivityHandler extends Handler {
	
	MapView mapView;
	private static final String TAG = "MapActivityHandler";
	private static MapActivityHandler instance=new MapActivityHandler();
	private Activity underlyingActivity;
	private ItemizedOverlayFactory itemizedOverlayFactory;
	private BaseItemizedOverlay nearbyUserItemizedOverlay;
	private MapController mapcontroller;	
	private BaseItemizedOverlay thisUserOverlay;

	
	public Activity getUnderlyingActivity() {
		return underlyingActivity;			
	}


	public void setUnderlyingActivity(Activity underlyingActivity) {
		this.underlyingActivity = underlyingActivity;
	}


	private MapActivityHandler(){}	
	
	
	public static MapActivityHandler getInstance()
	{		
		return instance;
		
	}

	public MapView getMapView() {
		return mapView;
	}

	public void setMapView(MapView mapView) {
		this.mapView = mapView;
	}		
	
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
			
		
	}
	
	public void handleOnClick(View v)
	{
		

	}
	
	public void initMyLocation() 
	{    	
		
		Log.i(TAG,"initializing this user location");
	    mapView.setBuiltInZoomControls(true);
	    mapcontroller = mapView.getController();
	    mapcontroller.setZoom(14);    
	    itemizedOverlayFactory = new ThisUserItemizedOverlayFactory(); 
	    Log.i(TAG,"setting myoverlay");        
	    thisUserOverlay = itemizedOverlayFactory.createItemizedOverlay();
	    SBGeoPoint currGeo = ThisUser.getInstance().getCurrentGeoPoint();
		if(currGeo == null)
		{
			SBLocation currLoc = null;
			while(currLoc == null)
				currLoc = SBLocationManager.getInstance().getLastBestLocation();
			currGeo =new SBGeoPoint(currLoc);
			Log.i(TAG,"location is:"+currGeo.getLatitudeE6()+","+currGeo.getLongitudeE6());
			ThisUser.getInstance().setCurrentGeoPoint(currGeo);
		}
	    thisUserOverlay.addThisUser();	    
	    mapView.getOverlays().add(thisUserOverlay);
	    mapView.postInvalidate();	       
	    mapcontroller.animateTo(ThisUser.getInstance().getCurrentGeoPoint());
	}


	public void updateNearbyUsers(List<NearbyUser> nearbyUsers) {
		Log.i(TAG,"updating earby user");
		if(nearbyUserItemizedOverlay!=null)
		{
			Log.i(TAG,"removing prev nearby users overlay");
			mapView.getOverlays().remove(nearbyUserItemizedOverlay);	
		}
		itemizedOverlayFactory = new NearbyUsersItemizedOverlayFactory();
		nearbyUserItemizedOverlay = itemizedOverlayFactory.createItemizedOverlay();
		nearbyUserItemizedOverlay.addList(nearbyUsers);
		Log.i(TAG,"adding nearby useroverlay");		
		mapView.getOverlays().add(nearbyUserItemizedOverlay);	
		mapView.postInvalidate();
		
	}
	
	public void updateThisUserMapOverlay()
	{		
	    thisUserOverlay.updateThisUser();
	    mapView.postInvalidate();	       
	    mapcontroller.animateTo(ThisUser.getInstance().getCurrentGeoPoint());
		
	}

	
}
