package my.b1701.SB.MapHelpers;

import my.b1701.SB.R;
import my.b1701.SB.Activities.MapListViewTabActivity;
import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.ChatClient.ChatWindow;
import my.b1701.SB.HelperClasses.SBImageLoader;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.Platform.Platform;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class NearbyUserOverlayItem extends BaseOverlayItem{

	private static String TAG = "NearbyUserOverlayItem";

	protected MapView mMapView = null;
	private static Context context =MapListActivityHandler.getInstance().getUnderlyingActivity();
	protected static LayoutInflater mInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	View viewOnMarkerSmall = null; 
	View viewOnMarkerExpanded = null;
	ImageView picViewSmall = null;
	ImageView picViewExpanded = null;
	ImageView chatIcon = null;
	ImageView facebookIcon = null;
	GeoPoint mGeoPoint = null;
	String mImageURL= null;
	String mUserFBID= null;
	boolean isVisibleSmall = false;
	boolean isVisibleExpanded = false;
	
		
	public NearbyUserOverlayItem(GeoPoint geoPoint, String imageURL, String fbID ,MapView mapView) {
		super(geoPoint, imageURL, fbID);
		this.mGeoPoint = geoPoint;		
		this.mMapView = mapView;
		this.mImageURL = imageURL;
		this.mUserFBID = fbID;
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
			removeSmallView();
			viewOnMarkerExpanded = mInflater.inflate(R.layout.map_expanded_layout, null);
			picViewExpanded = (ImageView)viewOnMarkerExpanded.findViewById(R.id.expanded_pic);						
			chatIcon = (ImageView)viewOnMarkerExpanded.findViewById(R.id.chat_icon_view);
			facebookIcon = (ImageView)viewOnMarkerExpanded.findViewById(R.id.fb_icon_view);
			//SBImageLoader.getInstance().displayImageElseStub(mImageURL, picView, R.drawable.userpicicon);
			SBImageLoader.getInstance().displayImage(mImageURL, picViewExpanded);
			
			chatIcon.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View chatIconView) {
					
					if(!ThisUserConfig.getInstance().getBool(ThisUserConfig.FBCHECK))
					{
						
						final Dialog dialog = new Dialog(context);
						dialog.setContentView(R.layout.fblogin_dialog);
						dialog.setTitle("One time FB login..");
						
						Button dialogCloseButton = (Button) dialog.findViewById(R.id.button_close_fb_login_dialog);
						// if button is clicked, close the custom dialog
						dialogCloseButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});
						
						Button fbLoginButton = (Button) dialog.findViewById(R.id.signInViaFacebook);
						// if button is clicked, close the custom dialog
						fbLoginButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								((MapListViewTabActivity)context).buttonOnMapClick(v);
							}
						});
			 
						dialog.show();
						//Intent fbLoginIntent = new Intent(context,LoginActivity.class);			
						//MapListActivityHandler.getInstance().getUnderlyingActivity().startActivity(fbLoginIntent);
					}	
					else
					{
						Intent startChatIntent = new Intent(Platform.getInstance().getContext(),ChatWindow.class);					
						startChatIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP
					 			| Intent.FLAG_ACTIVITY_NEW_TASK);
						startChatIntent.putExtra("participant", mUserFBID);
						context.startActivity(startChatIntent);
					}
					
				}
			});
			
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
