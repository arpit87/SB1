package my.b1701.SB.MapHelpers;

import my.b1701.SB.R;
import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.FacebookHelpers.FacebookConnector;
import my.b1701.SB.Fragments.SmsDialogFragment;
import my.b1701.SB.HelperClasses.ChatHelper;
import my.b1701.SB.HelperClasses.SBImageLoader;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Users.NearbyUser;
import my.b1701.SB.Users.UserFBInfo;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class NearbyUserOverlayItem extends BaseOverlayItem{

	private static String TAG = "my.b1701.SB.MapHelpers.NearbyUserOverlayItem";

	protected MapView mMapView = null;
	private static Context context =MapListActivityHandler.getInstance().getUnderlyingActivity();
	protected static LayoutInflater mInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	View viewOnMarkerSmall = null; 
	View viewOnMarkerExpanded = null;	
	ImageView picViewSmall = null;
	ImageView picViewExpanded = null;
	ImageView chatIcon = null;
	ImageView smsIcon = null;
	ImageView facebookIcon = null;
	ImageView buttonClose = null;
	GeoPoint mGeoPoint = null;
	String mImageURL= "";
	String mUserFBID= "";
	String mUserFBUsername = "";
	boolean isVisibleSmall = false;
	boolean isVisibleExpanded = false;
	private NearbyUser mNearbyUser = null;
	private UserFBInfo mUserFBInfo = null;
		
	public NearbyUserOverlayItem(NearbyUser user ,MapView mapView) {
		super(user.getUserLocInfo().getGeoPoint(), user.getUserFBInfo().getImageURL(), user.getUserFBInfo().getFbid());
		this.mGeoPoint = user.getUserLocInfo().getGeoPoint();		
		this.mMapView = mapView;
		this.mUserFBInfo = user.getUserFBInfo();
		this.mImageURL = mUserFBInfo.getImageURL();
		this.mUserFBID = mUserFBInfo.getFbid();
		this.mUserFBUsername = mUserFBInfo.getFBUsername();
		this.mNearbyUser = user;
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
			if(mNearbyUser.getUserOtherInfo().isOfferingRide())
				viewOnMarkerSmall = mInflater.inflate(R.layout.map_frame_layout_green, null);
			else
				viewOnMarkerSmall = mInflater.inflate(R.layout.map_frame_layout_blue, null);
			
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
	
	private void setFBInfoOnExpandedBalloon(View balloonView,UserFBInfo userFBInfo)
	{
		
		TextView fb_name = null;
		TextView works_at = null;
		TextView studied_at = null;
		TextView hometown = null;
		TextView gender = null;
		
		String name_str,worksat_str,studiedat_str,hometown_str,gender_str = "";
		
		fb_name = (TextView)viewOnMarkerExpanded.findViewById(R.id.expanded_name);
		works_at = (TextView)viewOnMarkerExpanded.findViewById(R.id.expanded_work);
		studied_at = (TextView)viewOnMarkerExpanded.findViewById(R.id.expanded_education);
		hometown = (TextView)viewOnMarkerExpanded.findViewById(R.id.expanded_from);
		gender = (TextView)viewOnMarkerExpanded.findViewById(R.id.expanded_gender);
		
		name_str = mNearbyUser.getUserFBInfo().getName();
		worksat_str = mNearbyUser.getUserFBInfo().getWorksAt();
		studiedat_str = mNearbyUser.getUserFBInfo().getStudiedAt();
		hometown_str = mNearbyUser.getUserFBInfo().getHometown();
		gender_str = mNearbyUser.getUserFBInfo().getGender();
		
		if(name_str!="")
			fb_name.setText(name_str);
		
		if(worksat_str!="")
			works_at.setText("Works at "+worksat_str);
		
		if(studiedat_str!="")
			studied_at.setText("Studied at " +studiedat_str);
		
		if(hometown_str!="")
			hometown.setText("HomeTown " + hometown_str);
		
		if(gender_str!="")
			gender.setText("Gender "+gender_str);
		
		
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
			smsIcon = (ImageView)viewOnMarkerExpanded.findViewById(R.id.sms_icon);
			facebookIcon = (ImageView)viewOnMarkerExpanded.findViewById(R.id.fb_icon_view);
			buttonClose = (ImageView)viewOnMarkerExpanded.findViewById(R.id.button_close_balloon_expandedview);
			
			buttonClose.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View buttonClose) {
					showSmallIfExpanded();
				}
				});
			
			smsIcon.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View buttonClose) {
					SmsDialogFragment sms_dialog = new SmsDialogFragment(mUserFBID);
					sms_dialog.show(MapListActivityHandler.getInstance().getUnderlyingActivity().getSupportFragmentManager(), "sms_dialog");						
				}
				});
			//SBImageLoader.getInstance().displayImageElseStub(mImageURL, picView, R.drawable.userpicicon);
			
			//set balloon info
			setFBInfoOnExpandedBalloon(viewOnMarkerExpanded,mNearbyUser.getUserFBInfo());
			
			
			SBImageLoader.getInstance().displayImage(this.mImageURL, picViewExpanded);
			
			chatIcon.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View chatIconView) {
					ChatHelper.getInstance().onChatClickWithUser(mUserFBID);						
				}
			});
			
			facebookIcon.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View chatIconView) {
					FacebookConnector fbconnect = new FacebookConnector((Activity)context);
					fbconnect.openFacebookPage(mUserFBID,mUserFBUsername);						
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
