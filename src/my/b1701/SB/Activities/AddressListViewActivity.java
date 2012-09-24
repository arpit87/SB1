package my.b1701.SB.Activities;

import java.io.IOException;
import java.util.List;

import my.b1701.SB.R;
import my.b1701.SB.HttpClient.GetUsersRequest;
import my.b1701.SB.HttpClient.SBHttpClient;
import my.b1701.SB.HttpClient.SBHttpRequest;
import my.b1701.SB.LocationHelpers.LocationService;
import my.b1701.SB.LocationHelpers.SBGeoPoint;
import my.b1701.SB.Server.ServerResponseBase;
import my.b1701.SB.Users.ThisUser;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AddressListViewActivity extends Activity{
	
	private static final String TAG = "AddressListViewActivity";
	String searchText=null;
	Geocoder myGeocoder;
	final static int MAX_RESULT = 5;
	TextView listResult;
	final static String DEFAULT_SEARCH="India";
	EditText searchEditText;
	Button searchButton;
	ListView listviewResult;
	//AddressListViewActivityHandler activityHandler;
	
	public void onCreate(Bundle savedInstanceState) {		
	      super.onCreate(savedInstanceState);	      	
	      setContentView(R.layout.addresslist);
	      Log.i(TAG,"started addresslist activity");
	 	  searchText = DEFAULT_SEARCH;   
	      searchEditText = (EditText)findViewById(R.id.searchedittext);	     
	      searchButton = (Button)findViewById(R.id.searchbutton);
	      listviewResult = (ListView)findViewById(R.id.list);	      
	      //activityHandler = AddressListViewActivityHandler.getInstance();
	      //activityHandler.setUnderlyingActivity(this);
	      searchButton.setOnClickListener(searchButtonOnClickListener);
	      listviewResult.setOnItemClickListener(listviewResultOnItemClickListener);
	      myGeocoder = new Geocoder(this);
	  }
	
	OnItemClickListener listviewResultOnItemClickListener
	  = new OnItemClickListener(){
	 
	  public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
	   Log.i(TAG,"address item clicked");
	   int lat = (int)(((Address)parent.getItemAtPosition(position)).getLatitude()*1e6);
	   int lon = (int)(((Address)parent.getItemAtPosition(position)).getLongitude()*1e6);
	   ThisUser.getInstance().setShareReqGeoPoint();		
	   ThisUser.getInstance().setDestinationGeoPoint(new SBGeoPoint(lat,lon));
	   Log.i(TAG,"user desti set..querying server");
	   SBHttpRequest request = new GetUsersRequest();
	   ServerResponseBase response = SBHttpClient.getInstance().executeRequest(request);
	   Log.i(TAG,"got response ,processing");
	   response.process();	   
	   Log.i(TAG,"processed response,finish activity n staring service");
	   Intent startLocService = new Intent(AddressListViewActivity.this,LocationService.class);
	   startService(startLocService);
	   finish();	   
	 }};
	
	  Button.OnClickListener searchButtonOnClickListener
	  = new Button.OnClickListener(){	 
	 public void onClick(View view) { 
	  String searchString = searchEditText.getText().toString();
	  searchFromLocationName(searchString);
	 }};

	
	 private void searchFromLocationName(String name){
		 try {
		  List<Address> result
		  = myGeocoder.getFromLocationName(name, MAX_RESULT);
		  
		  if ((result == null)||(result.isEmpty())){
		   Toast.makeText(this,
		     "No matches were found or there is no backend service!",
		     Toast.LENGTH_LONG).show();
		  }else{
		 
			  AddresslistArrayAdapter adapter = new AddresslistArrayAdapter(this,
		         android.R.layout.simple_list_item_1, result);
		   listviewResult.setAdapter(adapter);
		   
		   Toast.makeText(this,
		     "Finished!",
		     Toast.LENGTH_LONG).show();
		  }
		 
		  
		 } catch (IOException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
		  Toast.makeText(this,
		    "The network is unavailable or any other I/O problem occurs!",
		    Toast.LENGTH_LONG).show();
		 }
		}


public class AddresslistArrayAdapter extends ArrayAdapter<Address> {
	
	private final Context mycontext;
	private final List<Address> addressList;
	
    class ViewHolder {
		public TextView text;
		public ImageView image;
	}
	
	 public AddresslistArrayAdapter(Context context, int textViewResourceId, List<Address> objects) {
	  super(context, textViewResourceId, objects);
	  // TODO Auto-generated constructor stub
	  addressList=objects;
	  mycontext = context;
	 }
	 
	 @Override
	 public View getView(int position, View convertView, ViewGroup parent) {
	  // TODO Auto-generated method stub
	  
	  int maxAddressLineIndex = getItem(position).getMaxAddressLineIndex();
	  String addressLine = "";
	  
	  for (int j = 0; j <= maxAddressLineIndex; j++){
	   addressLine += getItem(position).getAddressLine(j) + ",";
	  }
	  
	  TextView rowAddress = new TextView(mycontext);
	  rowAddress.setText(addressLine);
	  
	  return rowAddress;
	 
	 }
	 
}
}
