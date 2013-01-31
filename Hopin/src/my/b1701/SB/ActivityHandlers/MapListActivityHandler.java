package my.b1701.SB.ActivityHandlers;

import java.util.List;

import my.b1701.SB.Activities.MapListViewTabActivity;
import my.b1701.SB.CustomViewsAndListeners.SBMapView;
import my.b1701.SB.Fragments.SBListFragment;
import my.b1701.SB.Fragments.SBMapFragment;
import my.b1701.SB.HttpClient.AddThisUserSrcDstRequest;
import my.b1701.SB.HttpClient.SBHttpClient;
import my.b1701.SB.HttpClient.SBHttpRequest;
import my.b1701.SB.LocationHelpers.SBGeoPoint;
import my.b1701.SB.LocationHelpers.SBLocation;
import my.b1701.SB.LocationHelpers.SBLocationManager;
import my.b1701.SB.MapHelpers.BaseItemizedOverlay;
import my.b1701.SB.MapHelpers.NearbyUsersItemizedOverlayFactory;
import my.b1701.SB.MapHelpers.ThisUserItemizedOverlayFactory;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Users.NearbyUser;
import my.b1701.SB.Users.ThisUser;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;

import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class MapListActivityHandler  {
	
	SBMapView mapView;	
	private static final String TAG = "my.b1701.SB.ActivityHandlers.MapActivityHandler";
	private static MapListActivityHandler instance=new MapListActivityHandler();
	private MapListViewTabActivity underlyingActivity;
	private NearbyUsersItemizedOverlayFactory nearbyUsersItemizedOverlayFactory = new NearbyUsersItemizedOverlayFactory();
	private ThisUserItemizedOverlayFactory thisUserItemizedOverlayFactory = new ThisUserItemizedOverlayFactory();
	private BaseItemizedOverlay nearbyUserItemizedOverlay;
	private MapController mapcontroller;	
	private BaseItemizedOverlay thisUserOverlay;
	private boolean updateMapRealTime = false;
	private ProgressDialog progressDialog;
	AlertDialog alertDialog ;	
	private boolean mapInitialized = false;
	private SBMapFragment mapFrag;
	private SBListFragment listFrag;
	private List<NearbyUser> nearbyUserList = null;
	
	public List<NearbyUser> getNearbyUserList() {
		return nearbyUserList;		
	}

		
	public BaseItemizedOverlay getNearbyUserItemizedOverlay() {
		return nearbyUserItemizedOverlay;
	}


	public boolean isMapInitialized() {
		return mapInitialized;
	}


	public MapListViewTabActivity getUnderlyingActivity() {
		return underlyingActivity;			
	}

	public void setUnderlyingActivity(MapListViewTabActivity underlyingActivity) {
		this.underlyingActivity = underlyingActivity;
	}


	private MapListActivityHandler(){super();}	
	
	
	public static MapListActivityHandler getInstance()
	{		
		return instance;
		
	}

	public MapView getMapView() {
		return mapView;
	}

	public void setMapView(SBMapView mapView) {
		this.mapView = mapView;
	}		
	
	public boolean isUpdateMapRealTime() {
		return updateMapRealTime;
	}


	public void setUpdateMapRealTime(boolean updateMapRealTime) {
		this.updateMapRealTime = updateMapRealTime;
	}
		
	public void initMyLocation() 
	{ 
		
		SBLocation currLoc = ThisUser.getInstance().getLocation();
		if(currLoc == null)
		{
			//location not found yet after initial screen!try more for 6 secs
			progressDialog = ProgressDialog.show(underlyingActivity, "Problem fetching location", "Trying,please wait..", true);			
			 Runnable fetchLocation = new Runnable() {
			      public void run() {
			    	  SBLocation currLoc = SBLocationManager.getInstance().getLastXSecBestLocation(6*60);
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
			Platform.getInstance().getHandler().postDelayed(fetchLocation, 6000);// post after 6 secs
		}
		else
		{					
			putInitialOverlay();		
		}
	}
	
	public void myLocationButtonClick()
	{
		if(!mapInitialized)
		{
			initMyLocation();	
			return;
		}
		
		int startInterval = 60;
		SBLocation thisCurrLoc = SBLocationManager.getInstance().getLastXSecBestLocation(startInterval);
		if(thisCurrLoc == null)
		{
			progressDialog = ProgressDialog.show(underlyingActivity, "Fetching location", "Please wait..", true);
			for(int attempt = 1 ; attempt <= 4; attempt++ )
			{
				//thisCurrLoc = SBLocationManager.getInstance().getCurrentBestLocation(location)
				thisCurrLoc = SBLocationManager.getInstance().getLastXSecBestLocation(startInterval*attempt);
				if(thisCurrLoc != null)
				{
					progressDialog.dismiss();
					ThisUser.getInstance().setLocation(thisCurrLoc);
					updateThisUserMapOverlay();
					centreMapTo(ThisUser.getInstance().getSourceGeoPoint());
					return;
				}
			}
			progressDialog.dismiss();
	}
		centreMapTo(ThisUser.getInstance().getSourceGeoPoint());
	}
		
public void centreMapTo(SBGeoPoint centrePoint)
{
	if(centrePoint !=null)
		mapcontroller.animateTo(centrePoint);
}
	
	
	
	private void putInitialOverlay()
	{
		Log.i(TAG,"initializing this user location");
	    mapView.setBuiltInZoomControls(true);
	    mapcontroller = mapView.getController();
	    mapcontroller.setZoom(14);
	    Log.i(TAG,"setting myoverlay");        
	    thisUserOverlay = thisUserItemizedOverlayFactory.createItemizedOverlay(mapView); 
	    //SBGeoPoint currGeo = ThisUser.getInstance().getCurrentGeoPoint();
	    //Log.i(TAG,"location is:"+currGeo.getLatitudeE6()+","+currGeo.getLongitudeE6());		
	    thisUserOverlay.addThisUser();	    
	    mapView.getOverlays().add(thisUserOverlay);
	    mapView.postInvalidate();	       
	    mapcontroller.animateTo(ThisUser.getInstance().getSourceGeoPoint());
	    //onResume of mapactivity doesnt update user till its once initialized
	    mapInitialized = true;
	    
	    if(nearbyUserList !=null)
	    	updateNearbyUsers(nearbyUserList);
	}


	
	public void updateNearbyUsers(List<NearbyUser> nearbyUsers) {
		
		if(nearbyUsers.size()==0)
			return;
		
		this.nearbyUserList = nearbyUsers;
		
		//update map view
		Log.i(TAG,"updating earby user");
		if(nearbyUserItemizedOverlay!=null)
		{
			Log.i(TAG,"removing prev nearby users overlay");
			nearbyUserItemizedOverlay.removeAllSmallViews();
			mapView.getOverlays().remove(nearbyUserItemizedOverlay);	
		}
		
		nearbyUserItemizedOverlay = nearbyUsersItemizedOverlayFactory.createItemizedOverlay(mapView);
		nearbyUserItemizedOverlay.addList(nearbyUsers);
		Log.i(TAG,"adding nearby useroverlay");		
		mapView.getOverlays().add(nearbyUserItemizedOverlay);	
		mapView.postInvalidate();
		
		//update listview
		NearbyUsersListViewAdapter adapter = new NearbyUsersListViewAdapter(underlyingActivity, nearbyUsers);
		if(listFrag == null)
			listFrag = (SBListFragment)underlyingActivity.getListFrag();
		if(listFrag != null)
		{
		listFrag.setListAdapter(adapter);
		adapter.notifyDataSetChanged();	
		}
		
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
		    mapcontroller.animateTo(ThisUser.getInstance().getSourceGeoPoint());
		}
		else
			Log.i(TAG,"but thisUSeroverlay empty!how?shldnt be..we initialixed it in init");
	}	
	
		
}
