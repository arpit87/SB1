package my.b1701.SB.ActivityHandlers;

import com.google.android.maps.MapView;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;

public class AddressListViewActivityHandler extends Handler{

	
	private static AddressListViewActivityHandler instance=new AddressListViewActivityHandler();
	private Activity underlyingActivity;
	
	public Activity getUnderlyingActivity() {
		return underlyingActivity;
	}


	public void setUnderlyingActivity(Activity underlyingActivity) {
		this.underlyingActivity = underlyingActivity;
	}


	private AddressListViewActivityHandler(){}	
	
	
	public static AddressListViewActivityHandler getInstance()
	{
		return instance;
	}

	
	public void handleMessage(Message msg) {		
		
	}
	
	public void handleOnClick(View v)
	{
		
	}
	public void handleOnClick(AdapterView<?> parent, View view, int position,
		    long id)
	{
		
		
	}
}
