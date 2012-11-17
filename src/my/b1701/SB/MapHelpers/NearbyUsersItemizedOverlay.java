package my.b1701.SB.MapHelpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import my.b1701.SB.R;
import my.b1701.SB.Activities.LoginActivity;
import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.CustomViewsAndListeners.SBMapView;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Users.NearbyUser;
import android.content.Intent;
import android.widget.Toast;

public class NearbyUsersItemizedOverlay extends BaseItemizedOverlay{

	ArrayList<NearbyUserOverlayItem> userList=new ArrayList<NearbyUserOverlayItem>();
	private SBMapView mMapView = null;
	
	public NearbyUsersItemizedOverlay(SBMapView mapView) {
		super(boundCenter(Platform.getInstance().getContext().getResources().getDrawable(R.drawable.new_green_marker)));
		this.mMapView = mapView;
	}
	
	public NearbyUsersItemizedOverlay() {
		super(boundCenter(Platform.getInstance().getContext().getResources().getDrawable(R.drawable.new_green_marker)));
		}
	
	@Override
	public void addList(List<?> allUsers) {		
		Iterator<NearbyUser> it = (Iterator<NearbyUser>) allUsers.iterator();
		while(it.hasNext() )
		{
			NearbyUser u = it.next();
			NearbyUserOverlayItem overlayItem=new NearbyUserOverlayItem(u.getUserLocInfo().getGeoPoint(),u.getUserFBInfo().getImageURL(),u.getUserLocInfo().getUserDestination(),mMapView);
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
		if(!ThisUserConfig.getInstance().getBool(ThisUserConfig.FBCHECK))
		{
			Intent fbLoginIntent = new Intent(MapListActivityHandler.getInstance().getUnderlyingActivity(),LoginActivity.class);			
			MapListActivityHandler.getInstance().getUnderlyingActivity().startActivity(fbLoginIntent);
		}
		Toast toast = Toast.makeText(Platform.getInstance().getContext(), "FB acces tok:"+ThisUserConfig.getInstance().getString(ThisUserConfig.FBACCESSTOKEN), Toast.LENGTH_SHORT);       
		toast.show();
		return true;
		
	}
	

	
	
}
