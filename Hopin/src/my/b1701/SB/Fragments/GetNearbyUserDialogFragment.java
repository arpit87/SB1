package my.b1701.SB.Fragments;

import my.b1701.SB.R;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class GetNearbyUserDialogFragment extends DialogFragment{
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View dialogView = inflater.inflate(R.layout.getuser_request_dialog, container);
        //Spinner timeSpinner = (Spinner)dialogView.findViewById(R.id.timespinner);  
        
        String times[]={"6:00 PM","7:00 PM","8:00 PM"};
        
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.time_spinner_item,times);
        // Specify the layout to use when the list of choices appears
        //adapter.setDropDownViewResource(R.layout.time_spinner_item);
        // Apply the adapter to the spinner
       // timeSpinner.setAdapter(adapter);
        
        Button cancel = (Button)dialogView.findViewById(R.id.cancelenterusernamebutton);
		// if button is clicked, close the custom dialog
        cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		Button enterButton = (Button) dialogView.findViewById(R.id.enterusernamebutton);
		// if button is clicked, close the custom dialog
		enterButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				dismiss();
				
			}
		});
        
		return dialogView;
	}
}
	
	/*public class TimeSpinnerAdapter extends ArrayAdapter<String>{
		
		Time now ;
		String timeText;
		String hour;
		String minute;
		LayoutInflater m_LayoutInflator;
		
        public TimeSpinnerAdapter(Context context, int textViewResourceId,
                String[] objects,LayoutInflater layoutInflator) {
            super(context, textViewResourceId, objects);
            m_LayoutInflator = layoutInflator;
            now = new Time(Time.getCurrentTimezone());
            now.setToNow();
            timeText = now.format("HH:mm:ss");
            hour = timeText.substring(0, 1);
            minute = timeText.substring(3, 4);
        }

        @Override
        public View getDropDownView(int position, View convertView,
                ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            //return super.getView(position, convertView, parent);

            View row=m_LayoutInflator.inflate(R.layout.time_spinner_item, parent, false);
            TextView timeTextView = (TextView)row.findViewById(R.id.timespinnertextview);
            timeTextView.setText(timeText);
            return row;
        }    
    }
}*/