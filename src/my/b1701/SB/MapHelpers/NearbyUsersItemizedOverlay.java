package my.b1701.SB.MapHelpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import my.b1701.SB.R;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Users.NearbyUser;
import android.graphics.drawable.Drawable;

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

	

	
	
}
