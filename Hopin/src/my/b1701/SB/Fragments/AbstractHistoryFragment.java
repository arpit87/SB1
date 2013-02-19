package my.b1701.SB.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import my.b1701.SB.ActivityHandlers.MapListActivityHandler;
import my.b1701.SB.Adapter.HistoryAdapter;
import my.b1701.SB.HelperClasses.ProgressHandler;
import my.b1701.SB.HttpClient.AddThisUserSrcDstRequest;
import my.b1701.SB.HttpClient.SBHttpClient;
import my.b1701.SB.HttpClient.SBHttpRequest;
import my.b1701.SB.LocationHelpers.SBGeoPoint;
import my.b1701.SB.R;
import my.b1701.SB.Users.ThisUser;
import my.b1701.SB.Util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHistoryFragment extends ListFragment {
    public static final String TAG = "my.b1701.SB.Fragments.AbstractHistoryFragment";

    HistoryAdapter adapter;
	View mListViewContainer;
	TextView mEmptyListTextView;
	List<HistoryAdapter.HistoryItem> historyList;

    public abstract int getDailyInstantType();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        historyList = fetchHistory();
        adapter = new HistoryAdapter(getActivity(), historyList);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if(adapter!=null)
        {
            HistoryAdapter.HistoryItem historyItem = adapter.getItem(position);
            CreateRequestFromHistory asyncReq = new CreateRequestFromHistory();
            asyncReq.execute(historyItem);
            getActivity().finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView( inflater, container, null );
        mListViewContainer = inflater.inflate(R.layout.historyfrag_listview, null);
        mEmptyListTextView = (TextView)mListViewContainer.findViewById(R.id.history_emptyList);
        if(historyList.isEmpty())
        {
            mEmptyListTextView.setVisibility(View.VISIBLE);
        }
        return mListViewContainer;
    }

    protected List<HistoryAdapter.HistoryItem> fetchHistory() {
        List<HistoryAdapter.HistoryItem> historyItems = new ArrayList<HistoryAdapter.HistoryItem>();
        List<HistoryAdapter.HistoryItem> historyItemList = ThisUser.getInstance().getHistoryItemList();
        for (HistoryAdapter.HistoryItem historyItem : historyItemList) {
            if (historyItem.getDailyInstantType() == getDailyInstantType()){
                historyItems.add(historyItem);
            }
        }
        return historyItems;
    }
    
    protected class CreateRequestFromHistory extends AsyncTask<HistoryAdapter.HistoryItem, Void, Void> {

        @Override
        protected Void doInBackground(HistoryAdapter.HistoryItem... historyItems) {
            HistoryAdapter.HistoryItem historyItem = historyItems[0];
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
            ThisUser.getInstance().set_Daily_Instant_Type(getDailyInstantType());//0 daily pool,1 instant share
            ThisUser.getInstance().set_Take_Offer_Type(takeride);//0 take ,1 offer

            Log.i(TAG, "Executing query");
            SBHttpRequest addThisUserSrcDstRequest= new AddThisUserSrcDstRequest();
            SBHttpClient.getInstance().executeRequest(addThisUserSrcDstRequest);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressHandler.showInfiniteProgressDialoge(MapListActivityHandler.getInstance().getUnderlyingActivity(), "Fetching users", "Please wait..");
        }
    }
}
