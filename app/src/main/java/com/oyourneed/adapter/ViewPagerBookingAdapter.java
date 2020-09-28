package com.oyourneed.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.oyourneed.activity.FiltersActivity;
import com.oyourneed.fragment.RefineFragment;
import com.oyourneed.fragment.FiltersFragment;


public class ViewPagerBookingAdapter extends FragmentStatePagerAdapter {

    private FiltersActivity context;
    private int tabCount;

    public ViewPagerBookingAdapter(FragmentManager fm, int tabCount, FiltersActivity context) {
        super(fm);
        this.tabCount = tabCount;
        this.context = context;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                RefineFragment tab3 = new RefineFragment(context);
                return tab3;

            case 1:
                FiltersFragment tab1 = new FiltersFragment(context);
                return tab1;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "Booking";
            case 1:
                return "Enquiry";
            default:
                return null;
        }
    }
}
