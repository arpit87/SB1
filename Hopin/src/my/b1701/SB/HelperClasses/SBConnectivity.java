package my.b1701.SB.HelperClasses;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class SBConnectivity {

	public static boolean isConnected(final Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(
		    Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnected();
	    }
}
