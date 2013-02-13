package my.b1701.SB.ActivityHandlers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

import my.b1701.SB.R;
import my.b1701.SB.Activities.MapListViewTabActivity;
import my.b1701.SB.CustomViewsAndListeners.SBMapView;
import my.b1701.SB.Fragments.FBLoginDialogFragment;
import my.b1701.SB.Fragments.SBListFragment;
import my.b1701.SB.Fragments.SBMapFragment;
import my.b1701.SB.HelperClasses.ProgressHandler;
import my.b1701.SB.HelperClasses.SBImageLoader;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.LocationHelpers.SBGeoPoint;
import my.b1701.SB.LocationHelpers.SBLocation;
import my.b1701.SB.LocationHelpers.SBLocationManager;
import my.b1701.SB.MapHelpers.BaseItemizedOverlay;
import my.b1701.SB.MapHelpers.NearbyUsersItemizedOverlay;
import my.b1701.SB.MapHelpers.ThisUserItemizedOverlay;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Server.GetMatchingNearbyUsersResponse;
import my.b1701.SB.Server.ServerConstants;
import my.b1701.SB.Users.CurrentNearbyUsers;
import my.b1701.SB.Users.NearbyUser;
import my.b1701.SB.Users.ThisUser;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;

public class MapListActivityHandler  extends BroadcastReceiver{
	
	SBMapView mapView;	
	private static final String TAG = "my.b1701.SB.ActivityHandlers.MapActivityHandler";
	private static MapListActivityHandler instance=new MapListActivityHandler();
	private MapListViewTabActivity underlyingActivity;	
	private BaseItemizedOverlay nearbyUserItemizedOverlay;
	private MapController mapcontroller;	
	private BaseItemizedOverlay thisUserOverlay;
	private boolean updateMap = false;
	private ProgressDialog progressDialog;
	AlertDialog alertDialog ;	
	private boolean mapInitialized = false;
	private SBMapFragment mapFrag;
	private SBListFragment listFrag;
	private boolean fbloginPromptIsShowing = false;
	PopupWindow fbPopupWindow = null;
	View fbloginlayout = null;
	ViewGroup popUpView = null;
	ViewGroup mListViewContainer;	
	ImageView mListImageView;
	private TextView mDestination;
	private TextView mSource;
	private TextView mUserName;
	private TextView mtime;
	
	
			
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

	public void setListFrag(SBListFragment listFrag) {
		this.listFrag = listFrag;
	}
	
	public void setMapFrag(SBMapFragment mapFrag) {
		this.mapFrag = mapFrag;
	}

	private MapListActivityHandler(){super();}	
	
	
	public static MapListActivityHandler getInstance()
	{		
		return instance;
		
	}

	public SBMapView getMapView() {
		return mapView;
	}

	public void setMapView(SBMapView mapView) {
		this.mapView = mapView;
	}	
	
	
	public boolean isUpdateMap() {
		return updateMap;
	}


	public void setUpdateMap(boolean updateMapRealTime) {
		this.updateMap = updateMapRealTime;
	}
		
	public void initMyLocation() 
	{ 
		
		SBLocation currLoc = ThisUser.getInstance().getCurrentLocation();
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
			    		  ThisUser.getInstance().setCurrentLocation(currLoc);
			    		  putInitialOverlay();
			    	  }
			    	  else
			    	  {
			    		  alertDialog = new AlertDialog.Builder(underlyingActivity).create(); 
			    		  alertDialog.setTitle("Boo-hoo..");
			    		  alertDialog.setMessage("Some problem with fetching network location,please enter source location yourself in user search");
			    		  alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
			    	           public void onClick(DialogInterface dialog, int id) {
			    	                dialog.cancel();
			    	           }
			    	       });
			    		  alertDialog.show();
			    	  }
			      } 
			 };			        
			Platform.getInstance().getHandler().postDelayed(fetchLocation, 4000);// post after 6 secs
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
		
		//if we have source which might be different from current then zoom in to source
		SBGeoPoint sourceGeoPoint = ThisUser.getInstance().getSourceGeoPoint();
		if(sourceGeoPoint!=null)
		{
			centreMapTo(sourceGeoPoint);
			return;
		}
		//else to current
		int startInterval = 300;
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
					break;
				}
			}
			progressDialog.dismiss();
		}
		ThisUser.getInstance().setCurrentLocation(thisCurrLoc);
		ThisUser.getInstance().setSourceLocation(thisCurrLoc);
		updateThisUserMapOverlay();
	}
		
