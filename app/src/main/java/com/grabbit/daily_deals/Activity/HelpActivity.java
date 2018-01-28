package com.grabbit.daily_deals.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.grabbit.daily_deals.Adapter.TabViewPageAdapter;
import com.grabbit.daily_deals.Fragment.HelpLineFragment;
import com.grabbit.daily_deals.Fragment.HospitalFragment;
import com.grabbit.daily_deals.Fragment.PoliceFragment;
import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.BaseActivity;
import com.grabbit.daily_deals.Utility.Other;

/**
 * Created by Gokul on 11/22/2016.
 */
public class HelpActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    private String TAG = "HelpActivity";
    private TabLayout tabLayout;
    //This is our viewPager
    private ViewPager viewPager;
    public double current_latitude = 0.00;
    public double current_longitude = 0.00;

     void onCreate1(Bundle savedInstanceState) {
       // super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_help);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        HospitalFragment hospitalFragment = new HospitalFragment();
        PoliceFragment policeFragment = new PoliceFragment();
        HelpLineFragment helpLineFragment = new HelpLineFragment();

        TabViewPageAdapter tabViewPageAdapter = new TabViewPageAdapter(getSupportFragmentManager());
        tabViewPageAdapter.addFragment(helpLineFragment, "Helpline");
        tabViewPageAdapter.addFragment(hospitalFragment, "Hospital");
        tabViewPageAdapter.addFragment(policeFragment, "Police Station");

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.green));
        viewPager = (ViewPager) findViewById(R.id.pager);
        //Adding adapter to pager
        viewPager.setAdapter(tabViewPageAdapter);

        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);

        Double currentLocation[] = Other.getCurrentLocation(this);
        current_latitude = currentLocation[0];
        current_longitude = currentLocation[1];
        Log.d(TAG, "::::current_latitude " + current_latitude + " ::: current_longitude" + current_longitude);
    }

    @Override
    public int getActivityLayout() {
        return R.layout.activity_help;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void init(Bundle save) {
        onCreate1(save);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}