package my.b1701.SB.BroadcastReceivers;

import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.LocationHelpers.LocationService;
import my.b1701.SB.Platform.Platform;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class KillLocationServiceReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context thisContext, Intent thisIntent) {
		thisContext.stopService(new Intent(thisContext,LocationService.class));
		ToastTracker.showToast("location service killed");	
	}

}
