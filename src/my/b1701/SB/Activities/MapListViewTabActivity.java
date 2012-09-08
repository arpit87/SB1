package my.b1701.SB.Activities;

import my.b1701.SB.R;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;


public class MapListViewTabActivity extends Activity {
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_list_tabview);
 
       //ActionBar gets initiated
        ActionBar actionbar = getActionBar();
      //Tell the ActionBar we want to use Tabs.
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
      //initiating both tabs and set text to it.
        ActionBar.Tab MapTab = actionbar.newTab().setText("MapView");
        ActionBar.Tab ListTab = actionbar.newTab().setText("ListView");
 
     //create the two fragments we want to use for display content
       // Fragment MapFragment = new MapFragment();
        //Fragment ListFragment = new ListFragment();
 
    //set the Tab listener. Now we can listen for clicks.
        //MapTab.setTabListener(new MyTabsListener(MapFragment));
        //ListTab.setTabListener(new MyTabsListener(MapFragment));
 
   //add the two tabs to the actionbar
        actionbar.addTab(MapTab);
        actionbar.addTab(ListTab);
    }

}
