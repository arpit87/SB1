package my.b1701.SB.Fragments;

import my.b1701.SB.R;
import my.b1701.SB.Activities.MapListViewTabActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.maps.MapView;

public class SBMapFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {				
	private static final String TAG = "SBMapFragment";	
	private ViewGroup mMapViewContainer;	
	private MapView mMapView;
	private ImageButton selfLocationButton;
	
	
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView( inflater, container, savedInstanceState );
		mMapViewContainer = ((MapListViewTabActivity)getActivity()).getThisMapContainerWithMapView();
		if(mMapView == null)
			mMapView = (MapView) mMapViewContainer.findViewById(R.id.map_view);
		//mMapViewContainer = inflater.inflate(R.layout.map,null,false);
		return mMapViewContainer;		
	}
	
	@Override
    public void onDestroyView() {
        super.onDestroyView();
        ViewGroup parentViewGroup = (ViewGroup) mMapViewContainer.getParent();
		if( null != parentViewGroup ) {
			parentViewGroup.removeView( mMapViewContainer );
		}
		mMapViewContainer.removeAllViews();
    }  
	
	
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	
}

