package my.b1701.SB.TabHelpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class SherLockActionBarTabListener implements BaseTabListener {

    private final SherlockFragmentActivity mActivity;
    private final Class mClass;
    
    public SherLockActionBarTabListener (SherlockFragmentActivity activity, Class<? extends Fragment> cls) {
    mActivity = activity;
    mClass = cls;
}

/* The following are each of the ActionBar.TabListener callbacks */
public void onTabSelected(BaseTab tab, FragmentTransaction ft) {
    // Check if the fragment is already initialized
    Fragment fragment = tab.getFragment();
    if (fragment == null) {
        // If not, instantiate and add it to the activity
        fragment = Fragment.instantiate(mActivity, mClass.getName());
        tab.setFragment(fragment);
        ft.add(android.R.id.tabcontent, fragment, tab.getTag());
    } else {
        // If it exists, simply attach it in order to show it
        ft.attach(fragment);
    }
}

public void onTabUnselected(BaseTab tab, FragmentTransaction ft) {
    Fragment fragment = tab.getFragment();
    if (fragment != null) {
        // Detach the fragment, because another one is being attached
        ft.detach(fragment);
    }
}

public void onTabReselected(BaseTab tab, FragmentTransaction ft) {
    // User selected the already selected tab. Do nothing.
}
}

