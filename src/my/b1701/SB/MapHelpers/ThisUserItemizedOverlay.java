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

public class ThisUserItemizedOverlay extends BaseItemizedOverlay{

	ArrayList<ThisUserOverlayItem> userList=new ArrayList<ThisUserOverlayItem>();
	
	public ThisUserItemizedOverlay() {
		super(Platform.getInstance().getContext().getResources().getDrawable(R.drawable.red_marker));
		// TODO Auto-generated constructor stub
	}

	@Override
	protected BaseOverlayItem createItem(int i) {
		return userList.get(i);
	}

	public void addList(List<?> allUsers) {		
		
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return userList.size();
	}

	@Override
	public void addThisUser() {
		SBGeoPoint currGeo = ThisUser.getInstance().getCurrentGeoPoint();
		if(currGeo == null)
		{
			SBLocation currLoc = null;
			while(currLoc == null)
				currLoc = SBLocationManager.getInstance().getLastBestLocation();
			currGeo =new SBGeoPoint(currLoc);
			ThisUser.getInstance().setCurrentGeoPoint(currGeo);
		}
		ThisUserOverlayItem overlay=new ThisUserOverlayItem(currGeo, ThisUser.getInstance().getUniqueID(), "");
		userList.add(overlay);
		populate();
	}

}
