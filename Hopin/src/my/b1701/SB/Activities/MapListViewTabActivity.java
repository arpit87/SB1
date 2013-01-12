package my.b1701.SB.Activities;

import my.b1701.SB.R;
import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.CustomViewsAndListeners.SBMapView;
import my.b1701.SB.FacebookHelpers.FacebookConnector;
import my.b1701.SB.Fragments.FBLoginDialogFragment;
import my.b1701.SB.Fragments.SBListFragment;
import my.b1701.SB.Fragments.SBMapFragment;
import my.b1701.SB.Fragments.UserNameDialogFragment;
import my.b1701.SB.HelperClasses.SBImageLoader;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.LocationHelpers.SBGeoPoint;
import my.b1701.SB.LocationHelpers.SBLocationManager;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Users.ThisUser;
import my.b1701.SB.Util.StringUtils;
import android.content.Intent;
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


public class MapListViewTabActivity extends SherlockFragmentActivity implements UserNameDialogFragment.UserNameDialogListener {
	//public View mMapViewContainer;
	ActionBar bar;
	private static final String TAG = "my.b1701.SB.Activities.MapListViewTabActivity";
	
	private ViewGroup mMapViewContainer;
	private ViewGroup mListViewContainer;
	private SBMapView mMapView;
	private ListView mListView;
	ImageView mListImageView;
	
	private ImageButton selfLocationButton = null;
	private ToggleButton offerRideButton = null;
	private SBMapFragment mapFrag;
	private SBListFragment listFrag;
	ActionBar ab;
	private boolean currentIsOfferMode;
	
	private FacebookConnector fbconnect;
	FragmentManager fm = getSupportFragmentManager();
	private boolean isMapShowing = true;
    private TextView mDestination;
    private TextView mUserName;
    private ImageView mFbLogin;

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
	
	public FacebookConnector getFbConnector()
	{
		return fbconnect;
	}
	
	

	/** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
       // requestWindowFeature((int) Window.FEATURE_ACTION_BAR & ~Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.maplistview);        
        showMapView();
        ab= getSupportActionBar();
        
        ab.setBackgroundDrawable(getResources().getDrawable(R.drawable.abs_transparent));        
        ToastTracker.showToast("Your userid:"+ThisUser.getInstance().getUserID());
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);       
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowTitleEnabled(true);
        //ab.setIcon(R.drawable.hoponlogoforactionbar);
        //ab.setDisplayUseLogoEnabled(true);
      
        fbconnect = new FacebookConnector(this);
        
    }
    
    public void onResume(){
    	super.onResume();
    	//we update realtime when on map activity
    	SBLocationManager.getInstance().StartListeningtoNetwork(); 
    	MapListActivityHandler.getInstance().setUpdateMapRealTime(true);
    	if(MapListActivityHandler.getInstance().isMapInitialized())
    		MapListActivityHandler.getInstance().updateThisUserMapOverlay();
        updateDestinationInListView();
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

    public ViewGroup getThisListContainerWithListView() {
        if (mListViewContainer == null) {
            mListViewContainer = (ViewGroup) getLayoutInflater().inflate(R.layout.nearbyuserlistview, null, false);
            mListImageView = (ImageView) mListViewContainer.findViewById(R.id.list_user_image);
            mUserName = (TextView) mListViewContainer.findViewById(R.id.UserNameInList);
            mDestination = (TextView) mListViewContainer.findViewById(R.id.DestinationInList);
            mFbLogin = (ImageView) mListViewContainer.findViewById(R.id.FbLogin);

            mUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserNameDialogFragment userNameDialogFragment = new UserNameDialogFragment();
                    userNameDialogFragment.show(getSupportFragmentManager(), "UserName");
                }
            });

            mFbLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FBLoginDialogFragment fbLoginDialogFragment = new FBLoginDialogFragment();
                    fbLoginDialogFragment.show(getSupportFragmentManager(), "fblogin_dialog");
                }
            });

            mListView = (ListView) mListViewContainer.findViewById(R.id.list);
            //mMapViewContainer.removeView(mMapView);
        }

        updateUserNameInListView();
        updateUserPicInListView();
        updateDestinationInListView();

    	return mListViewContainer;
    }

    public void updateUserPicInListView() {
        if (mListImageView != null) {
            String fbPicURL = ThisUserConfig.getInstance().getString(ThisUserConfig.FBPICURL);
            if (!StringUtils.isEmpty(fbPicURL)) {
                SBImageLoader.getInstance().displayImageElseStub(fbPicURL, mListImageView, R.drawable.userpicicon);
            } else {
                mListImageView.setImageDrawable(Platform.getInstance().getContext().getResources().getDrawable(R.drawable.userpicicon));
            }
        }
    }

    public void updateUserNameInListView() {
        if (mUserName != null) {
            String userName = ThisUserConfig.getInstance().getString(ThisUserConfig.USERNAME);
            if (StringUtils.isBlank(userName)) {
                return;
            }
            mUserName.setText(userName);
        }
    }

    public void updateDestinationInListView() {
        if (mDestination != null) {
            SBGeoPoint destinationGeoPoint = ThisUser.getInstance().getDestinationGeoPoint();
            if (destinationGeoPoint != null) {
                mDestination.setText(destinationGeoPoint.getAddress());
            }
        }
    }

    @Override
    public void onSetUserNameClick(String userName) {
        ThisUserConfig.getInstance().putString(ThisUserConfig.USERNAME, userName);
        updateUserNameInListView();
    }
}
