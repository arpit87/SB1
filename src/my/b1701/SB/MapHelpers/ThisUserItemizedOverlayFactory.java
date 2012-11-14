package my.b1701.SB.MapHelpers;

import com.google.android.maps.MapView;

public class ThisUserItemizedOverlayFactory extends ItemizedOverlayFactory{

	@Override
	public BaseItemizedOverlay createItemizedOverlay() {		
		return new ThisUserItemizedOverlay();
	}
	
	@Override
	public BaseItemizedOverlay createItemizedOverlay(MapView mapView) {		
		return new ThisUserItemizedOverlay(mapView);
	}

}
