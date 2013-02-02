package my.b1701.SB.Fragments;

import java.util.List;

import my.b1701.SB.Activities.MapListViewTabActivity;
import my.b1701.SB.ActivityHandlers.ChatHandler;
import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.ActivityHandlers.NearbyUsersListViewAdapter;
import my.b1701.SB.ChatClient.ChatWindow;
import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.Platform.Platform;
import my.b1701.SB.Users.CurrentNearbyUsers;
import my.b1701.SB.Users.NearbyUser;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class SBListFragment extends ListFragment {
	
	private static final String TAG = "my.b1701.SB.Fragments.SBListFragment";
	private ViewGroup mListViewContainer;
	private List<NearbyUser> nearbyUserlist = null;
	
	
	@Override
	public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
		//update listview
        Log.i(TAG,"on create list view");
        nearbyUserlist = CurrentNearbyUsers.getInstance().getAllNearbyUsers();
        if(nearbyUserlist!=null)
        {
			NearbyUsersListViewAdapter adapter = new NearbyUsersListViewAdapter(getActivity(), nearbyUserlist);
			setListAdapter(adapter);
			Log.i(TAG,"nearby users:"+nearbyUserlist.toString());
        }
        ((MapListViewTabActivity)getActivity()).setListFrag(this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i(TAG,"oncreateview listview");
		mListViewContainer=  ((MapListViewTabActivity)getActivity()).getThisListContainerWithListView();
		return mListViewContainer;
	}
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
		NearbyUser userAtthisPosition = CurrentNearbyUsers.getInstance().getNearbyUserAtPosition(position);
		if(userAtthisPosition != null)
			ChatHandler.getInstance().onChatClickWithUser(userAtthisPosition.getUserFBInfo().getFbid());
		else
			ToastTracker.showToast("Unable to chat,user not in current list");
        ToastTracker.showToast("Chat with user at: " + position);
    }
	
	@Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG,"ondestroyview listview");
        ViewGroup parentViewGroup = (ViewGroup) mListViewContainer.getParent();
		if( null != parentViewGroup ) {
			parentViewGroup.removeView( mListViewContainer );
		}
		//mListViewContainer.removeAllViews();
    }  
	

}
