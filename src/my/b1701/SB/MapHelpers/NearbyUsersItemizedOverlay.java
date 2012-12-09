package my.b1701.SB.MapHelpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import my.b1701.SB.R;
import my.b1701.SB.Activities.LoginActivity;
import my.b1701.SB.ActivityHandlers.MapActivityHandler;
import my.b1701.SB.FacebookHelpers.FacebookConnector;
import my.b1701.SB.HelperClasses.Constants;
import my.b1701.SB.HelperClasses.ThisUserConfig;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Users.NearbyUser;
import android.content.Intent;
import android.widget.Toast;

public class NearbyUsersItemizedOverlay extends BaseItemizedOverlay{

	ArrayList<NearbyUserOverlayItem> userList=new ArrayList<NearbyUserOverlayItem>();
	
	public NearbyUsersItemizedOverlay() {
		super(Platform.getInstance().getContext().getResources().getDrawable(R.drawable.green_marker));
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void addList(List<?> allUsers) {		
		Iterator<NearbyUser> it = (Iterator<NearbyUser>) allUsers.iterator();
		while(it.hasNext() )
		{
			NearbyUser u = it.next();
			NearbyUserOverlayItem overlayItem=new NearbyUserOverlayItem(u.GetUserGeopoint(),u.getUsername(),u.getUserDestination());
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

	protected boolean onTap(int i)
	{
		//on tap check if user logged in to fb
		if(!ThisUserConfig.getInstance().getBool(ThisUserConfig.FBCHECK))
		{
			Intent fbLoginIntent = new Intent(MapActivityHandler.getInstance().getUnderlyingActivity(),LoginActivity.class);			
			MapActivityHandler.getInstance().getUnderlyingActivity().startActivity(fbLoginIntent);
		}
		Toast toast = Toast.makeText(Platform.getInstance().getContext(), "FB acces tok:"+ThisUserConfig.getInstance().getString(ThisUserConfig.FBACCESSTOKEN), Toast.LENGTH_SHORT);       
		toast.show();
		return true;
		
	}
	

	
	
}
