package my.b1701.SB.Fragments;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import my.b1701.SB.R;
import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.Adapter.HistoryAdapter;
import my.b1701.SB.Adapter.HistoryAdapter.HistoryItem;
import my.b1701.SB.HelperClasses.ProgressHandler;
import my.b1701.SB.HelperClasses.ThisUserConfig;

import my.b1701.SB.HttpClient.AddThisUserSrcDstRequest;
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

public class HistoryInstaShareFragment extends ListFragment{
	private static final String TAG = "my.b1701.SB.Activites.HistoryInstaShareFragment";
	HistoryAdapter adapter = null;
	View mListViewContainer = null;
	TextView mEmptyListTextView = null;
	List<HistoryAdapter.HistoryItem> InstaHistoryList = null;

    @Override
	public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        InstaHistoryList = fetchInstaHistory();        
    	adapter = new HistoryAdapter(getActivity(),InstaHistoryList );
    	setListAdapter(adapter);
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
    		String date = historyItem.getFreq(); //this is Today,T+1...
    		String travelDate = StringUtils.getDateFromTplusString(date, "yyyy-MM-dd");

    		ThisUser.getInstance().setSourceGeoPoint(new SBGeoPoint(srclati,srclongi), true);
			ThisUser.getInstance().setDestinationGeoPoint(new SBGeoPoint(dstlati,dstlongi), true);
			ThisUser.getInstance().setTimeOfRequest(time24hr);
			ThisUser.getInstance().setDateOfRequest(travelDate);
			ThisUser.getInstance().setDateOfRequest(StringUtils.gettodayDateInFormat("yyyy-MM-dd"));
			ThisUser.getInstance().set_Daily_Instant_Type(1);//0 daily pool,1 instant share
			ThisUser.getInstance().set_Take_Offer_Type(takeride);//0 take ,1 offer
			
			Log.i(TAG, "from daily pool history..querying server");			
			SBHttpRequest addThisUserSrcDstRequest= new AddThisUserSrcDstRequest();
	        SBHttpClient.getInstance().executeRequest(addThisUserSrcDstRequest);
    		getActivity().finish();
    	}
    }
    
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView( inflater, container, null );
		Log.i(TAG,"oncreateview historylistview");
		mListViewContainer = inflater.inflate(R.layout.historyfrag_listview, null);
		mEmptyListTextView = (TextView)mListViewContainer.findViewById(R.id.history_emptyList);
		if(InstaHistoryList.isEmpty())
        {        	
        	mEmptyListTextView.setVisibility(View.VISIBLE);        	
        }
		return mListViewContainer;
	}

    private List<HistoryAdapter.HistoryItem> fetchInstaHistory() {
        List<HistoryItem> historyItems = new ArrayList<HistoryItem>();
        List<HistoryItem> historyItemList = ThisUser.getInstance().getHistoryItemList();
        for (HistoryItem historyItem : historyItemList) {
            if (historyItem.getDailyInstantType() == 1){
                historyItems.add(historyItem);
            }
        }
        return historyItems;
    }

}
