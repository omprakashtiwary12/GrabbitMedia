package com.mentobile.grabbit.Activity;

import android.os.Bundle;
import android.widget.TextView;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.BaseActivity;

/**
 * Created by Gokul on 11/22/2016.
 */
public class AboutUsActivity extends BaseActivity {

    private TextView about_us;

    @Override
    public int getActivityLayout() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initialize() {
        setTitle("About Us");
        about_us = (TextView) findViewById(R.id.about_us);
        // about_us.setText("");
    }

    @Override
    public void init(Bundle save) {

    }
}
