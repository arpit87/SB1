package my.b1701.SB.Fragments;

import my.b1701.SB.HelperClasses.ToastTracker;
import my.b1701.SB.R;
//import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SBListFragment extends ListFragment {
	
	 public static final String[] TITLES = 
		    {
		            "Henry IV (1)",   
		            "Henry V",
		            "Henry VIII",       
		            "Richard II",
		            "Richard III",
		            "Merchant of Venice",  
		            "Othello",
		            "King Lear"
		    };
	
	@Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        
        // Populate list with our static array of titles.
        setListAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.nearbyusertextview,TITLES
                ));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.nearbyuserlistview, container, false);
	}
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ToastTracker.showToast("Item clicked: " + id);
    }
	

}
