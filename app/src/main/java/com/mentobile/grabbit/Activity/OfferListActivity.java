package com.mentobile.grabbit.Activity;

import android.os.Bundle;

import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.BaseActivity;

/**
 * Created by Gokul on 11/22/2016.
 */
public class OfferListActivity extends BaseActivity {
    @Override
    public int getActivityLayout() {
        return R.layout.activity_offer_list;
    }

    @Override
    public void initialize() {

        setTitle("Offer List");
    }

    @Override
    public void init(Bundle save) {

    }
}
