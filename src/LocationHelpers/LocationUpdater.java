package LocationHelpers;

import my.b1701.SB.Users.ThisUser;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

public class LocationUpdater extends BroadcastReceiver{
	
	private static LocationUpdater instance = null;
	private LocationUpdater(){};
	
	public static LocationUpdater getInstance()
	{
		if(instance == null)
		{
			instance =  new LocationUpdater();
		}
		
		return instance;
	}
	
	public void UpdateCurrentLocation(SBLocation newLocation)
	{
		ThisUser.getInstance().setLocation(newLocation);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		 context.unregisterReceiver(this);	      
	     String key = LocationManager.KEY_LOCATION_CHANGED;
	     Location location = (Location)intent.getExtras().get(key);
	     ThisUser.getInstance().setLocation(new SBLocation(location));
	}

}
