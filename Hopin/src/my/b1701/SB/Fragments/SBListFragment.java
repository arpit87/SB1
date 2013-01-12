package my.b1701.SB.Fragments;

import java.util.List;

import my.b1701.SB.Activities.MapListViewTabActivity;
import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.ActivityHandlers.NearbyUsersListViewAdapter;
import my.b1701.SB.ChatClient.ChatWindow;
import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.Platform.Platform;
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
	
	private static final String TAG = "SBListFragment";
	private ViewGroup mListViewContainer;
	private List<NearbyUser> nearbyUserlist = null;
	
	
	@Override
	public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
		//update listview
        Log.i(TAG,"on create list view");
        nearbyUserlist = MapListActivityHandler.getInstance().getNearbyUserList();
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
		/*Intent startChatIntent = new Intent(Platform.getInstance().getContext(),ChatWindow.class);					
		startChatIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP
	 			| Intent.FLAG_ACTIVITY_NEW_TASK);
		startChatIntent.putExtra("participant", mUserFBID);
		context.startActivity(startChatIntent);*/
        ToastTracker.showToast("Item clicked: " + id);
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
