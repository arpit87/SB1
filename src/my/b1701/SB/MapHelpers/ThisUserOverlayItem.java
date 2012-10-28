package my.b1701.SB.MapHelpers;

import my.b1701.SB.R;
import my.b1701.SB.Platform.Platform;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;

public class ThisUserOverlayItem extends BaseOverlayItem{

	public ThisUserOverlayItem(GeoPoint arg0, String arg1, String arg2) {
		super(arg0, arg1, arg2);	
		Drawable icon= Platform.getInstance().getContext().getResources().getDrawable(R.drawable.red_marker);
		icon.setBounds(0, 0, icon.getIntrinsicHeight(), icon.getIntrinsicWidth());
		this.mMarker = icon;
	}

}
