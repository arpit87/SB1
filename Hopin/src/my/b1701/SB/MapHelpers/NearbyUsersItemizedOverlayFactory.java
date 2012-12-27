package my.b1701.SB.MapHelpers;

import my.b1701.SB.CustomViewsAndListeners.SBMapView;

public class NearbyUsersItemizedOverlayFactory extends ItemizedOverlayFactory {

	@Override
	public BaseItemizedOverlay createItemizedOverlay(SBMapView mapView) {
		// TODO Auto-generated method stub
		return new NearbyUsersItemizedOverlay(mapView);
	}

	@Override
	public BaseItemizedOverlay createItemizedOverlay() {
		// TODO Auto-generated method stub
		return new NearbyUsersItemizedOverlay();
	}

}
