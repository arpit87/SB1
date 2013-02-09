package my.b1701.SB.MapHelpers;

import my.b1701.SB.R;
import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.CustomViewsAndListeners.SBMapView;
import my.b1701.SB.HelperClasses.CommunicationHelper;
import my.b1701.SB.HelperClasses.SBImageLoader;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.LocationHelpers.SBGeoPoint;
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
	private View viewOnMarkerSmall = null; 
	private View viewOnMarkerExpanded = null;	
	private ImageView picViewSmall = null;
	private ImageView picViewExpanded = null;
	private ImageView chatIcon = null;
	private ImageView smsIcon = null;
	private ImageView facebookIcon = null;
	private ImageView buttonClose = null;
	private SBGeoPoint mGeoPoint = null;
	private String mImageURL= "";
	private String mUserFBID= "";
	private String mUserFBUsername = "";
	boolean isVisibleSmall = false;
	boolean isVisibleExpanded = false;
	private NearbyUser mNearbyUser = null;
	private UserFBInfo mUserFBInfo = null;
		
	public NearbyUserOverlayItem(NearbyUser user ,SBMapView mapView) {
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
	
	public SBGeoPoint getGeoPoint() {
		return mGeoPoint;
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
		TextView userNotLoggedIn = null;
		TextView fb_name = null;
		TextView works_at = null;
		TextView studied_at = null;
		TextView hometown = null;
		TextView gender = null;
		
		String name_str,worksat_str,studiedat_str,hometown_str,gender_str = "";
		
		if(!mNearbyUser.getUserFBInfo().FBInfoAvailable())
			return;
		
		userNotLoggedIn = (TextView)viewOnMarkerExpanded.findViewById(R.id.usernotloggedintext);
		userNotLoggedIn.setVisibility(View.GONE);
		
		fb_name = (TextView)viewOnMarkerExpanded.findViewById(R.id.expanded_balloon_header);
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
			
			if(!ThisUserConfig.getInstance().getBool(ThisUserConfig.FBLOGGEDIN))
			{
				chatIcon.setImageResource(R.drawable.chat_icon_disabled);
				chatIcon.invalidate();
				smsIcon.setImageResource(R.drawable.sms_icon_disabled);
				smsIcon.invalidate();
				facebookIcon.setImageResource(R.drawable.fb_icon_disabled);
				facebookIcon.invalidate();
			}
			else if(!mUserFBInfo.FBInfoAvailable())
			{
				chatIcon.setImageResource(R.drawable.chat_icon_disabled);
				chatIcon.invalidate();				
				facebookIcon.setImageResource(R.drawable.fb_icon_disabled);
				facebookIcon.invalidate();
			}
			
			if(!mNearbyUser.getUserOtherInfo().isMobileNumberAvailable())
			{
				smsIcon.setImageResource(R.drawable.sms_icon_disabled);
				smsIcon.invalidate();
			}
			
			buttonClose.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View buttonClose) {
					showSmallIfExpanded();
				}
				});
			
			smsIcon.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View buttonClose) {
					CommunicationHelper.getInstance().onSmsClickWithUser(mUserFBID);
				}
				});
			//SBImageLoader.getInstance().displayImageElseStub(mImageURL, picView, R.drawable.userpicicon);
			
			//set balloon info
			setFBInfoOnExpandedBalloon(viewOnMarkerExpanded,mNearbyUser.getUserFBInfo());
			
			
			SBImageLoader.getInstance().displayImage(this.mImageURL, picViewExpanded);
			
			chatIcon.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View chatIconView) {
					CommunicationHelper.getInstance().onChatClickWithUser(mUserFBID);						
				}
			});
			
			facebookIcon.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View chatIconView) {
					CommunicationHelper.getInstance().onFBIconClickWithUser((Activity)context,mUserFBID,mUserFBUsername);						
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
			MapListActivityHandler.getInstance().centreMapToPlusLilUp(mGeoPoint);
			return true;
		}
		
	}
	

}
