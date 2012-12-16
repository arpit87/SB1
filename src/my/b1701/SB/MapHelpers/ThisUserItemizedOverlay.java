package my.b1701.SB.MapHelpers;

import java.util.ArrayList;
import java.util.List;

import my.b1701.SB.R;
import my.b1701.SB.LocationHelpers.SBGeoPoint;
import my.b1701.SB.LocationHelpers.SBLocation;
import my.b1701.SB.LocationHelpers.SBLocationManager;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Users.ThisUser;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class ThisUserItemizedOverlay extends BaseItemizedOverlay{

	ArrayList<ThisUserOverlayItem> userList=new ArrayList<ThisUserOverlayItem>();
	ThisUserOverlayItem overlay;
	private static final String TAG = "ThisUserItemizedOverlay";
	
	public ThisUserItemizedOverlay() {
		super(Platform.getInstance().getContext().getResources().getDrawable(R.drawable.red_marker));
		// TODO Auto-generated constructor stub
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
		
		overlay=new ThisUserOverlayItem(ThisUser.getInstance().getCurrentGeoPoint(), ThisUser.getInstance().getUniqueID(), "");
		userList.add(overlay);
		populate();
	}
	
	public void  updateThisUser()
	{
		Log.i(TAG,"updating this user,removing overlay");
		if(overlay!=null)
			userList.remove(overlay);
		overlay=new ThisUserOverlayItem(ThisUser.getInstance().getCurrentGeoPoint(), ThisUser.getInstance().getUniqueID(), "");
		Log.i(TAG,"adding new this overlay");
		userList.add(overlay);
		populate();
	}

}
