package my.b1701.SB.MapHelpers;

import com.google.android.maps.MapView;

public class NearbyUsersItemizedOverlayFactory extends ItemizedOverlayFactory {

	@Override
	public BaseItemizedOverlay createItemizedOverlay(MapView mapView) {
		// TODO Auto-generated method stub
		return new NearbyUsersItemizedOverlay(mapView);
	}

	@Override
	public BaseItemizedOverlay createItemizedOverlay() {
		// TODO Auto-generated method stub
		return new NearbyUsersItemizedOverlay();
	}

}
