package my.b1701.SB.MapHelpers;

import my.b1701.SB.R;
import my.b1701.SB.HelperClasses.SBImageLoader;
import my.b1701.SB.HelperClasses.Store;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.Platform.Platform;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class NearbyUserOverlayItem extends BaseOverlayItem{

	private static String TAG = "NearbyUserOverlayItem";
	
	private MapView mMapView = null;
	private static LayoutInflater mInflater;
	View viewOnMarker = null; 
	ImageView picView = null;
	GeoPoint mGeoPoint = null;
	String mImageURL= null;
	boolean isVisible = false; 
	
	public NearbyUserOverlayItem(GeoPoint geoPoint, String imageURL, String arg2,MapView mapView) {
		super(geoPoint, imageURL, arg2);
		this.mGeoPoint = geoPoint;		
		this.mMapView = mapView;
		this.mImageURL = imageURL;
		createAndDisplayView();
		/*Drawable icon= Platform.getInstance().getContext().getResources().getDrawable(R.drawable.green_marker);
		icon.setBounds(0, 0, icon.getIntrinsicHeight(), icon.getIntrinsicWidth());
		this.mMarker = icon;*/
	}
	
	private void createAndDisplayView()
	{
		if(mMapView == null)
			return;
		
		MapView.LayoutParams params = new MapView.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, mGeoPoint,
				MapView.LayoutParams.BOTTOM_CENTER);
		params.mode = MapView.LayoutParams.MODE_MAP;
		if(viewOnMarker==null)
		{
			mInflater = (LayoutInflater) Platform.getInstance().getContext().getSystemService(Platform.getInstance().getContext().LAYOUT_INFLATER_SERVICE);
			viewOnMarker = mInflater.inflate(R.layout.map_frame_layout, null);
			picView = (ImageView)viewOnMarker.findViewById(R.id.userpic);	
			
			//SBImageLoader.getInstance().displayImageElseStub(mImageURL, picView, R.drawable.userpicicon);
			SBImageLoader.getInstance().displayImage(mImageURL, picView);
			
			mMapView.addView(viewOnMarker,params);
			viewOnMarker.setVisibility(View.VISIBLE);
			isVisible = true;
		}
		else
		{			
			viewOnMarker.setLayoutParams(params);	
			viewOnMarker.setVisibility(View.VISIBLE);
			isVisible = true;
		
		}
		
	}
	
	public void removeView()
	{
		if(viewOnMarker!=null)
		{
			viewOnMarker.setVisibility(View.GONE);
			isVisible = false;
		}
		else
			Log.i(TAG,"trying to remove null thisUserMapView");
	}	
	
	public void ToggleView()
	{
		if(isVisible)
			removeView();
		else
			showView();
	}
	
	public void showView()
	{
		if(viewOnMarker!=null)
		{
			viewOnMarker.setVisibility(View.VISIBLE);
			isVisible = true;
		}
		else
		{
			createAndDisplayView();
			Log.i(TAG,"trying to show null thisUserMapView");
		}
	}
	

}
