package my.b1701.SB.ActivityHandlers;

import java.util.List;

import my.b1701.SB.LocationHelpers.SBGeoPoint;
import my.b1701.SB.LocationHelpers.SBLocation;
import my.b1701.SB.LocationHelpers.SBLocationManager;
import my.b1701.SB.MapHelpers.BaseItemizedOverlay;
import my.b1701.SB.MapHelpers.ItemizedOverlayFactory;
import my.b1701.SB.MapHelpers.NearbyUsersItemizedOverlayFactory;
import my.b1701.SB.MapHelpers.ThisUserItemizedOverlayFactory;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Users.NearbyUser;
import my.b1701.SB.Users.ThisUser;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
	private boolean updateMapRealTime = false;
	private ProgressDialog progressDialog;
	AlertDialog alertDialog ;	
	private boolean mapInitialized = false;

	
	public boolean isMapInitialized() {
		return mapInitialized;
	}


	public Activity getUnderlyingActivity() {
		return underlyingActivity;			
	}


	public void setUnderlyingActivity(Activity underlyingActivity) {
		this.underlyingActivity = underlyingActivity;
	}


	private MapActivityHandler(){super();}	
	
	
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
	
	public boolean isUpdateMapRealTime() {
		return updateMapRealTime;
	}


	public void setUpdateMapRealTime(boolean updateMapRealTime) {
		this.updateMapRealTime = updateMapRealTime;
	}


	public void handleMessage(Message msg) {
		super.handleMessage(msg);
			
		
	}
	
	public void handleOnClick(View v)
	{
		

	}
	
	public void initMyLocation() 
	{ 
		SBLocation currLoc = SBLocationManager.getInstance().getLastXMinBestLocation(5);
		if(currLoc == null)
		{
			//location not found yet after initial screen!try more for 3 secs
			progressDialog = ProgressDialog.show(underlyingActivity, "Problem fetching location", "Trying,please wait..", true);			
			 Runnable fetchLocation = new Runnable() {
			      public void run() {
			    	  SBLocation currLoc = SBLocationManager.getInstance().getLastXMinBestLocation(2);
			    	  progressDialog.dismiss();
			    	  if(currLoc != null)
			    	  {			    		  
			    		  ThisUser.getInstance().setLocation(currLoc);
			    		  putInitialOverlay();
			    	  }
			    	  else
			    	  {
			    		  alertDialog = new AlertDialog.Builder(underlyingActivity).create(); 
			    		  alertDialog.setTitle("Boo-hoo..");
			    		  alertDialog.setMessage("Some problem with fetching network location,please enter location yourself");
			    		  alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
			    	           public void onClick(DialogInterface dialog, int id) {
			    	                dialog.cancel();
			    	           }
			    	       });
			    		  alertDialog.show();
			    	  }
			      } 
			 };			        
			Platform.getInstance().getHandler().postDelayed(fetchLocation, 3000);// post after 3 secs
		}
		else
		{
			ThisUser.getInstance().setLocation(currLoc);		
			putInitialOverlay();		
		}
	}
	
	private void putInitialOverlay()
	{
		Log.i(TAG,"initializing this user location");
	    mapView.setBuiltInZoomControls(true);
	    mapcontroller = mapView.getController();
	    mapcontroller.setZoom(14);    
	    itemizedOverlayFactory = new ThisUserItemizedOverlayFactory(); 
	    Log.i(TAG,"setting myoverlay");        
	    thisUserOverlay = itemizedOverlayFactory.createItemizedOverlay(); 
	    //SBGeoPoint currGeo = ThisUser.getInstance().getCurrentGeoPoint();
	    //Log.i(TAG,"location is:"+currGeo.getLatitudeE6()+","+currGeo.getLongitudeE6());		
	    thisUserOverlay.addThisUser();	    
	    mapView.getOverlays().add(thisUserOverlay);
	    mapView.postInvalidate();	       
	    mapcontroller.animateTo(ThisUser.getInstance().getCurrentGeoPoint());
	    //onResume of mapactivity doesnt update user till its once initialized
	    mapInitialized = true;
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
		//be careful here..do we have location yet?
		Log.i(TAG,"update this user called");	
		if(thisUserOverlay != null)	
		{
			mapView.getOverlays().remove(thisUserOverlay);
		    thisUserOverlay.updateThisUser();
		    Log.i(TAG,"this user map overlay updated");	    
		    mapView.getOverlays().add(thisUserOverlay);
		    mapView.postInvalidate();	       
		    mapcontroller.animateTo(ThisUser.getInstance().getCurrentGeoPoint());
		}
		else
			Log.i(TAG,"but thisUSeroverlay empty!how?shldnt be..we initialixed it in init");
		
	}

	
}
