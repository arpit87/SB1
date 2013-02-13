package my.b1701.SB.Activities;

import my.b1701.SB.R;
import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.CustomViewsAndListeners.SBMapView;
import my.b1701.SB.FacebookHelpers.FacebookConnector;
import my.b1701.SB.Fragments.FBLoginDialogFragment;
import my.b1701.SB.Fragments.SBListFragment;
import my.b1701.SB.Fragments.SBMapFragment;
import my.b1701.SB.Fragments.UserNameDialogFragment;
import my.b1701.SB.HelperClasses.ProgressHandler;
import my.b1701.SB.HelperClasses.SBImageLoader;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.LocationHelpers.SBGeoPoint;
import my.b1701.SB.LocationHelpers.SBLocationManager;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Server.ServerConstants;
import my.b1701.SB.Users.CurrentNearbyUsers;
import my.b1701.SB.Users.ThisUser;
import my.b1701.SB.Util.StringUtils;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.MapView;


public class MapListViewTabActivity extends SherlockFragmentActivity  {
	//public View mMapViewContainer;
	
	private static final String TAG = "my.b1701.SB.Activities.MapListViewTabActivity";
	
	MapListActivityHandler mapListActivityHandler = MapListActivityHandler.getInstance();
	private ViewGroup mMapViewContainer;	
	private SBMapView mMapView;
	
	
	private ImageButton selfLocationButton = null;
	private ToggleButton offerRideButton = null;
	
	ActionBar ab;
	private boolean currentIsOfferMode;
	
	private FacebookConnector fbconnect;
	FragmentManager fm = getSupportFragmentManager();
	private boolean isMapShowing = true;
   
    private ImageView mFbLogin;
	
	public FacebookConnector getFbConnector()
	{
		return fbconnect;
	}
	
	

	/** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
       // requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
       // requestWindowFeature((int) Window.FEATURE_ACTION_BAR & ~Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.maplistview);        
        showMapView();
        ab= getSupportActionBar();
        
        ab.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent_black));   
        
        ToastTracker.showToast("Your userid:"+ThisUser.getInstance().getUserID());
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);       
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowTitleEnabled(true);
        this.registerReceiver(mapListActivityHandler,new IntentFilter(ServerConstants.NEARBY_USER_UPDATED));    
        fbconnect = new FacebookConnector(this);
        
        Intent i = getIntent();
        if(i.hasExtra("uuid"))
        {         	
        	Bundle b = i.getExtras();
        	String uuid = b.getString("uuid");
        	Intent show_tutorial = new Intent(this,Tutorial.class);
    		show_tutorial.putExtra("uuid", uuid);
    		startActivity(show_tutorial);
        } 
        
    }
    
    
    
    @Override
    public void onResume(){
    	super.onResume();
    	//we update realtime when on map activity
    	SBLocationManager.getInstance().StartListeningtoNetwork(); 
    	if(CurrentNearbyUsers.getInstance().getAllNearbyUsers()!=null)
    		ToastTracker.showToast("current nearby user not null");
    	//MapListActivityHandler.getInstance().setUpdateMap(true);
    	//if(MapListActivityHandler.getInstance().isMapInitialized())
    	//	MapListActivityHandler.getInstance().updateThisUserMapOverlay();
        //updateDestinationInListView();
    }

    //test
    @Override
	public void onPause(){
    	super.onPause();
    	//MapListActivityHandler.getInstance().setUpdateMap(false);
    	SBLocationManager.getInstance().StopListeningtoGPS();    	
        SBLocationManager.getInstance().StopListeningtoNetwork();
    	//mymapview.getOverlays().clear();
    	//mymapview.postInvalidate();
    }
    
    @Override
    public void onDestroy()
    {    
    	super.onDestroy();    	
    	this.unregisterReceiver(mapListActivityHandler);
    	mapListActivityHandler.clearAllData();    	
    }
	
		
	/*@Override
	  public void onBackPressed() {
	    moveTaskToBack(true);
	  }*/
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbconnect.authorizeCallback(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
    	
        switch (menuItem.getItemId())
        {
        case R.id.menu_search:
        	//onSearchRequested();        	 
	    	 Intent searchInputIntent = new Intent(this,SearchInputActivity.class);	
	    	 searchInputIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	   		 startActivity(searchInputIntent);
        	break;
        case R.id.fb_login_menuitem:
        	if(ThisUserConfig.getInstance().getBool(ThisUserConfig.FBLOGGEDIN))
        	{
        		Toast.makeText(this, "Already logged in", Toast.LENGTH_SHORT);
        		break;
        	}
        	FBLoginDialogFragment fblogin_dialog = new FBLoginDialogFragment();
			fblogin_dialog.show(getSupportFragmentManager(), "fblogin_dialog");
			break;
        case R.id.fb_logout_menuitem:
        	//logout from chat server?
			FacebookConnector fbconnect = new FacebookConnector(MapListViewTabActivity.this);
        	fbconnect.logoutFromFB();
        	break;
        case R.id.settings_menuitem:
        	break;
        case R.id.exit_app_menuitem:
        	//delete user request,close service
        	Platform.getInstance().stopChatService();
        	finish();
        	break;   
   	 case R.id.btn_listview:   		
      	toggleMapListView(menuItem);
      	break; 	
   	 case R.id.test_app_menuitem:
   		//GetNearbyUserDialogFragment test_dialog = new GetNearbyUserDialogFragment();
   		//test_dialog.show(getSupportFragmentManager(), "test_dialog");
   		//FragmentTransaction ft = fm.beginTransaction();
        //ft.replace(R.id.tabcontent, new GetNearbyUserFragment());
        //ft.commit();
   		// Intent searchInputIntent = new Intent(this,SearchInputActivity.class);
   		// searchInputIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
   		// startActivity(searchInputIntent);
   		// break;
            break;
     case R.id.history:
         Intent intent = new Intent(MapListViewTabActivity.this, HistoryActivity.class);
         startActivity(intent);
        } 
        return super.onOptionsItemSelected(menuItem);
    }
    
