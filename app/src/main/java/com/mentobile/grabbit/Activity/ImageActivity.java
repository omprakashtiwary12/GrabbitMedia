package com.mentobile.grabbit.Activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.mentobile.grabbit.Adapter.AdapterForGallery;
import com.mentobile.grabbit.GrabbitApplication;
import com.mentobile.grabbit.Model.NearByModel;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gokul on 11/30/2016.
 */
public class ImageActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    int position = 0;
    NearByModel nearByModel;
    List<String> profilePhotos = new ArrayList<String>();
    ViewPager mViewPager;

    @Override
    public int getActivityLayout() {
        return R.layout.activity_gallery;
    }

    @Override
    public void initialize() {
        getSupportActionBar().hide();
        mViewPager = (ViewPager) findViewById(R.id.pager);
    }

    @Override
    public void init(Bundle save) {
        save = getIntent().getExtras();
        if (save != null) {
            position = Integer.parseInt(save.getString("position"));
            nearByModel = GrabbitApplication.nearByModelList.get(position);
            profilePhotos.add("http://grabbit.co.in/merchant/uploads/" + nearByModel.getM_id() + "/gallery1.png");
            profilePhotos.add("http://grabbit.co.in/merchant/uploads/" + nearByModel.getM_id() + "/gallery2.png");
            profilePhotos.add("http://grabbit.co.in/merchant/uploads/" + nearByModel.getM_id() + "/gallery3.png");
            AdapterForGallery adapterViewPager = new AdapterForGallery(this, profilePhotos);
            mViewPager.setAdapter(adapterViewPager);
            mViewPager.setOnPageChangeListener(this);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
