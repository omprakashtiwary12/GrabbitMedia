package com.mentobile.grabbit.Activity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mentobile.grabbit.Adapter.AdapterIntro;
import com.mentobile.grabbit.Adapter.AdapterViewPager;
import com.mentobile.grabbit.GrabbitApplication;
import com.mentobile.grabbit.Model.NearByModel;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.AppPref;
import com.mentobile.grabbit.Utility.AppUrl;
import com.mentobile.grabbit.Utility.BaseActivity;
import com.mentobile.grabbit.Utility.GetDataUsingWService;
import com.mentobile.grabbit.Utility.GetWebServiceData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gokul on 11/23/2016.
 */
public class TourActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    LinearLayout pager_indicator;
    List<String> profilePhotos = new ArrayList<String>();
    ImageButton btn_next;

    int dotsCount = 0;
    ImageView[] dots;
    private static final int LOAD_MERCHANT = 0;
    private static final int MERCHANT_DISTANCE = 1;

    double current_latitude = 0;
    double current_longitude = 0;

    @Override
    public int getActivityLayout() {
        return R.layout.activity_tour;
    }

    @Override
    public void initialize() {
        getSupportActionBar().hide();
        addDataInViewPager();
        ViewPager viewpager_image = (ViewPager) findViewById(R.id.pager_introduction);
        AdapterIntro adapterViewPager = new AdapterIntro(this, profilePhotos);
        viewpager_image.setAdapter(adapterViewPager);
        viewpager_image.setOnPageChangeListener(this);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        btn_next = (ImageButton) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);
        setUiPageViewController();
    }

    private void addDataInViewPager() {
        profilePhotos.add("camera");
        profilePhotos.add("gallery");
        profilePhotos.add("camera");
        profilePhotos.add("gallery");
    }

    private void setUiPageViewController() {

        dotsCount = profilePhotos.size();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }


    @Override
    public void init(Bundle save) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

        if (position + 1 == dotsCount) {
            btn_next.setVisibility(View.VISIBLE);
        } else {
            btn_next.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                AppPref.getInstance().setTakeATour("Completed");
                sendToThisActivity(LoginActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                break;
        }
    }

}