    //show main screen popup menu
   /* private void showPopupMenu(View v){
    	   PopupMenu popupMenu = new PopupMenu(MapListViewTabActivity.this, v);
    	      popupMenu.getMenuInflater().inflate(R.menu.maindropdown, popupMenu.getMenu());
    	    
    	      popupMenu.setOnMenuItemClickListener((android.widget.PopupMenu.OnMenuItemClickListener) new OnMainMenuDropDownClickListener());
    	    
    	      popupMenu.show();
    	  }
    	*/
   
    
   /* public void showMainDropDownMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);         
        popup.getMenuInflater().inflate(R.menu.maindropdown,popup.getMenu());  
        popup.show();
    }*/
    
    public void buttonOnMapClick(View button)
    {
    	switch(button.getId())
    	{
    	case R.id.my_location_button:
    		MapListActivityHandler.getInstance().myLocationButtonClick();    		
    		break; 	
    	}
    }

    private void toggleMapListView(MenuItem menuItem)
    {
    	if(!isMapShowing)
    	{    		
    		isMapShowing = true;
    		showMapView();
    		menuItem.setIcon(R.drawable.maptolist);
    	}
    	else
    	{    		
    		isMapShowing = false;
    		showListView();
    		menuItem.setIcon(R.drawable.listtomap);
    	}
    		
    }
    
    private void showMapView()
    {
    	if (fm != null) {
            
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.maplistviewcontent, new SBMapFragment());
            ft.commit();
        }
    }
    
    private void showListView()
    {
if (fm != null) {
            
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.maplistviewcontent, new SBListFragment());
            ft.commit();
        }
    }
    
    
   
	public ViewGroup getThisMapContainerWithMapView()
    {
    	if(mMapViewContainer == null)
    	{
    		mMapViewContainer = (ViewGroup) getLayoutInflater().inflate(R.layout.map,null,false);
    		mMapView = (SBMapView) mMapViewContainer.findViewById(R.id.map_view);
    		selfLocationButton = (ImageButton) mMapViewContainer.findViewById(R.id.my_location_button);
    		//offerRideButton = (ToggleButton) mMapViewContainer.findViewById(R.id.offerride_button);
    		if(currentIsOfferMode)
    			offerRideButton.setChecked(true);
    		mMapView.getOverlays().clear();    		
    		MapListActivityHandler.getInstance().setMapView(mMapView);
            MapListActivityHandler.getInstance().setUnderlyingActivity(this);
            Log.i(TAG,"initialize handler");
            Log.i(TAG,"initialize mylocation");           
            MapListActivityHandler.getInstance().initMyLocation();  
            
    		//mMapViewContainer.removeView(mMapView);
    	}
    	else
    	{
    		mMapViewContainer.addView(mMapView);
    		mMapViewContainer.addView(selfLocationButton);
    		
    		//mMapViewContainer.addView(offerRideButton);
    		//if(currentIsOfferMode)
    		//	offerRideButton.setChecked(true);
    	}
    	return mMapViewContainer;
    }

      
}
