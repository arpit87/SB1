package my.b1701.SB.Activities;

import java.io.UnsupportedEncodingException;
import java.util.List;

import my.b1701.SB.R;
import my.b1701.SB.HttpClient.HttpQueries;
import my.b1701.SB.LocationHelpers.AllUsersItemizedOverlay;
import my.b1701.SB.Server.ServerQueries;
import my.b1701.SB.Server.ServerResponse;
import my.b1701.SB.Users.OtherUser;

import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import HelperClasses.ThisAppInstallation;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class MyMapView extends MapActivity implements LocationListener { 
	
	private LocationManager locationmanager;
	private MapView mymapview;
	private MyLocationOverlay myOverlay;
	private AllUsersItemizedOverlay allUsersItemizedOverlay;
	private MapController myMapcontroller;
	private String locProvider;
	private Location location;
	private Location deslocation;
	private Button serachBuddiesButton;
	private EditText fromLocationView;
	private EditText desLocationView;
	//private UserPopUp userPopUp;
	

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view); 
        serachBuddiesButton = (Button)findViewById(R.id.searchBuddiesButton);
        fromLocationView = (EditText)findViewById(R.id.fromLocation);
        desLocationView = (EditText)findViewById(R.id.desLocation);        
        initMyLocation();  
        
        
    }  
    
    
    private void initMyLocation() {
		// TODO Auto-generated method stub
    	locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE) ;
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        locProvider = locationmanager.getBestProvider(criteria, false);
       // LocationListener myloclistener=new LocationListener();
        location = locationmanager.getLastKnownLocation(locProvider);
        
        mymapview=(MapView) findViewById(R.id.mapview);
        mymapview.setBuiltInZoomControls(true);
       
        myMapcontroller = mymapview.getController();
        myMapcontroller.setZoom(14);
        myOverlay= new MyLocationOverlay(this,mymapview);
        mymapview.getOverlays().add(myOverlay);
        mymapview.postInvalidate();
         
       locationmanager.requestLocationUpdates(locProvider, 0, 0, this);
        
        myOverlay.runOnFirstFix( new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				mymapview.getController().animateTo(myOverlay.getMyLocation());
			}
		});
			
         
        if(location!=null)
        {
        Toast.makeText(getApplicationContext(), "Lati:"+location.getLatitude(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "Longi:"+location.getLongitude(), Toast.LENGTH_SHORT).show();
        }
        else
        	Toast.makeText(getApplicationContext(), "location null,using "+locProvider, Toast.LENGTH_SHORT).show();
		
	}
    
    public void onClickMapViewButtons(View v) {
        switch (v.getId()) {
        case R.id.searchBuddiesButton:
        	getLocationListFromAddressString();
        	//updateNearbyBuddies();  
        	break;
        }
    }

	public void onResume(){
    	super.onResume();    	
    	myOverlay.enableMyLocation();
    	myOverlay.enableCompass();
    	locationmanager.requestLocationUpdates(locProvider, 0, 0, this);    	
    	//User user1=new User("arpit","34.5678","53.0987");
    	//User user2=new User("arpit1","30.5678","50.0987");
    	//User user3=new User("arpit2","30.5678","53.0987");
    	//User users[]={user1,user2,user3};
    	//PopulateNearbyUsers(users);
    	  	
    	
    }
	
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		  // Collect data from the intent and use it
		  Bundle desDetails=data.getExtras();
		  double desLatitude = desDetails.getDouble("deslatitude");
		  double desLongitude = desDetails.getDouble("deslongitude");
		  deslocation = new Location(locProvider);
		  deslocation.setLatitude(desLatitude);
		  deslocation.setLongitude(desLongitude);
		  updateNearbyBuddies();
		}
	
	private void  getLocationListFromAddressString()
	{
		
		String addressString =desLocationView.getText().toString();
		Intent showAddressList = new Intent(this,AddressListViewActivity.class);
		showAddressList.putExtra("addressString", addressString);	
		int requestCode=1;
		startActivityForResult(showAddressList, requestCode);
	}

    private void updateNearbyBuddies() {
		// TODO Auto-generated method stub
    	//new Runnable(){

			//public void run() {
				// TODO Auto-generated method stub
				JSONObject jsonobj=new JSONObject();
				try {
					jsonobj.put("userid", ThisAppInstallation.id(this));
					jsonobj.put("latitude", location.getLatitude());
					jsonobj.put("longitude", location.getLongitude());
					jsonobj.put("deslatitude", deslocation.getLatitude());
					jsonobj.put("deslongitude", deslocation.getLongitude());
					jsonobj.put("time", "3:00 pm");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				StringEntity postEntityUser = null;
				try {
					postEntityUser = new StringEntity(jsonobj.toString());
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				postEntityUser.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
				Log.d("debug", "calling server:"+jsonobj.toString());				
			    ServerResponse response = HttpQueries.QueryServer(HttpQueries.QueryMethod.Post, ServerQueries.POST_QUERY, postEntityUser);
			    JSONObject jobj=response.GetJSONResponse();
			    if(jobj != null)
			    	{
			    	Log.d("debug", "server response:"+jobj.toString());
					List<OtherUser> nearbyUsers=OtherUser.GetUsersInfoFromJSONObject(jobj);
					PopulateNearbyUsers(nearbyUsers);
			    	}
				
			//}
			  		
    //	};
		
	}        
    
    private void PopulateNearbyUsers(List<OtherUser> nearbyUsers) {
    	Drawable drawable = this.getResources().getDrawable(R.drawable.red_marker);
		allUsersItemizedOverlay = new AllUsersItemizedOverlay(drawable,this);
		allUsersItemizedOverlay.addAllUserOverlay(nearbyUsers);
		mymapview.getOverlays().add(allUsersItemizedOverlay);	
		mymapview.postInvalidate();
		
	}  

    //test
	public void onPause(){
    	super.onPause();
    	//locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    	myOverlay.disableMyLocation();
    	myOverlay.disableCompass();
    	locationmanager.removeUpdates(this);
    	mymapview.getOverlays().remove(allUsersItemizedOverlay);
    	mymapview.postInvalidate();
    }
    
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		int lati=(int)(location.getLatitude()*1E6);
		int longi=(int)(location.getLongitude()*1E6);
		GeoPoint newpoint=new GeoPoint(lati,longi);
		myMapcontroller.animateTo(newpoint);
		
	}

	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
    
   
}