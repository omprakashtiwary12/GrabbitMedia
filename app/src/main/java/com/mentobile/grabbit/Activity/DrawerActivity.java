package com.mentobile.grabbit.Activity;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mentobile.grabbit.Adapter.Pager;
import com.mentobile.grabbit.Fragment.NearByFragment;
import com.mentobile.grabbit.GrabbitApplication;
import com.mentobile.grabbit.Model.NearByModel;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.AppPref;
import com.mentobile.grabbit.Utility.AppUrl;
import com.mentobile.grabbit.Utility.GetDataUsingWService;
import com.mentobile.grabbit.Utility.GetWebServiceData;
import com.mentobile.grabbit.Utility.Other;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GetWebServiceData, TabLayout.OnTabSelectedListener {

    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;
    private static final int LOAD_MERCHANT = 0;
    private static final int MERCHANT_DISTANCE = 1;
    double current_latitude = 0;
    double current_longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.view_home_toolbar, null);
        toolbar.addView(mCustomView);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isNetworkEnabled || isGpsEnabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
                return;
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) == null ? locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) : locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                current_latitude = location.getLatitude();
                current_longitude = location.getLongitude();
            }
        }
        //setBluetooth(true);
        loadDataFromServer();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = (View) navigationView.getHeaderView(0);
        setHeaderView(view);

        //setFragment(new NearByFragment());

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("WishList"));
        tabLayout.addTab(tabLayout.newTab().setText("Beacons"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

        //Creating our pager adapter
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
    }

    private void loadDataFromServer() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("api_key=").append(AppUrl.API_KEY);
        String content = stringBuilder.toString();
        GetDataUsingWService getDataUsingWService = new GetDataUsingWService(this, AppUrl.MERCHANTS_URL, LOAD_MERCHANT, content, true, "Loading ...", this);
        getDataUsingWService.execute();
    }

    @Override
    public void getWebServiceResponse(String responseData, int serviceCounter) {
        Log.w("responseData", responseData);
        switch (serviceCounter) {
            case LOAD_MERCHANT:
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("details");
                        parseJSONArray(jsonArray);
                    } else if (status.equalsIgnoreCase("0")) {
                        // toastMessage(jsonObject.getString("msg"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MERCHANT_DISTANCE:
                parseMerchantDistance(responseData);
                break;
        }

    }

    private void parseMerchantDistance(String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            JSONArray jsonArray = jsonObject.getJSONArray("rows");
            JSONArray jsonArray1 = jsonArray.getJSONObject(0).getJSONArray("elements");
            Log.w("jsonArray1", jsonArray1.toString());
            for (int i = 0; i < jsonArray1.length(); i++) {
                jsonObject = jsonArray1.getJSONObject(i);
                jsonObject = jsonObject.getJSONObject("distance");
                String distance = jsonObject.getString("text");
                GrabbitApplication.nearByModelList.get(i).setDistance(distance);
            }
        } catch (Exception e) {

        }
        // sendToThisActivity(DrawerActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }


    private void parseJSONArray(JSONArray jsonArray) throws Exception {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String m_id = jsonObject.getString("m_id");
            String business_name = jsonObject.getString("business_name");
            String email = jsonObject.getString("email");
            String phone = jsonObject.getString("phone");
            String latitude = jsonObject.getString("latitude");
            String longitude = jsonObject.getString("longitude");
            String address = jsonObject.getString("address");
            String about = jsonObject.getString("about");
            String website = jsonObject.getString("website");
            String facebook = jsonObject.getString("facebook");
            String twitter = jsonObject.getString("twitter");
            String Instagram = jsonObject.getString("Instagram");
            String open_time = jsonObject.getString("open_time");
            String close_time = jsonObject.getString("close_time");
            String city_name = jsonObject.getString("city_name");
            String state_name = jsonObject.getString("state_name");
            String business_logo = jsonObject.getString("business_logo");
            String business_banner = jsonObject.getString("business_banner");
            String gallery_img1 = jsonObject.getString("gallery_img1");
            String gallery_img2 = jsonObject.getString("gallery_img2");
            String gallery_img3 = jsonObject.getString("gallery_img3");
            String gallery_img4 = jsonObject.getString("gallery_img4");
            String gallery_img5 = jsonObject.getString("gallery_img5");
            String opening_days = jsonObject.getString("opening_days");
            String cat_name = jsonObject.getString("cat_name");
            String status = jsonObject.getString("status");
            String wishlist = jsonObject.getString("wishlist");

            NearByModel nearByModel = new NearByModel();
            nearByModel.setM_id(m_id);
            nearByModel.setBusiness_name(business_name);
            nearByModel.setEmail(email);
            nearByModel.setPhone(phone);
            nearByModel.setLatitude(latitude);
            nearByModel.setLongitude(longitude);
            nearByModel.setAddress(address);
            nearByModel.setAbout(about);
            nearByModel.setWebsite(website);
            nearByModel.setFacebook(facebook);
            nearByModel.setTwitter(twitter);
            nearByModel.setInstagram(Instagram);
            nearByModel.setOpen_time(open_time);
            nearByModel.setClose_time(close_time);
            nearByModel.setCity_name(city_name);
            nearByModel.setState_name(state_name);
            nearByModel.setBusiness_logo(business_logo);
            nearByModel.setBusiness_banner(business_banner);
            nearByModel.setGallery_img1(gallery_img1);
            nearByModel.setGallery_img2(gallery_img2);
            nearByModel.setGallery_img3(gallery_img3);
            nearByModel.setGallery_img4(gallery_img4);
            nearByModel.setGallery_img5(gallery_img5);
            nearByModel.setOpening_days(opening_days);
            nearByModel.setCat_name(cat_name);
            nearByModel.setStatus(status);
            nearByModel.setWishlist(wishlist);
            GrabbitApplication.nearByModelList.add(nearByModel);
        }


        calcualteDistance();
    }

    private void calcualteDistance() {
        String finalURL = AppUrl.DISTANCE_URL + "&origins=" + current_latitude + "," + current_longitude + "&destinations=";
        for (int i = 0; i < GrabbitApplication.nearByModelList.size(); i++) {
            finalURL += "%7C" + GrabbitApplication.nearByModelList.get(i).getLatitude() + "%2C" + GrabbitApplication.nearByModelList.get(i).getLongitude();
        }
        finalURL += AppUrl.DISTANCE_API_KEY;
        Log.w("finalUrl", finalURL);
        GetDataUsingWService getDataUsingWService = new GetDataUsingWService(this, finalURL, MERCHANT_DISTANCE, "", false, "Loading ...", this);
        getDataUsingWService.execute();
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

        Picasso.with(this).load(AppUrl.PROFILE_PIC_URL + AppPref.getInstance().getUserID() + ".jpg").into(userimage);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
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
//            case R.id.offer_list:
//                Other.sendToThisActivity(this, OfferListActivity.class);
//                break;
//            case R.id.near_by:
//                Other.sendToThisActivity(this, BeaconActivity.class);
//                break;
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
                Other.sendToThisActivity(this, HelpActivity.class);
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
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
               // Toast.makeText(this, "", Toast.LENGTH_LONG).show();
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
