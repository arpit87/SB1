package my.b1701.SB.LocationHelpers;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import java.util.Iterator;
import java.util.List;

import my.b1701.SB.R;
import my.b1701.SB.Activities.MyMapView;
import my.b1701.SB.Activities.UserPopUpActivity;
import my.b1701.SB.Users.OtherUser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;

public class AllUsersItemizedOverlay extends ItemizedOverlay<IndividualUserMapOverLayItem>{
	

	private ArrayList<IndividualUserMapOverLayItem> allUserListOverlay = new ArrayList<IndividualUserMapOverLayItem>();	
	private MyMapView myMapActivity;	
	
	public AllUsersItemizedOverlay(Drawable defaultMarker,MyMapView mapactivity) {
		  super(boundCenterBottom(defaultMarker));		   		   		   
		   this.myMapActivity=mapactivity;		 
		}
	
	public void addAllUserOverlay(List<OtherUser> allUsers) {		
		Iterator<OtherUser> it = allUsers.iterator();
		while(it.hasNext() )
		{
			OtherUser u = it.next();
			IndividualUserMapOverLayItem overlayItem=new IndividualUserMapOverLayItem(u.GetUserGeopoint(),u.getUsername(),u.getUserDestination());
			allUserListOverlay.add(overlayItem);
			populate();
		}	    
		
	}
	

	@Override
	protected IndividualUserMapOverLayItem createItem(int i) {
		// TODO Auto-generated method stub
		return allUserListOverlay.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return allUserListOverlay.size();
	}
	
	
	@Override
	protected boolean onTap(int i) {
		 IndividualUserMapOverLayItem user = (IndividualUserMapOverLayItem) getItem(i);
		 
		 Intent userPopUpIntent=new Intent(this.myMapActivity,UserPopUpActivity.class);
		 
		 userPopUpIntent.putExtra("username", user.getUserName());
		 userPopUpIntent.putExtra("destination", user.getDestination());	
		 
		 myMapActivity.startActivity(userPopUpIntent);
		 
         // set data
         //UserPopUpActivity UserPopUpActivity=new UserPopUpActivity(myMapActivity,(ViewGroup)mapView.getParent());
         //UserPopUpActivity.setUserDetails(user.getUserName(), user.getDestination());

         // show popup
         //UserPopUpActivity.show();

         return true;
		
		
    }
	
		
	

		

}

//public class AllUsersOverLay extends ItemizedOverlay<OverlayItem>
