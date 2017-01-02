package com.mentobile.grabbit.Activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.mentobile.grabbit.Adapter.AndroidImageAdapter;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.BaseActivity;

/**
 * Created by Gokul on 11/30/2016.
 */
public class ImageActivity extends BaseActivity {
    ImageView image;

    @Override
    public int getActivityLayout() {
        //return R.layout.activity_image;
        return R.layout.activity_gallery;
    }

    @Override
    public void initialize() {
        getSupportActionBar().hide();
//        image = (ImageView) findViewById(R.id.image);
//        image.setImageBitmap(MerchantDetailsActivity.bitmap);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        AndroidImageAdapter adapterView = new AndroidImageAdapter(this);
        mViewPager.setAdapter(adapterView);
    }

    @Override
    public void init(Bundle save) {

    }


}
