package my.b1701.SB.HelperClasses;

import my.b1701.SB.Platform.Platform;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class SBConnectivity {

	final static Context context = Platform.getInstance().getContext();
	public static boolean isConnected() {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		Log.d("SBConnectivity", "ConnectivityManager:"+cm.toString());
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnected();
	    }
}
