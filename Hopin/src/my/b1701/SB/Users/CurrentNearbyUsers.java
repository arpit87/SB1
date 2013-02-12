package my.b1701.SB.Users;

import java.util.HashMap;
import java.util.List;

import my.b1701.SB.HelperClasses.JSONHandler;
import my.b1701.SB.HelperClasses.ToastTracker;

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
	private HashMap<String, NearbyUser> FBID_NearbyUserMap = new HashMap<String, NearbyUser>(); //store fbid<-> nearbyuser obj map
	private List<NearbyUser> mCurrentNearbyUserList = null;
	private List<NearbyUser> mNewNearbyUserList = null;
	private static CurrentNearbyUsers instance=new CurrentNearbyUsers();
	public static CurrentNearbyUsers getInstance() {
		 return instance;
	}
	
	public void updateNearbyUsersFromJSON(JSONObject body)
	{		
		//we temporarily put new users in new list and MapHandler has to check if changed and callupdate then we change current to new
		//we return null for 0 users so check for null always while getting nearby users
		Log.i(TAG,"updating nearby users");
		mNewNearbyUserList = JSONHandler.getInstance().GetNearbyUsersInfoFromJSONObject(body);	
		if(mNewNearbyUserList!=null)
			ToastTracker.showToast("new users:"+mNewNearbyUserList.size());
		else
			ToastTracker.showToast("new users 0");
		
	}

	public List<NearbyUser> getAllNearbyUsers()
	{
		return mCurrentNearbyUserList;
	}
	
	public void updateCurrentToNew()
	{
		mCurrentNearbyUserList = mNewNearbyUserList ;
		FBID_NearbyUserMap.clear();
		if(mCurrentNearbyUserList!=null)
		{
			for(NearbyUser n : mCurrentNearbyUserList)
			{
				FBID_NearbyUserMap.put(n.getUserFBInfo().getFbid(), n);
			}
		}		
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
		n = mCurrentNearbyUserList.get(id);
		return n;
	}
	
	public boolean usersHaveChanged()
	{
		Log.i(TAG,"chking if usr changed ");
		if(mCurrentNearbyUserList == null)
		{			
			if(mNewNearbyUserList == null)
				return false; //called before getMatch
			else
				return true; //first time update
		}
		else if(mNewNearbyUserList == null)
		{
			ToastTracker.showToast("users changed to 0");
			return true; //new number of users is 0 but currently we showing some who moved out	
		}
				
		//check for objects inside..we have overriden equals..yiipee
		for(NearbyUser n:mNewNearbyUserList)
		{
			if(mCurrentNearbyUserList.contains(n))
				continue;	
				
			Log.i(TAG,"user have changed ");
			ToastTracker.showToast("users changed");
			return true;
		}		
		Log.i(TAG,"user did not change ");
		ToastTracker.showToast("users not changed");
		return false;
	}
	
	public void clearAllData()
	{
		mCurrentNearbyUserList.clear();
		mNewNearbyUserList.clear();
		FBID_NearbyUserMap.clear();
	}
	
}