public void centreMapTo(SBGeoPoint centrePoint)
{
	if(centrePoint !=null)
		mapcontroller.animateTo(centrePoint);
}

public void centreMapToPlusLilUp(SBGeoPoint centrePoint)
{
	GeoPoint lilUpcentrePoint = new GeoPoint(centrePoint.getLatitudeE6()+1000/mapView.getZoomLevel(), centrePoint.getLongitudeE6());
	if(lilUpcentrePoint !=null)
		mapcontroller.animateTo(lilUpcentrePoint);
}
	
	
	
	private void putInitialOverlay()
	{
		Log.i(TAG,"initializing this user location");
	    mapView.setBuiltInZoomControls(true);
	    mapcontroller = mapView.getController();
	    mapcontroller.setZoom(14);
	    Log.i(TAG,"setting myoverlay");        
	    thisUserOverlay = new ThisUserItemizedOverlay(mapView); 
	    //SBGeoPoint currGeo = ThisUser.getInstance().getCurrentGeoPoint();
	    //Log.i(TAG,"location is:"+currGeo.getLatitudeE6()+","+currGeo.getLongitudeE6());		
	    thisUserOverlay.addThisUser();	    
	    mapView.getOverlays().add(thisUserOverlay);
	    mapView.postInvalidate();	       
	    mapcontroller.animateTo(ThisUser.getInstance().getCurrentGeoPoint());
	    //onResume of mapactivity doesnt update user till its once initialized
	    mapInitialized = true;
	  
	}


	
	public void updateNearbyUsers() {
		
		//caution while updating nearbyusers
		//this user may be interacting with a view so we are going to show progressbar
				
		if(!CurrentNearbyUsers.getInstance().usersHaveChanged())
			return;
		
		String updateString ="";
		if(CurrentNearbyUsers.getInstance().getAllNearbyUsers() == null)
			updateString = "Matching users found"; //fist time update
		else
			updateString = "Matching users changed";  //subsequent update
		
		ProgressHandler.showInfiniteProgressDialoge(underlyingActivity, "Updating users",updateString);
		//this call changes current list to new list
		//we are maintaining two list as dont want to unnecessaarily update mapview is no user has changed
		CurrentNearbyUsers.getInstance().updateCurrentToNew();
		List<NearbyUser> nearbyUsers = CurrentNearbyUsers.getInstance().getAllNearbyUsers();
		
		//update map view
		Log.i(TAG,"updating earby user");
		if(nearbyUserItemizedOverlay!=null)
		{
			Log.i(TAG,"removing prev nearby users overlay");			
			mapView.getOverlays().remove(nearbyUserItemizedOverlay);
			mapView.removeAllNearbyUserView();
		}		
		
		
		//null means 0 users returned by server or not yet single call to server
		if(nearbyUsers == null)
			return;			
		
		nearbyUserItemizedOverlay = new NearbyUsersItemizedOverlay(mapView);
		nearbyUserItemizedOverlay.addList(nearbyUsers);
		Log.i(TAG,"adding nearby useroverlay");		
		mapView.getOverlays().add(nearbyUserItemizedOverlay);	
		mapView.postInvalidate();
		
		//update listview
		NearbyUsersListViewAdapter adapter = new NearbyUsersListViewAdapter(underlyingActivity, nearbyUsers);
		
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
		
		ProgressHandler.dismissDialoge();
		centerMap();
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
		    thisUserOverlay.updateThisUser();
		    Log.i(TAG,"this user map overlay updated");
		    mapView.postInvalidate();	       
		    //mapcontroller.animateTo(ThisUser.getInstance().getSourceGeoPoint());
		}
		else
			Log.i(TAG,"but thisUSeroverlay empty!how?shldnt be..we initialixed it in init");
	}	
	
		
	private void centerMap() {

		int mylat = ThisUser.getInstance().getCurrentGeoPoint().getLatitudeE6();
		int mylon = ThisUser.getInstance().getCurrentGeoPoint().getLongitudeE6();
        int minLat = mylat;
        int maxLat = mylat;
        int minLon = mylon;
        int maxLon = mylon;
        
        List<NearbyUser> nearbyUsers = CurrentNearbyUsers.getInstance().getAllNearbyUsers();
        for (NearbyUser n : nearbyUsers) {
        		SBGeoPoint geoPoint = n.getUserLocInfo().getGeoPoint();
                int lat = (int) (geoPoint.getLatitudeE6());
                int lon = (int) (geoPoint.getLongitudeE6());

                maxLat = Math.max(lat, maxLat);
                minLat = Math.min(lat, minLat);
                maxLon = Math.max(lon, maxLon);
                minLon = Math.min(lon, minLon);
        }

        mapcontroller.zoomToSpan(Math.abs(maxLat - minLat), Math.abs(maxLon - minLon));        
        mapcontroller.animateTo(new GeoPoint((maxLat + minLat) / 2, (maxLon + minLon) / 2));
}
	
