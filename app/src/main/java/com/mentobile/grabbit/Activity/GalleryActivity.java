package com.mentobile.grabbit.Activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.mentobile.grabbit.Adapter.AndroidImageAdapter;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.BaseActivity;

/**
 * Created by Administrator on 12/20/2016.
 */


public class GalleryActivity extends BaseActivity {


    @Override
    public int getActivityLayout() {
        return R.layout.activity_gallery;
    }

    @Override
    public void initialize() {
        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        AndroidImageAdapter adapterView = new AndroidImageAdapter(this);
        mViewPager.setAdapter(adapterView);
    }

    @Override
    public void init(Bundle save) {


    }
}
