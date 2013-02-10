package my.b1701.SB.LocationHelpers;

import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.HelperClasses.Constants;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Users.ThisUser;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

public class LocationUpdaterFromIntent extends BroadcastReceiver{
	
	private final String TAG = "my.b1701.SB.LocationHelpers.LocationUpdaterFromIntent";  
	//not yet used	
	public void UpdateToBestCurrentLocation(SBLocation newLocation)
	{
		
		ThisUser.getInstance().setCurrentLocation(newLocation);
		Toast toast = Toast.makeText(Platform.getInstance().getContext(), "Updating cur loc", Toast.LENGTH_SHORT);       
		toast.show();
		MapListActivityHandler.getInstance().updateThisUserMapOverlay();
		
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "location intent received");
		String locationKey = LocationManager.KEY_LOCATION_CHANGED;
		if(intent.hasExtra(locationKey))
		{
			context.unregisterReceiver(this);
			Location location = (Location)intent.getExtras().get(locationKey);
			Log.d(TAG,"updating loc in intent");
			ThisUser.getInstance().setCurrentLocation(new SBLocation(location));
		}
		else
			Log.d(TAG,"lockey not found in loc intent");
	}

	
}
