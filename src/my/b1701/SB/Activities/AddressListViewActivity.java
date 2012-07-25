package my.b1701.SB.Activities;

import java.io.IOException;
import java.util.List;

import my.b1701.SB.R;
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
	
	String searchText=null;
	Geocoder myGeocoder;
	final static int MAX_RESULT = 5;
	TextView listResult;
	final static String DEFAULT_SEARCH="India";
	EditText searchEditText;
	Button searchButton;
	ListView listviewResult;
	
	public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);	      	
	      setContentView(R.layout.addresslist);
	      Bundle addressBundle=getIntent().getExtras();   	      
	      
	      if (addressBundle == null) {
	    		Log.d("debug","no address string");
	    		searchText = DEFAULT_SEARCH;
	    		}
	      else
	        {
	        	searchText = addressBundle.getString("addressString");	        	
	        }
	      
	      searchEditText = (EditText)findViewById(R.id.searchedittext);
	      searchEditText.setText(searchText);
	      searchButton = (Button)findViewById(R.id.searchbutton);
	      listviewResult = (ListView)findViewById(R.id.list);	      
	      searchButton.setOnClickListener(searchButtonOnClickListener);
	      listviewResult.setOnItemClickListener(listviewResultOnItemClickListener);
	      
	      myGeocoder = new Geocoder(this);	      
	      searchFromLocationName(searchText);
 
	    
	  }
	
	OnItemClickListener listviewResultOnItemClickListener
	  = new OnItemClickListener(){
	 
	  public void onItemClick(AdapterView<?> parent, View view, int position,
	    long id) {
	   	    
	   double lat = ((Address)parent.getItemAtPosition(position)).getLatitude();
	   double lon = ((Address)parent.getItemAtPosition(position)).getLongitude();
	   Intent intent = new Intent();
	   Bundle desBundle = new Bundle();
	   desBundle.putDouble("deslatitude", lat);
	   desBundle.putDouble("deslongitude", lon);	   
	   intent.putExtras(desBundle);
	   setResult(1, intent);
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
