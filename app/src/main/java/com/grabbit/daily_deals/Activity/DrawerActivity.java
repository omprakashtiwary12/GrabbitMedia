package com.grabbit.daily_deals.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.grabbit.daily_deals.Fragment.NearByFragment;
import com.grabbit.daily_deals.Fragment.WishListFragment;
import com.grabbit.daily_deals.GrabbitApplication;
import com.grabbit.daily_deals.Model.NearByModel;
import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.AppPref;
import com.grabbit.daily_deals.Utility.Other;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TabLayout.OnTabSelectedListener, View.OnClickListener {

    private static final String TAG = "DrawerActivity";
    private TabLayout tabLayout;
    //This is our viewPager
    private ViewPager viewPager;

    public static int cat_id;
    public static List<NearByModel> nearByModelList = new ArrayList<NearByModel>();

    private Button btnFilterBy;
    private Button btnSortBy;

    private NearByFragment nearByFragment;
    private WishListFragment wishListFragment;
    public String filterString = "0";
    public String sortedBy = "distance";

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);

//        nearByModelList = new LoadDataFromServer().getMerchantList(sortedBy, filterString);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = (View) navigationView.getHeaderView(0);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Other.sendToThisActivity(DrawerActivity.this, MyProfileActivity.class);
            }
        });

        setHeaderView(view);
        //color  for  icon
        navigationView.setItemIconTintList(null);
        //setFragment(new NearByFragment());
        //Initializing the tablayout
//        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //Adding the tabs using addTab() method

        //Initializing viewPager
//        viewPager = (ViewPager) findViewById(R.id.pager);

        nearByFragment = new NearByFragment();
//        wishListFragment = new WishListFragment();
        //Creating our pager adapter
//        TabViewPageAdapter tabViewPageAdapter = new TabViewPageAdapter(getSupportFragmentManager());
//        tabViewPageAdapter.addFragment(nearByFragment, "Deals for You");
       // tabViewPageAdapter.addFragment(wishListFragment, "Wishlist");

        //Adding adapter to pager
//        viewPager.setAdapter(tabViewPageAdapter);

        //Adding onTabSelectedListener to swipe views
//        tabLayout.setOnTabSelectedListener(this);
//        tabLayout.setupWithViewPager(viewPager);

//        btnFilterBy = (Button) findViewById(R.id.activity_drawer_filter_by);
//        btnFilterBy.setOnClickListener(this);
//
//        btnSortBy = (Button) findViewById(R.id.activity_drawer_sort_by);
//        btnSortBy.setOnClickListener(this);
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
            /*case R.id.about_us:
                Other.sendToThisActivity(this, AboutUsActivity.class);
                break;*/
            case R.id.contact_us:
                Other.sendToThisActivity(this, ContactUsActivity.class);
                break;
            case R.id.sharing:
                Other.sendToThisActivity(this, SharingActivity.class);
                break;
            case R.id.notification:
                Other.sendToThisActivity(this, NotificationActivity.class);
                break;
            case R.id.feedback:
                Other.sendToThisActivity(this, FeedbackPageActivity.class);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_noti);
        LayerDrawable layerDrawable = (LayerDrawable) menuItem.getIcon();
        int mNotificationCount = GrabbitApplication.database.getNotificationCount();
        Other.setBadgeCount(getApplicationContext(), layerDrawable, mNotificationCount);
        return true;
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_noti:
                Other.sendToThisActivity(DrawerActivity.this, NotificationActivity.class);
                break;
            case R.id.action_search:
                Intent intent1 = new Intent(DrawerActivity.this, CategoryActivity.class);
                intent1.setFlags(100);
                startActivityForResult(intent1, 100);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "::::onActivityResult" + resultCode);
        if (resultCode == 100) {
//            Log.d(TAG, "::::onActivityResult");
//            cat_id = data.getIntExtra("pos", 1);
//            nearByModelList = new LoadDataFromServer().getMerchantList(cat_id, true, "ASC");
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.push_up_in, R.anim.push_up_out, R.anim.push_down_in, R.anim.push_down_out);
        switch (v.getId()) {
            case R.id.activity_drawer_sort_by:
//                transaction.replace(android.R.id.content, new SortByFragment());
                viewShortedValueDialog();
                break;
            case R.id.activity_drawer_filter_by:
//                transaction.replace(android.R.id.content, new FilterByFragment());
                viewFilterValueDialog();
                break;
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void viewShortedValueDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_sort_by);
        RadioButton radioButtonAToZ = (RadioButton) dialog.findViewById(R.id.radio_btn_atoz);
        radioButtonAToZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortedBy = "ASC";
//                nearByModelList = new LoadDataFromServer().getMerchantList(sortedBy, filterString);
                nearByFragment.refreshAdapter();
                dialog.dismiss();
            }
        });

        RadioButton radioButtonZToA = (RadioButton) dialog.findViewById(R.id.radio_btn_ztoa);
        radioButtonZToA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortedBy = "DESC";
//                nearByModelList = new LoadDataFromServer().getMerchantList(sortedBy, filterString);
                nearByFragment.refreshAdapter();
                dialog.dismiss();
            }
        });

        RadioButton radioButtonDistance = (RadioButton) dialog.findViewById(R.id.radio_btn_distance);
        radioButtonDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortedBy = "distance";
//                nearByModelList = new LoadDataFromServer().getMerchantList(sortedBy, filterString);
                nearByFragment.refreshAdapter();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void viewFilterValueDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_filter_by);

        final CheckBox checkBox_Event = (CheckBox) dialog.findViewById(R.id.checkbox_event);
        final CheckBox checkBox_Electric = (CheckBox) dialog.findViewById(R.id.checkbox_electric);
        final CheckBox checkBox_Hospitility = (CheckBox) dialog.findViewById(R.id.checkbox_hospitility);
        final CheckBox checkBox_Retails = (CheckBox) dialog.findViewById(R.id.checkbox_retails);
        final CheckBox checkBox_Saloon = (CheckBox) dialog.findViewById(R.id.checkbox_saloon);
        final CheckBox checkBox_Travel = (CheckBox) dialog.findViewById(R.id.checkbox_travel);

        Button buttonApply = (Button) dialog.findViewById(R.id.filter_apply);
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterString = "";
                if (checkBox_Event.isChecked()) {
                    filterString += "3,";
                }
                if (checkBox_Electric.isChecked()) {
                    filterString += "6,";
                }
                if (checkBox_Hospitility.isChecked()) {
                    filterString += "1,";
                }
                if (checkBox_Retails.isChecked()) {
                    filterString += "2,";
                }
                if (checkBox_Saloon.isChecked()) {
                    filterString += "5,";
                }
                if (checkBox_Travel.isChecked()) {
                    filterString += "4,";
                }
//                filterString = filterString.substring(0, filterString.length() - 1);
//                nearByModelList = new LoadDataFromServer().getMerchantList("", filterString);
//                nearByFragment.refreshAdapter();
//                dialog.dismiss();
                Log.d(TAG, "::::Filter Value " + filterString);
            }
        });

        Button buttonClear = (Button) dialog.findViewById(R.id.filter_clear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterString = "0";
//                nearByModelList = new LoadDataFromServer().getMerchantList(sortedBy, filterString);
//                nearByFragment.refreshAdapter();
//                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
