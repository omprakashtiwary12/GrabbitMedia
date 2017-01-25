package com.mentobile.grabbit.Activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.mentobile.grabbit.Adapter.AdapterForGallery;
import com.mentobile.grabbit.GrabbitApplication;
import com.mentobile.grabbit.Model.NearByModel;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 12/20/2016.
 */


public class GalleryActivity extends BaseActivity implements ViewPager.OnPageChangeListener {


    @Override
    public int getActivityLayout() {
        return R.layout.activity_gallery;
    }

    int position = 0;
    NearByModel nearByModel;
    List<String> profilePhotos = new ArrayList<String>();
    ViewPager mViewPager;

    @Override
    public void initialize() {
        mViewPager = (ViewPager) findViewById(R.id.pager);
        //addDataInViewPager();
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

    private void addDataInViewPager() {
        profilePhotos.add("camera");
        profilePhotos.add("gallery");
        profilePhotos.add("camera");
        profilePhotos.add("gallery");
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
