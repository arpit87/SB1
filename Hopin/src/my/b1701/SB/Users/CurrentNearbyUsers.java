package my.b1701.SB.Users;

import java.util.HashMap;
import java.util.List;

import my.b1701.SB.HelperClasses.JSONHandler;

import org.json.JSONObject;

import android.util.Log;

/****
 * 
 * @author arpit87
 * this maintains a list of all nearby users from most recent getMatchingUsers request
 *
 */
public class CurrentNearbyUsers {
	
	private static String TAG = "my.b1701.SB.Users.CurrentNearbyUsers" ;
	private HashMap<String, NearbyUser> FBID_NearbyUserMap = new HashMap<String, NearbyUser>();
	private List<NearbyUser> nearbyUserList = null;
	private static CurrentNearbyUsers instance=new CurrentNearbyUsers();
	public static CurrentNearbyUsers getInstance() {
		 return instance;
	}
	
	public void updateNearbyUsersFromJSON(JSONObject body)
	{		
		Log.i(TAG,"updating enarby users");
		nearbyUserList = JSONHandler.getInstance().GetNearbyUsersInfoFromJSONObject(body);	
		FBID_NearbyUserMap.clear();
		for(NearbyUser n : nearbyUserList)
		{
			FBID_NearbyUserMap.put(n.getUserFBInfo().getFbid(), n);
		}
	}

	public List<NearbyUser> getAllNearbyUsers()
	{
		return nearbyUserList;
	}
	
	public NearbyUser getNearbyUserWithFBID(String FBid)
	{
		NearbyUser n;
		n = FBID_NearbyUserMap.get(FBid);
		return n;
	}
	
	public NearbyUser getNearbyUserAtPosition(int id)
	{
		NearbyUser n;
		n = nearbyUserList.get(id);
		return n;
	}
	
	
}
