package my.b1701.SB.ActivityHandlers;

import java.util.List;

import my.b1701.SB.MapHelpers.BaseItemizedOverlay;
import my.b1701.SB.MapHelpers.ItemizedOverlayFactory;
import my.b1701.SB.MapHelpers.NearbyUsersItemizedOverlayFactory;
import my.b1701.SB.Users.NearbyUser;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.google.android.maps.MapView;


public class MapActivityHandler extends Handler {
	
	MapView mapView;
	private static final String TAG = "MapActivityHandler";
	private static MapActivityHandler instance=new MapActivityHandler();
	private Activity underlyingActivity;
	private ItemizedOverlayFactory itemizedOverlayFactory;
	private BaseItemizedOverlay nearbyUserItemizedOverlay;

public enum MessageType {
	UPDATENEARBYUSER;	
		
	
}

	
	public Activity getUnderlyingActivity() {
		return underlyingActivity;			
	}


	public void setUnderlyingActivity(Activity underlyingActivity) {
		this.underlyingActivity = underlyingActivity;
	}


	private MapActivityHandler(){}	
	
	
	public static MapActivityHandler getInstance()
	{		
		return instance;
		
	}

	public MapView getMapView() {
		return mapView;
	}

	public void setMapView(MapView mapView) {
		this.mapView = mapView;
	}		
	
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
			
		
	}
	
	public void handleOnClick(View v)
	{
		

	}


	public void updateNearbyUsers(List<NearbyUser> nearbyUsers) {
		Log.i(TAG,"updating earby user");
		itemizedOverlayFactory = new NearbyUsersItemizedOverlayFactory();
		nearbyUserItemizedOverlay = itemizedOverlayFactory.createItemizedOverlay();
		nearbyUserItemizedOverlay.addList(nearbyUsers);
		Log.i(TAG,"adding nearby useroverlay");
		mapView.getOverlays().add(nearbyUserItemizedOverlay);	
		mapView.postInvalidate();
		
	}

	
}
