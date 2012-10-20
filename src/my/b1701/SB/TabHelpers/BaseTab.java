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

import com.actionbarsherlock.app.ActionBar.Tab;

import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Represents a single tab.
 * The {@link TabHelper} initializes one of the subclasses of this based
 * on the current platform version, upon call to {@link TabHelper#newTab(String)}()
 */
public abstract class BaseTab {
    final FragmentActivity mActivity;
    final String mTag;

    protected BaseTab(FragmentActivity activity, String tag) {
        mActivity = activity;
        mTag = tag;
    }

    public abstract BaseTab setText(int resId);
    public abstract BaseTab setIcon(int resId);
    public abstract BaseTab setTabListener(BaseTabListener callback);
    public abstract BaseTab setFragment(Fragment fragment);

    public abstract CharSequence getText();
    public abstract Drawable getIcon();
    public abstract BaseTabListener getCallback();
    public abstract Fragment getFragment();

    public abstract Object getTab();

    public String getTag() {
        return mTag;
    }
	
}
