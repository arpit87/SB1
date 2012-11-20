package my.b1701.SB.ActivityHandlers;

import java.util.List;

import my.b1701.SB.R;
import my.b1701.SB.HelperClasses.SBImageLoader;
import my.b1701.SB.Users.NearbyUser;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NearbyUsersListViewAdapter extends BaseAdapter{

	List mNearbyUsers;
	Activity underLyingActivity;
	private static LayoutInflater inflater=null;
	public NearbyUsersListViewAdapter(Activity activity,List<NearbyUser> nearbyUsers)
	{
		underLyingActivity = activity;
		mNearbyUsers = nearbyUsers;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mNearbyUsers.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NearbyUser thisUser = (NearbyUser) mNearbyUsers.get(position);
		View thisUserView=convertView;
        if(convertView==null)
        	thisUserView = inflater.inflate(R.layout.nearbyuser_list_row, null);
        ImageView userImageView = (ImageView)thisUserView.findViewById(R.id.nearbyuser_list_image); 
        TextView userName = (TextView)thisUserView.findViewById(R.id.nearbyusername);
        TextView userDestination = (TextView)thisUserView.findViewById(R.id.nearbyuserdestination);
        TextView userDistance = (TextView)thisUserView.findViewById(R.id.nearbyuserdistance);
        TextView userLatitude = (TextView)thisUserView.findViewById(R.id.nearbyuserlatitude);
        TextView userLongitudee = (TextView)thisUserView.findViewById(R.id.nearbyuserlongitude);
        
        SBImageLoader.getInstance().displayImageElseStub(thisUser.getUserFBInfo().getImageURL(), userImageView, R.id.userpic);
        userName.setText(thisUser.getUserFBInfo().getName());
        userDestination.setText(thisUser.getUserLocInfo().getUserDestination());
        userLatitude.setText(thisUser.getUserLocInfo().getSrcLatitude());
        userLatitude.setText(thisUser.getUserLocInfo().getSrcLongitude());
		
		return thisUserView;
	}

}
