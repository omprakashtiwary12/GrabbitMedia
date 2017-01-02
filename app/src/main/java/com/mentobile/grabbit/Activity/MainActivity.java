package com.mentobile.grabbit.Activity;

import android.os.Bundle;
import android.view.View;

import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.BaseActivity;

/**
 * Created by Gokul on 11/23/2016.
 */
public class MainActivity extends BaseActivity {
    @Override
    public int getActivityLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void init(Bundle save) {

    }

    public void login(View view) {
        sendToThisActivity(LoginActivity.class);
    }

    public void tour(View view) {
        sendToThisActivity(TourActivity.class);
    }
}
