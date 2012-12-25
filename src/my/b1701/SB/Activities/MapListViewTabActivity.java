package my.b1701.SB.Activities;

import my.b1701.SB.R;
import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.CustomViewsAndListeners.SBMapView;
import my.b1701.SB.FacebookHelpers.FacebookConnector;
import my.b1701.SB.Fragments.SBListFragment;
import my.b1701.SB.Fragments.SBMapFragment;
import my.b1701.SB.HelperClasses.SBImageLoader;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.LocationHelpers.SBLocationManager;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.TabHelpers.SherlockActionBarTab;
import my.b1701.SB.Users.ThisUser;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


public class MapListViewTabActivity extends SherlockFragmentActivity {
	//public View mMapViewContainer;
	ActionBar bar;
	private static final String TAG = "MapListViewTabActivity";
	private MapListActivityHandler mapListActivityHandler;
	private boolean mapInitialized = false;
	private ViewGroup mMapViewContainer;
	private ViewGroup mListViewContainer;
	private SBMapView mMapView;
	private ListView mListView;
	ImageView mListImageView;
	private SherlockActionBarTab mapTab;
	private SherlockActionBarTab listTab;
	private TabHost tabHost;
	private ImageButton selfLocationButton = null;
	private ToggleButton offerRideButton = null;
	private SBMapFragment mapFrag;
	private SBListFragment listFrag;
	ActionBar ab;
	private boolean currentIsOfferMode;
	private Button fbloginbutton;
	private FacebookConnector fbconnect;
	FragmentManager fm = getSupportFragmentManager();
	private boolean isMapShowing = true;
	
	public Fragment getListFrag() {
		return listFrag;		
	}
	
	public Fragment getMapFrag() {
		return mapFrag;
	}	
	

	public void setListFrag(SBListFragment listFrag) {
		this.listFrag = listFrag;
	}
	
	public void setMapFrag(SBMapFragment mapFrag) {
		this.mapFrag = mapFrag;
	}

	/** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
       // requestWindowFeature((int) Window.FEATURE_ACTION_BAR & ~Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.tab_navigation_trial);
        fm.enableDebugLogging(true);
        showMapView();
        ab= getSupportActionBar();
        
        ab.setBackgroundDrawable(getResources().getDrawable(R.drawable.abs_transparent));        
        ToastTracker.showToast("Your userid:"+ThisUser.getInstance().getUserID());
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);       
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowTitleEnabled(false);
        //ab.setIcon(R.drawable.hoponlogoforactionbar);
        //ab.setDisplayUseLogoEnabled(true);
       /* mapTab = new SherlockActionBarTab(this, "MapFragment");
        mapTab.setText(R.string.mapviewstr).setTabListener(new SherLockActionBarTabListener(this, SBMapFragment.class));
        ab.addTab((ActionBar.Tab)mapTab.getTab());       

        listTab = new SherlockActionBarTab(this, "ListFragment");
        listTab.setText(R.string.listviewstr).setTabListener(new SherLockActionBarTabListener(this, SBListFragment.class));
        ab.addTab((ActionBar.Tab)listTab.getTab());*/
        
        fbconnect = new FacebookConnector(this);
        
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
        	onSearchRequested();
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
   	 case R.id.menu_switch_listview:
      	toggleMapListView();
      	break; 	
        
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
    	
    	case R.id.offerride_button:
    		offerRideClick();
    		break;
    	case R.id.signInViaFacebook:
    		fbconnect.loginToFB();
    		break;
    	
    	
    	}
    }

    private void toggleMapListView()
    {
    	if(!isMapShowing)
    	{
    		isMapShowing = true;
    		showMapView();
    	}
    	else
    	{
    		isMapShowing = false;
    		showListView();
    	}
    		
    }
    
    private void showMapView()
    {
    	if (fm != null) {
            
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.tabcontent, new SBMapFragment());
            ft.commit();
        }
    }
    
    private void showListView()
    {
if (fm != null) {
            
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.tabcontent, new SBListFragment());
            ft.commit();
        }
    }
    
    
    private void offerRideClick() {
    	String message = "";
    	currentIsOfferMode = ThisUserConfig.getInstance().getBool(ThisUserConfig.IsOfferMode);
    	 if(!currentIsOfferMode)
        	{
        		message = "Enabled ride offering mode";
        		ThisUserConfig.getInstance().putBool(ThisUserConfig.IsOfferMode,true);
        	}
        	else
        	{
        		message = "Disabled ride offering mode";
        		ThisUserConfig.getInstance().putBool(ThisUserConfig.IsOfferMode,false);
        	}	
    		
    	ToastTracker.showToast(message);		
	}

	public ViewGroup getThisMapContainerWithMapView()
    {
    	if(mMapViewContainer == null)
    	{
    		mMapViewContainer = (ViewGroup) getLayoutInflater().inflate(R.layout.map,null,false);
    		mMapView = (SBMapView) mMapViewContainer.findViewById(R.id.map_view);
    		selfLocationButton = (ImageButton) mMapViewContainer.findViewById(R.id.my_location_button);
    		offerRideButton = (ToggleButton) mMapViewContainer.findViewById(R.id.offerride_button);
    		if(currentIsOfferMode)
    			offerRideButton.setChecked(true);
    		mMapView.getOverlays().clear();
    		MapListActivityHandler.getInstance().setMapView(mMapView);
            MapListActivityHandler.getInstance().setUnderlyingActivity(this);
            Log.i(TAG,"initialize handler");
            Log.i(TAG,"initialize mylocation");
            ToastTracker.showToast("Updating location..",1);
            MapListActivityHandler.getInstance().initMyLocation();            
    		//mMapViewContainer.removeView(mMapView);
    	}
    	else
    	{
    		mMapViewContainer.addView(mMapView);
    		mMapViewContainer.addView(selfLocationButton);
    		mMapViewContainer.addView(offerRideButton);
    		if(currentIsOfferMode)
    			offerRideButton.setChecked(true);
    	}
    	return mMapViewContainer;
    }
    
    public ViewGroup getThisListContainerWithListView()
    {
    	if(mListViewContainer == null)
    	{
    		mListViewContainer = (ViewGroup) getLayoutInflater().inflate(R.layout.nearbyuserlistview,null,false);
    		mListImageView = (ImageView)mListViewContainer.findViewById(R.id.list_user_image);    		
    		String fbPicURL = ThisUserConfig.getInstance().getString(ThisUserConfig.FBPICURL);
			if(fbPicURL != "")
			{
				SBImageLoader.getInstance().displayImageElseStub(fbPicURL, mListImageView, R.drawable.userpicicon);
			}
			else
			{
				mListImageView.setImageDrawable( Platform.getInstance().getContext().getResources().getDrawable(R.drawable.userpicicon));
			}
			
    		mListView = (ListView) mListViewContainer.findViewById(R.id.list);    		
    		//mMapViewContainer.removeView(mMapView);
    	}  	
    	
    	return mListViewContainer;
    }

    

}
