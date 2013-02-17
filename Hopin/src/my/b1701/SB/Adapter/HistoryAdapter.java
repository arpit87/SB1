package my.b1701.SB.Adapter;

import java.util.List;

import my.b1701.SB.R;
import my.b1701.SB.Activities.SearchInputActivity;
import my.b1701.SB.LocationHelpers.SBGeoPoint;
import my.b1701.SB.Users.ThisUser;
import my.b1701.SB.Util.StringUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HistoryAdapter extends BaseAdapter{

    private List<HistoryItem> historyItemList;
    private LayoutInflater inflater;
    private Activity underlyingActiviy = null;

    public HistoryAdapter(Activity activity, List<HistoryItem> historyItemList){
        this.historyItemList = historyItemList;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.underlyingActiviy = activity;
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
        final HistoryItem historyItem = historyItemList.get(position);
        View view=convertView;
        if(convertView==null) {
            view = inflater.inflate(R.layout.history_list_row, null);
        }
                      
        TextView source = (TextView)view.findViewById(R.id.history_source);
        TextView destination = (TextView)view.findViewById(R.id.history_destination);        
        TextView details = (TextView)view.findViewById(R.id.history_details);     
        ImageView edit_button = (ImageView)view.findViewById(R.id.history_editbutton);
        TextView reqDateView = (TextView)view.findViewById(R.id.history_req_date);
        final String sourceStr = historyItem.getSourceLocation();
        final String dstStr = historyItem.getDestinationLocation();
        String time = historyItem.getTimeOfRequest();//HH:mm 24hr
        int type = historyItem.getDailyInstantType();//daily 0,insta 1
        String freq = historyItem.getFreq(); //yyyy-MM-dd 
        String date = historyItem.getReqDate();
        source.setText(sourceStr);
        destination.setText(dstStr);
        details.setText(freq + "@"+time);
        reqDateView.setText(date);
        edit_button.setOnClickListener(new OnClickListener() {				
			@Override
			public void onClick(View chatIconView) {
				int srclati = historyItem.getSrclatitudee6();
				int srclongi = historyItem.getSrclongitudee6();
				int dstlati = historyItem.getDstlatitudee6();
				int dstlongi = historyItem.getDstlongitudee6();
				//here we are setting src destination bfr hand and then passing intent wid address to set
				//that address is not used by searchinput activity to get src dst
				ThisUser.getInstance().setSourceGeoPoint(new SBGeoPoint(srclati,srclongi), true);
				ThisUser.getInstance().setDestinationGeoPoint(new SBGeoPoint(dstlati,dstlongi), true);
				Intent i = new Intent(underlyingActiviy,SearchInputActivity.class);	
				i.putExtra("source", sourceStr);
				i.putExtra("destination", dstStr);
				i.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
				underlyingActiviy.startActivity(i);
			}
		});
        
	    return view;
    }

    public static class HistoryItem {
        String sourceLocation;
        String destinationLocation;
        String timeOfRequest;
        Integer dailyInstantType;
        Integer takeOffer;
        String freq;
        String reqDate;
        int srclatitudee6;
        int srclongitudee6;
        int dstlatitudee6;
        int dstlongitudee6;

        public HistoryItem(String sourceLocation, String destinationLocation, String timeOfRequest, 
        		           int dailyInstantType, int takeOffer, String freq,String reqdate,
        		           int srclongitude,int srclatitude,int dstlongitude,int dstlatitude) {
            this.sourceLocation = sourceLocation;
            this.destinationLocation = destinationLocation;
            this.timeOfRequest = timeOfRequest;
            this.dailyInstantType = dailyInstantType;
            this.takeOffer = takeOffer;
            this.freq = freq;
            this.reqDate = reqdate;
            this.srclatitudee6 = srclatitude;
            this.srclongitudee6 = srclongitude;
            this.dstlatitudee6 = dstlatitude;
            this.dstlongitudee6 = dstlongitude;
        }

        public String getSourceLocation() {
            return sourceLocation;
        }

        public String getDestinationLocation() {
            return destinationLocation;
        }

        /**
         * 12 hr format
         * @return
         */
        public String getTimeOfRequest() {
            return timeOfRequest;
        }

        public int getDailyInstantType() {
            return dailyInstantType;
        }

        public int getTakeOffer() {
            return takeOffer;
        }

        public String getFreq() {
            return freq;
        }
        
        /**
         * it of format d MMM,ie.  14 Feb
         * @return
         */
        public String getReqDate() {
            return reqDate;
        }

		public int getSrclongitudee6() {
			return srclongitudee6;
		}

		public int getDstlatitudee6() {
			return dstlatitudee6;
		}

		public int getDstlongitudee6() {
			return dstlongitudee6;
		}

		public int getSrclatitudee6() {
			return srclatitudee6;
		}
    }
}

