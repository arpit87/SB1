package my.b1701.SB.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import my.b1701.SB.R;

import java.sql.Date;
import java.util.List;

public class HistoryAdapter extends BaseAdapter{

    private List<HistoryItem> historyItemList;
    private LayoutInflater inflater;

    public HistoryAdapter(Activity activity, List<HistoryItem> historyItemList){
        this.historyItemList = historyItemList;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return historyItemList.size();
    }

    @Override
    public HistoryItem getItem(int i) {
        return historyItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryItem historyItem = historyItemList.get(position);
        View view=convertView;
        if(convertView==null) {
            view = inflater.inflate(R.layout.history_row, null);
        }

        TextView sourceLocation = (TextView)view.findViewById(R.id.sourceLocation);
        TextView destinationLocation = (TextView)view.findViewById(R.id.destinationLocation);

        sourceLocation.setText(historyItem.getSourceLocation());
        destinationLocation.setText(historyItem.getDestinationLocation());
        return view;
    }

    public static class HistoryItem {
        String sourceLocation;
        String destinationLocation;
        String timeOfRequest;
        Integer dailyInstantType;
        Integer takeOffer;
        Date date;

        public HistoryItem(String sourceLocation, String destinationLocation, String timeOfRequest, Integer dailyInstantType, Integer takeOffer, Date date) {
            this.sourceLocation = sourceLocation;
            this.destinationLocation = destinationLocation;
            this.timeOfRequest = timeOfRequest;
            this.dailyInstantType = dailyInstantType;
            this.takeOffer = takeOffer;
            this.date = date;
        }

        public String getSourceLocation() {
            return sourceLocation;
        }

        public String getDestinationLocation() {
            return destinationLocation;
        }

        public String getTimeOfRequest() {
            return timeOfRequest;
        }

        public Integer getDailyInstantType() {
            return dailyInstantType;
        }

        public Integer getTakeOffer() {
            return takeOffer;
        }

        public Date getDate() {
            return date;
        }
    }
}

