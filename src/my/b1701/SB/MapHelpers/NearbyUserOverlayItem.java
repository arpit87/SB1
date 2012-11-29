package my.b1701.SB.MapHelpers;

import my.b1701.SB.R;
import my.b1701.SB.HelperClasses.SBImageLoader;
import my.b1701.SB.HelperClasses.Store;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.Platform.Platform;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

@SuppressLint("ParserError")
public class NearbyUserOverlayItem extends BaseOverlayItem{

	private static String TAG = "NearbyUserOverlayItem";

	protected MapView mMapView = null;
	protected static LayoutInflater mInflater = (LayoutInflater) Platform.getInstance().getContext().getSystemService(Platform.getInstance().getContext().LAYOUT_INFLATER_SERVICE);
	View viewOnMarkerSmall = null; 
	View viewOnMarkerExpanded = null;
	ImageView picViewSmall = null;
	ImageView picViewExpanded = null;
	GeoPoint mGeoPoint = null;
	String mImageURL= null;
	String mUserID= null;
	boolean isVisibleSmall = false;
	boolean isVisibleExpanded = false;
		
	public NearbyUserOverlayItem(GeoPoint geoPoint, String imageURL, String userID ,MapView mapView) {
		super(geoPoint, imageURL, userID);
		this.mGeoPoint = geoPoint;		
		this.mMapView = mapView;
		this.mImageURL = imageURL;
		this.mUserID = userID;
		createAndDisplaySmallView();
		/*Drawable icon= Platform.getInstance().getContext().getResources().getDrawable(R.drawable.green_marker);
		icon.setBounds(0, 0, icon.getIntrinsicHeight(), icon.getIntrinsicWidth());
		this.mMarker = icon;*/
	}
	
	protected void createAndDisplaySmallView()
	{
		if(mMapView == null || mGeoPoint == null)
			return;
		
		MapView.LayoutParams params = new MapView.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, mGeoPoint,
				MapView.LayoutParams.BOTTOM_CENTER);
		params.mode = MapView.LayoutParams.MODE_MAP;
		if(viewOnMarkerSmall==null)
		{			
			viewOnMarkerSmall = mInflater.inflate(R.layout.map_frame_layout_green, null);
			picViewSmall = (ImageView)viewOnMarkerSmall.findViewById(R.id.userpic);	
			
			viewOnMarkerSmall.setOnTouchListener(new NearbyUserOnTouchListener());
			
			if(mImageURL != "")
				SBImageLoader.getInstance().displayImageElseStub(mImageURL, picViewSmall, R.drawable.userpicicon);
			else
				picViewSmall.setImageDrawable( Platform.getInstance().getContext().getResources().getDrawable(R.drawable.nearbyusericon));
			//SBImageLoader.getInstance().displayImage(mImageURL, picViewSmall);
			
			mMapView.addView(viewOnMarkerSmall,params);
			viewOnMarkerSmall.setVisibility(View.VISIBLE);
			isVisibleSmall = true;
		}
		else
		{			
			viewOnMarkerSmall.setLayoutParams(params);	
			viewOnMarkerSmall.setVisibility(View.VISIBLE);
			isVisibleSmall = true;
		
		}
		
	}
	
	protected void createAndDisplayExpandedView()
	{
		if(mMapView == null || mGeoPoint == null)
			return;
		
		MapView.LayoutParams params = new MapView.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, mGeoPoint,
				MapView.LayoutParams.BOTTOM_CENTER);
		params.mode = MapView.LayoutParams.MODE_MAP;
		if(viewOnMarkerExpanded==null)
		{			
			viewOnMarkerExpanded = mInflater.inflate(R.layout.map_expanded_layout, null);
			picViewExpanded = (ImageView)viewOnMarkerExpanded.findViewById(R.id.expanded_pic);						
			
			//SBImageLoader.getInstance().displayImageElseStub(mImageURL, picView, R.drawable.userpicicon);
			SBImageLoader.getInstance().displayImage(mImageURL, picViewExpanded);
			
			removeSmallView();
			
			mMapView.addView(viewOnMarkerExpanded,params);
			viewOnMarkerExpanded.setVisibility(View.VISIBLE);
			isVisibleExpanded = true;
		}
		else
		{			
			viewOnMarkerExpanded.setLayoutParams(params);	
			viewOnMarkerExpanded.setVisibility(View.VISIBLE);
			isVisibleExpanded = true;		
		}
		
	}
	
	public void removeExpandedView()
	{
		if(viewOnMarkerExpanded!=null && isVisibleExpanded == true)
		{
			viewOnMarkerExpanded.setVisibility(View.GONE);
			isVisibleExpanded = false;
		}
		else
			Log.i(TAG,"trying to remove expanded null View");
	}	
	
	public void removeSmallView()
	{
		if(viewOnMarkerSmall!=null && isVisibleSmall == true)
		{
			viewOnMarkerSmall.setVisibility(View.GONE);
			isVisibleSmall = false;
		}
		else
			Log.i(TAG,"trying to remove null View");
	}	
	
	public void toggleSmallView()
	{
		if(isVisibleSmall)
			removeSmallView();
		else
			showSmallView();
	}
	
	public void showSmallIfExpanded()
	{
		if(isVisibleExpanded)
		{
			removeExpandedView();
			showSmallView();
		}		
	}
	
	public void showExpandedIfSmall()
	{
		if(isVisibleSmall)
		{
			removeSmallView();
			showExpandedView();
		}		
	}
	
	public void showSmallView()
	{
		if(viewOnMarkerSmall!=null)
		{			
			viewOnMarkerSmall.setVisibility(View.VISIBLE);
			isVisibleSmall = true;
		}
		else
		{
			createAndDisplaySmallView();			
		}
	}
	
	public void showExpandedView()
	{
		if(viewOnMarkerExpanded!=null)
		{
			viewOnMarkerExpanded.setVisibility(View.VISIBLE);
			isVisibleExpanded = true;
		}
		else
		{
			createAndDisplayExpandedView();			
		}
	}

	
	public class NearbyUserOnTouchListener implements OnTouchListener
	{		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			removeSmallView();
			showExpandedView();
			return true;
		}
		
	}
	

}
