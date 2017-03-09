package com.mentobile.grabbit.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.estimote.sdk.SystemRequirementsChecker;
import com.mentobile.grabbit.Adapter.Pager;
import com.mentobile.grabbit.Adapter.TabViewPageAdapter;
import com.mentobile.grabbit.Fragment.BeaconFragment;
import com.mentobile.grabbit.Fragment.NearByFragment;
import com.mentobile.grabbit.Fragment.WishListFragment;
import com.mentobile.grabbit.GrabbitApplication;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.AppPref;
import com.mentobile.grabbit.Utility.Other;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TabLayout.OnTabSelectedListener {

    private TabLayout tabLayout;
    //This is our viewPager
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = (View) navigationView.getHeaderView(0);
        setHeaderView(view);

        //color  for  icon
        navigationView.setItemIconTintList(null);
        //setFragment(new NearByFragment());

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //Adding the tabs using addTab() method
//        tabLayout.addTab(tabLayout.newTab().setText("All"));
//        tabLayout.addTab(tabLayout.newTab().setText("WishList"));
        //tabLayout.addTab(tabLayout.newTab().setText("Beacons"));

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

        //Creating our pager adapter
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());

        TabViewPageAdapter tabViewPageAdapter = new TabViewPageAdapter(getSupportFragmentManager());
        tabViewPageAdapter.addFragment(new NearByFragment(), "All");
        tabViewPageAdapter.addFragment(new WishListFragment(), "Wishlist");

        //Adding adapter to pager
        viewPager.setAdapter(tabViewPageAdapter);

        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);

        GrabbitApplication.getInstance().setBeaconNotification();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void setHeaderView(View view) {
        TextView username = (TextView) view.findViewById(R.id.user_name);
        TextView useremail = (TextView) view.findViewById(R.id.user_email);
        CircularImageView userimage = (CircularImageView) view.findViewById(R.id.user_photo);

        username.setText(AppPref.getInstance().getUserName());
        useremail.setText(AppPref.getInstance().getUserEmail());
        //Picasso.with(this).load(AppUrl.PROFILE_PIC_URL + AppPref.getInstance().getUserID() + ".jpg").into(userimage);
        try {
            Picasso.with(this).load(AppPref.getInstance().getImageUrl()).into(userimage);
        } catch (Exception e) {
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.my_profile:
                Other.sendToThisActivity(this, MyProfileActivity.class);
                break;

            case R.id.about_us:
                Other.sendToThisActivity(this, AboutUsActivity.class);
                break;
            case R.id.contact_us:
                Other.sendToThisActivity(this, ContactUsActivity.class);
                break;
            case R.id.sharing:
                Other.sendToThisActivity(this, SharingActivity.class);
                break;
            case R.id.setting:
                Other.sendToThisActivity(this, SettingActivity.class);
                break;
            case R.id.help:
                Other.sendToThisActivity(this, BeaconActivity.class);

                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_sort:
                Other.sendToThisActivity(DrawerActivity.this, CategoryActivity.class);
                break;
            case R.id.menu_noti:
                Other.sendToThisActivity(DrawerActivity.this, NotificationActivity.class);
                break;
            default:
                break;
        }
        return true;
    }
}
