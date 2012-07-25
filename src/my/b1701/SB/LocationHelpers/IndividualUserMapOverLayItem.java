package my.b1701.SB.LocationHelpers;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class IndividualUserMapOverLayItem extends OverlayItem{
	
	private String userName;
	private String destination;
	private String time;

	
	public IndividualUserMapOverLayItem(GeoPoint point, String name, String destination) {
		super(point,name,destination);
		this.userName=name;
		this.destination=destination;
	
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String userFullName) {
		this.destination = userFullName;
	}
	
	public String getTime() {
		return time;
	}

	public void setTime(String userStatus) {
		this.time = userStatus;
	}
	

}
