package com.example.android.map;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by David Rosas on 9/27/2018.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private int mNoOfTabs;

    public PagerAdapter(FragmentManager manager, int tabs) {
        super(manager);
        mNoOfTabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new LocationReviewsFragment();
            case 1:
                return new LocationPhotosFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
