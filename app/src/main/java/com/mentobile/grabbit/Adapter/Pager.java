package com.mentobile.grabbit.Adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mentobile.grabbit.Fragment.BeaconFragment;
import com.mentobile.grabbit.Fragment.NearByFragment;
import com.mentobile.grabbit.Fragment.WishListFragment;

public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class 
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs 
        switch (position) {
            case 0:
                NearByFragment tab1 = new NearByFragment();
                return tab1;
            case 1:
                WishListFragment tab2 = new WishListFragment();
                return tab2;
            case 2:
                BeaconFragment tab3 = new BeaconFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}