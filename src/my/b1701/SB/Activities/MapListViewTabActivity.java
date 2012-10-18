package my.b1701.SB.Activities;

import my.b1701.SB.R;
import my.b1701.SB.ActivityHandlers.MapActivityHandler;
import my.b1701.SB.Fragments.SBListFragment;
import my.b1701.SB.Fragments.SBMapFragment;
import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.TabHelperClasses.CompatTab;
import my.b1701.SB.TabHelperClasses.CompatTabListener;
import my.b1701.SB.TabHelperClasses.TabCompatActivity;
import my.b1701.SB.TabHelperClasses.TabHelper;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TabHost;

import com.google.android.maps.MapView;


public class MapListViewTabActivity extends TabCompatActivity {
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
        setContentView(R.layout.map_list_tabview); 
        mapFrag = new SBMapFragment();
        TabHelper tabHelper = getTabHelper();

        CompatTab mapTab = tabHelper.newTab("Map")
                .setText(R.string.mapviewstr)   
                .setIcon(R.drawable.tab_map)
                .setTabListener(new InstantiatingTabListener(this, SBMapFragment.class));
        tabHelper.addTab(mapTab);

        CompatTab listTab = tabHelper.newTab("List")
                .setText(R.string.listviewstr)     
                .setIcon(R.drawable.tab_chart)
                .setTabListener(new InstantiatingTabListener(this, SBListFragment.class));
        tabHelper.addTab(listTab);       
       
 
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
    	}
    	return mMapViewContainer;
    }
    
    /**
     * Implementation of {@link CompatTabListener} to handle tab change events. This implementation
     * instantiates the specified fragment class with no arguments when its tab is selected.
     */
    public static class InstantiatingTabListener implements CompatTabListener {

        private final TabCompatActivity mActivity;
        private final Class mClass;

        /**
         * Constructor used each time a new tab is created.
         *
         * @param activity The host Activity, used to instantiate the fragment
         * @param cls      The class representing the fragment to instantiate
         */
        public InstantiatingTabListener(TabCompatActivity activity, Class<? extends Fragment> cls) {
            mActivity = activity;
            mClass = cls;
        }

        /* The following are each of the ActionBar.TabListener callbacks */
        public void onTabSelected(CompatTab tab, FragmentTransaction ft) {
            // Check if the fragment is already initialized
            Fragment fragment = tab.getFragment();
            if (fragment == null) {
                // If not, instantiate and add it to the activity
                fragment = Fragment.instantiate(mActivity, mClass.getName());
                tab.setFragment(fragment);
                ft.add(android.R.id.tabcontent, fragment, tab.getTag());
            } else {
                // If it exists, simply attach it in order to show it
                ft.attach(fragment);
            }
        }
        
        public void onTabUnselected(CompatTab tab, FragmentTransaction ft) {
            Fragment fragment = tab.getFragment();
            if (fragment != null) {
                // Detach the fragment, because another one is being attached
                ft.detach(fragment);
            }
        }

        public void onTabReselected(CompatTab tab, FragmentTransaction ft) {
            // User selected the already selected tab. Do nothing.
        }
    }
	
	

}

