package my.b1701.SB.ActivityHandlers;

import java.util.List;

import my.b1701.SB.R;
import my.b1701.SB.Activities.MapListViewTabActivity;
import my.b1701.SB.CustomViewsAndListeners.SBMapView;
import my.b1701.SB.Fragments.FBLoginDialogFragment;
import my.b1701.SB.Fragments.SBListFragment;
import my.b1701.SB.Fragments.SBMapFragment;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.LocationHelpers.SBGeoPoint;
import my.b1701.SB.LocationHelpers.SBLocation;
import my.b1701.SB.LocationHelpers.SBLocationManager;
import my.b1701.SB.MapHelpers.BaseItemizedOverlay;
import my.b1701.SB.MapHelpers.NearbyUsersItemizedOverlayFactory;
import my.b1701.SB.MapHelpers.ThisUserItemizedOverlayFactory;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Users.CurrentNearbyUsers;
import my.b1701.SB.Users.NearbyUser;
import my.b1701.SB.Users.ThisUser;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.TransitionDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;

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
	private boolean fbloginPromptIsShowing = false;
	PopupWindow fbPopupWindow = null;
	View fbloginlayout = null;
	ViewGroup popUpView = null;
	
			
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
			progressDialog = ProgressDialog.show(underlyingActivity, "Fetching location", "Trying,please wait..", true);			
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
	    
	    
	    if(CurrentNearbyUsers.getInstance().getAllNearbyUsers() !=null)
	    	updateNearbyUsers();
	}


	
	public void updateNearbyUsers() {
		
		List<NearbyUser> nearbyUsers = CurrentNearbyUsers.getInstance().getAllNearbyUsers();
		
		//update map view
		Log.i(TAG,"updating earby user");
		if(nearbyUserItemizedOverlay!=null)
		{
			Log.i(TAG,"removing prev nearby users overlay");
			nearbyUserItemizedOverlay.removeAllSmallViews();
			mapView.getOverlays().remove(nearbyUserItemizedOverlay);	
		}
		
		//null means 0 users returned by server or not yet single call to server
		if(nearbyUsers == null)
			return;			
		
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
		
		//show fb login popup at bottom if not yet logged in
		boolean isfbloggedin = ThisUserConfig.getInstance().getBool(ThisUserConfig.FBLOGGEDIN);
		if(!isfbloggedin)
		{
			fbloginpromptpopup_show(true);
		}	
		
	}
	
	public void fbloginpromptpopup_show(boolean show)
	{
		
		if(show )
		{
			if(!fbloginPromptIsShowing)
			{
				Log.i(TAG,"showing fblogin prompt");	
				popUpView = (ViewGroup) underlyingActivity.getLayoutInflater().inflate(R.layout.fbloginpromptpopup, null); 
				fbPopupWindow = new PopupWindow(popUpView,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT,false); //Creation of popup
				fbPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);   
				fbPopupWindow.showAtLocation(popUpView, Gravity.BOTTOM, 0, 0);    // Displaying popup
		        fbloginPromptIsShowing = true;		
		        fbPopupWindow.setTouchable(true);
		        fbPopupWindow.setFocusable(false);
		        //fbPopupWindow.setOutsideTouchable(true);
		        fbloginlayout = popUpView.findViewById(R.id.fbloginpromptloginlayout);
		        fbloginlayout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						FBLoginDialogFragment fblogin_dialog = new FBLoginDialogFragment();
						fblogin_dialog.show(underlyingActivity.getSupportFragmentManager(), "fblogin_dialog");
						fbPopupWindow.dismiss();
						fbloginPromptIsShowing = false;
						Log.i(TAG,"fblogin prompt clicked");
					}
				});
		        ImageView buttonClosefbprompt = (ImageView) popUpView.findViewById(R.id.fbloginpromptclose);
		        buttonClosefbprompt.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						fbPopupWindow.dismiss();
						fbloginPromptIsShowing = false;
					}
				});
			}
			else
			{
				//will flicker prompt here if already showing
				TransitionDrawable transition = (TransitionDrawable) popUpView.getBackground();
				transition.startTransition(300);				
				transition.reverseTransition(300);				
			}
			//popUpView.setBackgroundResource(R.drawable.transparent_black);
		}
		if(!show)
		{
			if(fbloginPromptIsShowing && fbPopupWindow!=null)
				fbPopupWindow.dismiss();
			fbloginPromptIsShowing = false;
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