public void clearAllData()
{
	if(mapView!=null)
	{
		mapView.removeAllViews();
		mapView.getOverlays().clear();
	}
	if(listFrag!=null && listFrag.getListAdapter()!=null)
	{		
		((NearbyUsersListViewAdapter)listFrag.getListAdapter()).clear();
	}
	if(mSource!=null)
		mSource.setText(R.string.source_listview);
	if(mDestination!=null)
		mDestination.setText(R.string.destination_listview);
	if(mtime!=null)
		mtime.setText("");
}


@Override
public void onReceive(Context context, Intent intent) {
	String intentAction = intent.getAction();
	if(intentAction.equals(ServerConstants.NEARBY_USER_UPDATED))
	{
		ToastTracker.showToast("update intent received");
		updateNearbyUsers();
	}	
}

public ViewGroup getThisListContainerWithListView() {
    if (mListViewContainer == null) {
        mListViewContainer = (ViewGroup) underlyingActivity.getLayoutInflater().inflate(R.layout.nearbyuserlistview, null, false);
        mListImageView = (ImageView) mListViewContainer.findViewById(R.id.selfthumbnail);
        mUserName = (TextView) mListViewContainer.findViewById(R.id.my_name_listview);
        mDestination = (TextView) mListViewContainer.findViewById(R.id.my_destination_listview);
        mSource =  (TextView) mListViewContainer.findViewById(R.id.my_source_listview);        
       
        mtime = (TextView) mListViewContainer.findViewById(R.id.my_time_listview); 
        updateUserNameInListView();
        updateUserPicInListView();
        //mMapViewContainer.removeView(mMapView);
    }    

	return mListViewContainer;
}

public void updateUserPicInListView() {
    if (mListImageView != null) {
        String fbPicURL = ThisUserConfig.getInstance().getString(ThisUserConfig.FBPICURL);
        if (fbPicURL != "") {
            SBImageLoader.getInstance().displayImageElseStub(fbPicURL, mListImageView, R.drawable.userpicicon);
        } else {
            mListImageView.setImageDrawable(Platform.getInstance().getContext().getResources().getDrawable(R.drawable.userpicicon));
        }
    }
}

public void updateUserNameInListView() {
    if (mUserName != null) {
        String userName = ThisUserConfig.getInstance().getString(ThisUserConfig.USERNAME);
        if (userName=="") {
        	ToastTracker.showToast("haaw..username null!!");
            return;
        }
        mUserName.setText(userName);
    }
}

public void updateSrcDstTimeInListView() {
	
	if (mListViewContainer == null) {
        mListViewContainer = (ViewGroup) underlyingActivity.getLayoutInflater().inflate(R.layout.nearbyuserlistview, null, false);
        mListImageView = (ImageView) mListViewContainer.findViewById(R.id.selfthumbnail);
        mUserName = (TextView) mListViewContainer.findViewById(R.id.my_name_listview);
        mDestination = (TextView) mListViewContainer.findViewById(R.id.my_destination_listview);
        mSource =  (TextView) mListViewContainer.findViewById(R.id.my_source_listview);
        mtime = (TextView) mListViewContainer.findViewById(R.id.my_time_listview); 
	}
	
    SBGeoPoint sourceGeoPoint = ThisUser.getInstance().getSourceGeoPoint();
    if (sourceGeoPoint != null) {
    	mSource.setText(sourceGeoPoint.getAddress());
    }    

    SBGeoPoint destinationGeoPoint = ThisUser.getInstance().getDestinationGeoPoint();
    if (destinationGeoPoint != null) {
        mDestination.setText(destinationGeoPoint.getAddress());
    }    
    
    String date_time = ThisUser.getInstance().getDateAndTimeOfRequest();
    if(date_time != "")
    {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    	Date date;
		try {
			date = formatter.parse(date_time);
			formatter.applyPattern("h:mm a, EEE, MMM d");
	    	String newFormat = formatter.format(date);
	    	mtime.setText("Time: "+newFormat);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
    	
    }
}
		
}
