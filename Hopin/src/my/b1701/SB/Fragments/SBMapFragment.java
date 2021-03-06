package my.b1701.SB.Fragments;

import my.b1701.SB.R;
import my.b1701.SB.Activities.MapListViewTabActivity;
import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import android.media.AudioRecord.OnRecordPositionUpdateListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.maps.MapView;

public class SBMapFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {				
	private static final String TAG = "my.b1701.SB.Fragments.SBMapFragment";
	private ViewGroup mMapViewContainer;	
	private MapView mMapView;
	private ImageButton selfLocationButton;
	
	@Override
	public void onCreate(Bundle savedState) {
        super.onCreate(null);
        Log.i(TAG,"oncreate,mapview");
        MapListActivityHandler.getInstance().setMapFrag(this);
	}
	
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView( inflater, container, null );
		Log.i(TAG,"oncreateview,mapview");
		mMapViewContainer = ((MapListViewTabActivity)getActivity()).getThisMapContainerWithMapView();
		if(mMapView == null)
			mMapView = (MapView) mMapViewContainer.findViewById(R.id.map_view);
		//mMapViewContainer = inflater.inflate(R.layout.map,null,false);
		return mMapViewContainer;		
	}	

	
	@Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG,"ondestroyview,mapview");
        ViewGroup parentViewGroup = (ViewGroup) mMapViewContainer.getParent();
		if( null != parentViewGroup ) {
			parentViewGroup.removeView( mMapViewContainer );
		}
		mMapViewContainer.removeView(mMapView);
		mMapViewContainer.removeAllViews();
    }  
	
	
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	
}

