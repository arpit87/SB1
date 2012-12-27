package my.b1701.SB.MapHelpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import my.b1701.SB.R;
import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.CustomViewsAndListeners.SBMapView;
import my.b1701.SB.Users.NearbyUser;
import android.content.Context;


public class NearbyUsersItemizedOverlay extends BaseItemizedOverlay{

	ArrayList<NearbyUserOverlayItem> userList=new ArrayList<NearbyUserOverlayItem>();
	private SBMapView mMapView = null;
	private static Context context = MapListActivityHandler.getInstance().getUnderlyingActivity();
	
	public NearbyUsersItemizedOverlay(SBMapView mapView) {
		super(boundCenter(context.getResources().getDrawable(R.drawable.my_dot_green)));
		this.mMapView = mapView;
	}
	
	public NearbyUsersItemizedOverlay() {
		super(boundCenter(context.getResources().getDrawable(R.drawable.my_dot_green)));
		}
	
	@Override
	public void addList(List<?> allUsers) {		
		Iterator<NearbyUser> it = (Iterator<NearbyUser>) allUsers.iterator();
		while(it.hasNext() )
		{
			NearbyUser u = it.next();
			NearbyUserOverlayItem overlayItem=new NearbyUserOverlayItem(u,mMapView);
			userList.add(overlayItem);
			populate();
		}	    
		
	}

	@Override
	protected NearbyUserOverlayItem createItem(int i) {
		return userList.get(i);
	}

	@Override
	public int size() {
		return userList.size();
	}

	public void removeAllSmallViews()
	{
		if(size()==0)
			return;
		Iterator<NearbyUserOverlayItem> it = (Iterator<NearbyUserOverlayItem>) userList.iterator();
		while(it.hasNext() )
		{
			it.next().removeSmallView();			
		}
	}
	
	public void removeExpandedShowSmallViews()
	{
		if(size()==0)
			return;
		Iterator<NearbyUserOverlayItem> it = (Iterator<NearbyUserOverlayItem>) userList.iterator();
		while(it.hasNext() )
		{
			it.next().showSmallIfExpanded();			
		}
	}
	
	
	
	protected boolean onTap(int i)
	{
		//on tap check if user logged in to fb
		userList.get(i).toggleSmallView();		
		return true;
		
	}
	

	
	
}
