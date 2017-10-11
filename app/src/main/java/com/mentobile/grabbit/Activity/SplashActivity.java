package com.mentobile.grabbit.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.mentobile.grabbit.Utility.AppPref;
import com.mentobile.grabbit.Utility.BaseActivity;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.NetworkErrorActivity;
import com.mentobile.grabbit.Utility.Other;

/**
 * Created by Gokul on 11/15/2016.
 */

public class SplashActivity extends BaseActivity implements LoadDataFromServer.iGetResponse {

    private static final String TAG = "SplashActivity";

    public final static int RESULT_NETWORK = 100;
    public final static int RESULT_BLUETOOTH = 101;

    @Override
    public int getActivityLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void initialize() {
        getSupportActionBar().hide();

        if (!Other.isNetworkAvailable(getApplicationContext())) {
            Intent intent = new Intent(SplashActivity.this, NetworkErrorActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(intent, RESULT_NETWORK);
        } else if (!Other.checkBluetoothConnection()) {
            Intent intent = new Intent(SplashActivity.this, BluetoothActivity.class);
            startActivityForResult(intent, RESULT_BLUETOOTH);
        } else {
            new LoadDataFromServer(this, this).startFatching();
        }
    }

    @Override
    public void init(Bundle save) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_BLUETOOTH) {
            new LoadDataFromServer(this, this).startFatching();
        }
    }

    @Override
    public void getLoadDataResponse(boolean isStatus) {
        if (isStatus) {
            if (!AppPref.getInstance().isLogin()) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SplashActivity.this, DrawerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }
    }
}
