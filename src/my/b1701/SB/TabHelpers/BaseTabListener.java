package my.b1701.SB.TabHelpers;

import android.support.v4.app.FragmentTransaction;

/**
 * @see android.app.ActionBar.TabListener
 */
public interface BaseTabListener {
    /**
     * @see android.app.ActionBar.TabListener#onTabSelected(
     *android.app.ActionBar.Tab, android.app.FragmentTransaction)
     */
    public void onTabSelected(BaseTab tab, FragmentTransaction ft);

    /**
     * @see android.app.ActionBar.TabListener#onTabUnselected(
     *android.app.ActionBar.Tab, android.app.FragmentTransaction)
     */
    public void onTabUnselected(BaseTab tab, FragmentTransaction ft);

    /**
     * @see android.app.ActionBar.TabListener#onTabReselected(
     *android.app.ActionBar.Tab, android.app.FragmentTransaction)
     */
    public void onTabReselected(BaseTab tab, FragmentTransaction ft);
}
