package my.b1701.SB.Fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import my.b1701.SB.R;
import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.Adapter.HistoryAdapter;
import my.b1701.SB.Adapter.HistoryAdapter.HistoryItem;
import my.b1701.SB.HelperClasses.ProgressHandler;
import my.b1701.SB.HttpClient.AddThisUserScrDstCarPoolRequest;
import my.b1701.SB.HttpClient.SBHttpClient;
import my.b1701.SB.HttpClient.SBHttpRequest;
import my.b1701.SB.LocationHelpers.SBGeoPoint;
import my.b1701.SB.Users.ThisUser;
import my.b1701.SB.Util.StringUtils;
import my.b1701.SB.provider.HistoryContentProvider;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class HistoryDailyPoolFragment extends ListFragment{
	private static final String TAG = "my.b1701.SB.Activites.HistoryDailyPoolFragment";
	HistoryAdapter adapter = null;
	View mListViewContainer = null;
	TextView mEmptyListTextView = null;
	List<HistoryAdapter.HistoryItem> dailyPoolHistoryList = null;

    @Override
	public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        dailyPoolHistoryList = fetchDailyPoolHistory();        
		adapter = new HistoryAdapter(getActivity(),dailyPoolHistoryList );
		setListAdapter(adapter);
        
    }
    
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView( inflater, container, null );
		Log.i(TAG,"oncreateview historylistview");
		mListViewContainer = inflater.inflate(R.layout.historyfrag_listview, null);
		mEmptyListTextView = (TextView)mListViewContainer.findViewById(R.id.history_emptyList);
		if(dailyPoolHistoryList.isEmpty())
        {
        	mEmptyListTextView.setVisibility(View.VISIBLE);
        }
		return mListViewContainer;
	}

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	if(adapter!=null)
    	{
    		ProgressHandler.showInfiniteProgressDialoge(MapListActivityHandler.getInstance().getUnderlyingActivity(), "Fetching users", "Please wait..");
    		HistoryItem historyItem = adapter.getItem(position);
    		int srclati = historyItem.getSrclatitudee6();
    		int srclongi = historyItem.getSrclongitudee6();
    		int dstlati = historyItem.getDstlatitudee6();
    		int dstlongi = historyItem.getDstlongitudee6();
    		String time = historyItem.getTimeOfRequest(); //its 12 hr hh:MM AM/PM
    		String time24hr = StringUtils.formatDate("hh:mm a", "HH:mm", time);
    		int takeride = historyItem.getTakeOffer();

    		ThisUser.getInstance().setSourceGeoPoint(new SBGeoPoint(srclati,srclongi), true);
			ThisUser.getInstance().setDestinationGeoPoint(new SBGeoPoint(dstlati,dstlongi), true);
			ThisUser.getInstance().setTimeOfRequest(time24hr);
			ThisUser.getInstance().setDateOfRequest(StringUtils.gettodayDateInFormat("yyyy-MM-dd"));
			ThisUser.getInstance().set_Daily_Instant_Type(0);//0 daily pool,1 instant share
			ThisUser.getInstance().set_Take_Offer_Type(takeride);//0 take ,1 offer
			
			Log.i(TAG, "from daily pool history..querying server");
			
			SBHttpRequest addThisUserSrcDstRequest= new AddThisUserScrDstCarPoolRequest();
	        SBHttpClient.getInstance().executeRequest(addThisUserSrcDstRequest);
	        getActivity().finish();
    	}
    }
    private List<HistoryAdapter.HistoryItem> fetchDailyPoolHistory() {
        List<HistoryItem> historyItems = new ArrayList<HistoryItem>();
        List<HistoryItem> historyItemList = ThisUser.getInstance().getHistoryItemList();
        for (HistoryItem historyItem : historyItemList) {
            if (historyItem.getDailyInstantType() == 0){
                historyItems.add(historyItem);
            }
        }
        return historyItems;
    }

}
