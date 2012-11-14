/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package my.b1701.SB.TabHelpers;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class SherlockActionBarTab extends BaseTab implements ActionBar.TabListener {

    ActionBar.Tab mTab;
    BaseTabListener mCallback;
    Fragment mFragment;

    public SherlockActionBarTab(SherlockFragmentActivity activity, String tag) {
        super(activity, tag);
        mTab = activity.getSupportActionBar().newTab();
    }

    @Override
    public BaseTab setText(int resId) {
        mTab.setText(resId);
        return this;
    }

    @Override
    public BaseTab setIcon(int resId) {
        mTab.setIcon(resId);
        return this;
    }

    @Override
    public BaseTab setTabListener(BaseTabListener callback) {
        mCallback = callback;
        mTab.setTabListener(this);
        return this;
    }

    @Override
    public CharSequence getText() {
        return mTab.getText();
    }

    @Override
    public Drawable getIcon() {
        return mTab.getIcon();
    }

    @Override
    public Object getTab() {
        return mTab;
    }

    @Override
    public BaseTabListener getCallback() {
        return mCallback;
    }

    @Override
    public BaseTab setFragment(Fragment fragment) {
        mFragment = fragment;
        return this;
    }

    @Override
    public Fragment getFragment() {
        return mFragment;
    }

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		  ft = mActivity.getSupportFragmentManager().beginTransaction();
	        ft.disallowAddToBackStack();
	        mCallback.onTabSelected(this, ft);
	        ft.commit();
		
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		  ft = mActivity.getSupportFragmentManager().beginTransaction();
	        ft.disallowAddToBackStack();
	        mCallback.onTabUnselected(this, ft);
	        ft.commit();
		
	}

	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		  ft = mActivity.getSupportFragmentManager().beginTransaction();
	        ft.disallowAddToBackStack();
	        mCallback.onTabReselected(this, ft);
	        ft.commit();
		
	}

}