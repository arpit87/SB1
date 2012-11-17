package my.b1701.SB.MapHelpers;

import my.b1701.SB.CustomViewsAndListeners.SBMapView;

public class ThisUserItemizedOverlayFactory extends ItemizedOverlayFactory{

	@Override
	public BaseItemizedOverlay createItemizedOverlay() {		
		return new ThisUserItemizedOverlay();
	}
	
	@Override
	public BaseItemizedOverlay createItemizedOverlay(SBMapView mapView) {		
		return new ThisUserItemizedOverlay(mapView);
	}

}
