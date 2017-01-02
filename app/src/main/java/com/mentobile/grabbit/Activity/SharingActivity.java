package com.mentobile.grabbit.Activity;

import android.os.Bundle;

import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.BaseActivity;

/**
 * Created by Gokul on 11/22/2016.
 */
public class SharingActivity extends BaseActivity {
    @Override
    public int getActivityLayout() {
        return R.layout.activity_sharing;
    }

    @Override
    public void initialize() {
        setTitle("Sharing");
    }

    @Override
    public void init(Bundle save) {

    }
}
