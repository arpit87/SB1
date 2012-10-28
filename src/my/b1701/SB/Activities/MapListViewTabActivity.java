package my.b1701.SB.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TabHost;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.MapView;
import my.b1701.SB.ActivityHandlers.MapActivityHandler;
import my.b1701.SB.Fragments.SBListFragment;
import my.b1701.SB.Fragments.SBMapFragment;
import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.R;
import my.b1701.SB.TabHelpers.SherLockActionBarTabListener;
import my.b1701.SB.TabHelpers.SherlockActionBarTab;


public class MapListViewTabActivity extends SherlockFragmentActivity {
	//public View mMapViewContainer;
	ActionBar bar;
	private static final String TAG = "MapListViewTabActivity";
	private MapActivityHandler mapActivityHandler;
	private boolean mapInitialized = false;
	private ViewGroup mMapViewContainer;
	private MapView mMapView;
	private TabHost tabHost;
	private ImageButton selfLocationButton;
	private Fragment mapFrag;
	/** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_navigation);
        ActionBar ab= getSupportActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        SherlockActionBarTab mapTab = new SherlockActionBarTab(this, "MapFragment");
        mapTab.setText(R.string.mapviewstr).setIcon(R.drawable.tab_map).setTabListener(new SherLockActionBarTabListener(this, SBMapFragment.class));
        ab.addTab((ActionBar.Tab)mapTab.getTab());

        SherlockActionBarTab listTab = new SherlockActionBarTab(this, "ListFragment");
        listTab.setText(R.string.listviewstr).setIcon(R.drawable.tab_map).setTabListener(new SherLockActionBarTabListener(this, SBListFragment.class));
        ab.addTab((ActionBar.Tab)listTab.getTab());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.menu_search){
            onSearchRequested();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public ViewGroup getThisMapContainerWithMapView()
    {
    	if(mMapViewContainer == null)
    	{
    		mMapViewContainer = (ViewGroup) getLayoutInflater().inflate(R.layout.map,null,false);
    		mMapView = (MapView) mMapViewContainer.findViewById(R.id.map_view);
    		selfLocationButton = (ImageButton) mMapViewContainer.findViewById(R.id.my_location_button);
    		mMapView.getOverlays().clear();
    		MapActivityHandler.getInstance().setMapView(mMapView);
            MapActivityHandler.getInstance().setUnderlyingActivity(this);
            Log.i(TAG,"initialize handler");
            Log.i(TAG,"initialize mylocation");
            ToastTracker.showToast("Updating location..",1);
            MapActivityHandler.getInstance().initMyLocation();
    		//mMapViewContainer.removeView(mMapView);
    	}
    	else
    	{
    		mMapViewContainer.addView(mMapView);
    		mMapViewContainer.addView(selfLocationButton);
    	}
    	return mMapViewContainer;
    }


}
