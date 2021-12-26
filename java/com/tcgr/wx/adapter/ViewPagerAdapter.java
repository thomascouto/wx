package com.tcgr.wx.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tcgr.wx.ui.fragment.bookmark.BookMetarFragment;
import com.tcgr.wx.ui.fragment.bookmark.BookTafFragment;

/**
 * @see com.tcgr.wx.ui.fragment.TabsFragment
 * Created by thomas on 28/04/16.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private int tabs;

    public ViewPagerAdapter(FragmentManager fm, int tabs) {
        super(fm);
        this.tabs = tabs;
    }

    @Override
    public int getCount() {
        return tabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new BookMetarFragment();
            default:
                return new BookTafFragment();
        }
    }
}
