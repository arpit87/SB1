// Copyright 2012 Google Inc. All Rights Reserved.

package my.b1701.SB.TabHelperClasses;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * A base implementation of the {@link CompatTab} interface.
 */
public class CompatTabEclair extends CompatTab {
    private CompatTabListener mCallback;
    private CharSequence mText;
    private Drawable mIcon;
    private Fragment mFragment;

    protected CompatTabEclair(FragmentActivity activity, String tag) {
        super(activity, tag);
    }

    @Override
    public CompatTab setText(int resId) {
        mText = mActivity.getResources().getText(resId);
        return this;
    }

    @Override
    public CompatTab setIcon(int resId) {
        mIcon = mActivity.getResources().getDrawable(resId);
        return this;
    }

    @Override
    public CompatTab setTabListener(CompatTabListener callback) {
        mCallback = callback;
        return this;
    }

    @Override
    public CompatTab setFragment(Fragment fragment) {
        mFragment = fragment;
        return this;
    }

    @Override
    public Fragment getFragment() {
        return mFragment;
    }

    @Override
    public CharSequence getText() {
        return mText;
    }

    @Override
    public Drawable getIcon() {
        return mIcon;
    }

    @Override
    public Object getTab() {
        return null;
    }

    @Override
    public CompatTabListener getCallback() {
        return mCallback;
    }
}
