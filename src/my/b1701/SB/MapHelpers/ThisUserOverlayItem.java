package my.b1701.SB.MapHelpers;

import my.b1701.SB.R;
import my.b1701.SB.HelperClasses.Store;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.Platform.Platform;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class ThisUserOverlayItem extends BaseOverlayItem{

	private static String TAG = "ThisUserOverlayItem";
	View thisUserViewWithFrame;	
	Drawable icon;
	private MapView mMapView = null;
	private static LayoutInflater mInflater;
	View viewOnMarker = null; 
	ImageView picView = null;
	GeoPoint mGeoPoint = null;
	String mImageURL= null;
	
	public ThisUserOverlayItem(GeoPoint geoPoint, String imageURL, String arg2,MapView mapView) {
		super(geoPoint, imageURL, arg2);	
		this.mGeoPoint = geoPoint;		
		this.mMapView = mapView;
		this.mImageURL = imageURL;
		/*Bitmap bmp = Store.getInstance().getBitmapFromFile(ThisUserConfig.FBPICFILENAME);
		if(bmp != null )
		{
			//if fb checked in then fetch image else show icon
			mInflater = (LayoutInflater) Platform.getInstance().getContext().getSystemService(Platform.getInstance().getContext().LAYOUT_INFLATER_SERVICE);
			thisUserViewWithFrame = mInflater.inflate(R.layout.map_frame_layout, null);
			userPic = (ImageView)thisUserViewWithFrame.findViewById(R.id.userpic);
			userPic.setImageBitmap(bmp);	
		}
		else
		{	
			userPic.setImageDrawable( Platform.getInstance().getContext().getResources().getDrawable(R.drawable.userpicicon));
			
		}
		
		thisUserViewWithFrame.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		thisUserViewWithFrame.layout(0, 0, thisUserViewWithFrame.getMeasuredWidth(), thisUserViewWithFrame.getMeasuredHeight()); 
		thisUserViewWithFrame.buildDrawingCache(true);
		Bitmap b = Bitmap.createBitmap(thisUserViewWithFrame.getDrawingCache());
		thisUserViewWithFrame.setDrawingCacheEnabled(false);
		icon = (Drawable) new BitmapDrawable(Platform.getInstance().getContext().getResources(), bmp);
		
		icon.setBounds(0, 0, icon.getIntrinsicHeight(), icon.getIntrinsicWidth());
		this.mMarker = icon;*/
		createAndDisplayView();
	}
	
	//enable a view to be drawn over each marker
		
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
			Bitmap bmp = Store.getInstance().getBitmapFromFile(ThisUserConfig.FBPICFILENAME);
			if(bmp != null )
			{
				picView.setImageBitmap(bmp);
			}
			else
			{
				picView.setImageDrawable( Platform.getInstance().getContext().getResources().getDrawable(R.drawable.userpicicon));
			}
			mMapView.addView(viewOnMarker,params);
			viewOnMarker.setVisibility(View.VISIBLE);
		}
		else
		{			
			viewOnMarker.setLayoutParams(params);	
			viewOnMarker.setVisibility(View.VISIBLE);
		
		}
		
	}
	
	public void removeView()
	{
		if(viewOnMarker!=null)
			viewOnMarker.setVisibility(View.GONE);
		else
			Log.i(TAG,"trying to remove null thisUserMapView");
	}	

}
