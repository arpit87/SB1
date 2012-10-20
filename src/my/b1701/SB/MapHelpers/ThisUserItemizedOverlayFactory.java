package my.b1701.SB.MapHelpers;

import android.graphics.drawable.Drawable;

public class ThisUserItemizedOverlayFactory extends ItemizedOverlayFactory{

	@Override
	public BaseItemizedOverlay createItemizedOverlay() {		
		return new ThisUserItemizedOverlay();
	}

}
