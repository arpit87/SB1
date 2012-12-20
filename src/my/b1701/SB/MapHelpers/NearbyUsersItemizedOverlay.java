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
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


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
			NearbyUserOverlayItem overlayItem=new NearbyUserOverlayItem(u.getUserLocInfo().getGeoPoint(),u.getUserFBInfo().getImageURL(),u.getUserFBInfo().getFbid(),mMapView);
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
			
			final Dialog dialog = new Dialog(context);
			dialog.setContentView(R.layout.fblogin_dialog);
			dialog.setTitle("One time FB login..");
			
			/*Button dialogButton = (Button) dialog.findViewById(R.id.signInViaFacebook);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
				}
			});*/
 
			//dialog.show();
			Intent fbLoginIntent = new Intent(context,LoginActivity.class);			
			MapListActivityHandler.getInstance().getUnderlyingActivity().startActivity(fbLoginIntent);
		}
		Toast toast = Toast.makeText(Platform.getInstance().getContext(), "FB acces tok:"+ThisUserConfig.getInstance().getString(ThisUserConfig.FBACCESSTOKEN), Toast.LENGTH_SHORT);       
		toast.show();
		return true;
		
	}
	

	
	
}
