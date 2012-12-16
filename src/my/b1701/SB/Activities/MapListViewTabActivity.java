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
import my.b1701.SB.TabHelpers.SherLockActionBarTabListener;
import my.b1701.SB.TabHelpers.SherlockActionBarTab;
import my.b1701.SB.Users.ThisUser;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TabHost;

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
	private ImageButton selfLocationButton;
	private SBMapFragment mapFrag;
	private SBListFragment listFrag;
	ActionBar ab;
	
	
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
        setContentView(R.layout.tab_navigation);
        ab= getSupportActionBar();
        ToastTracker.showToast("Your userid:"+ThisUser.getInstance().getUserID());        
              
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);       
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowTitleEnabled(false);
        ab.setIcon(R.drawable.hoponlogoforactionbar);
        //ab.setDisplayUseLogoEnabled(true);
        mapTab = new SherlockActionBarTab(this, "MapFragment");
        mapTab.setText(R.string.mapviewstr).setTabListener(new SherLockActionBarTabListener(this, SBMapFragment.class));
        ab.addTab((ActionBar.Tab)mapTab.getTab());       

        listTab = new SherlockActionBarTab(this, "ListFragment");
        listTab.setText(R.string.listviewstr).setTabListener(new SherLockActionBarTabListener(this, SBListFragment.class));
        ab.addTab((ActionBar.Tab)listTab.getTab());
        
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
        case R.id.menu_more:        	
        	
        	break;       	
        
        }      
        
		
        return super.onOptionsItemSelected(menuItem);
    }
    
    //show main screen popup menu
    private void showPopupMenu(View v){
    	   PopupMenu popupMenu = new PopupMenu(MapListViewTabActivity.this, v);
    	      popupMenu.getMenuInflater().inflate(R.menu.maindropdown, popupMenu.getMenu());
    	    
    	      popupMenu.setOnMenuItemClickListener((android.widget.PopupMenu.OnMenuItemClickListener) new OnMainMenuDropDownClickListener());
    	    
    	      popupMenu.show();
    	  }
    	
    private class OnMainMenuDropDownClickListener implements OnMenuItemClickListener
    {

		@Override
		public boolean onMenuItemClick(android.view.MenuItem menuItem) {
			switch (menuItem.getItemId())
	        {
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
	        	
	        }
			
			return false;
		}
    	
    }

    
   /* public void showMainDropDownMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);         
        popup.getMenuInflater().inflate(R.menu.maindropdown,popup.getMenu());  
        popup.show();
    }*/

    public ViewGroup getThisMapContainerWithMapView()
    {
    	if(mMapViewContainer == null)
    	{
    		mMapViewContainer = (ViewGroup) getLayoutInflater().inflate(R.layout.map,null,false);
    		mMapView = (SBMapView) mMapViewContainer.findViewById(R.id.map_view);
    		selfLocationButton = (ImageButton) mMapViewContainer.findViewById(R.id.my_location_button);
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
