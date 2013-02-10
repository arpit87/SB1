package my.b1701.SB.MapHelpers;

import java.util.ArrayList;

import my.b1701.SB.R;
import my.b1701.SB.CustomViewsAndListeners.SBMapView;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Users.ThisUser;
import android.util.Log;

public class ThisUserItemizedOverlay extends BaseItemizedOverlay{

	ArrayList<ThisUserOverlayItem> userList=new ArrayList<ThisUserOverlayItem>();
	ThisUserOverlayItem selfOverlayItem;
	private static final String TAG = "my.b1701.SB.MapHelpers.ThisUserItemizedOverlay";
	private SBMapView mMapView = null;
	
	
	public ThisUserItemizedOverlay(SBMapView mapView) {		
		super(boundCenter(Platform.getInstance().getContext().getResources().getDrawable(R.drawable.map_dp_frame_shadow)));
		this.mMapView = mapView;
		// TODO Auto-generated constructor stub
	}
	
	public ThisUserItemizedOverlay() {		
		super(boundCenter(Platform.getInstance().getContext().getResources().getDrawable(R.drawable.map_dp_frame_shadow)));		
	}

	@Override
	protected BaseOverlayItem createItem(int i) {
		return userList.get(i);
	}
	

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return userList.size();
	}
	
	@Override
	public void addThisUser() {
		
		selfOverlayItem=new ThisUserOverlayItem(ThisUser.getInstance().getCurrentGeoPoint(), ThisUser.getInstance().getUserID(), "",mMapView);
		userList.add(selfOverlayItem);
		populate();
	}
	
	public void  updateThisUser()
	{
		Log.i(TAG,"updating this user,removing overlay");		
		mMapView.removeSelfView();
		if(selfOverlayItem!=null)
		{			
			userList.remove(selfOverlayItem);			
		}
		selfOverlayItem=new ThisUserOverlayItem(ThisUser.getInstance().getSourceGeoPoint(), ThisUser.getInstance().getUserID(), "",mMapView);
		Log.i(TAG,"adding new this overlay");
		userList.add(selfOverlayItem);
		populate();
	}
	
	protected boolean onTap(int i)
	{
		Log.i(TAG,"toggling this user view");
		selfOverlayItem.ToggleView();
		return true;
		
	}
	
	
	
	

}
