package com.grabbit.daily_deals.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.grabbit.daily_deals.Utility.AppPref;
import com.grabbit.daily_deals.Utility.BaseActivity;
import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.NetworkErrorActivity;
import com.grabbit.daily_deals.Utility.Other;

/**
 * Created by Gokul on 11/15/2016.
 */

public class SplashActivity extends BaseActivity implements LoadDataFromServer.iGetResponse, View.OnClickListener {

    private static final String TAG = "SplashActivity";

    public final static int RESULT_NETWORK = 100;
    public final static int RESULT_BLUETOOTH = 101;

    private Button btnLogin;
    private Button btnSignup;

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
        }
//        else if (!Other.checkBluetoothConnection()) {
//            Intent intent = new Intent(SplashActivity.this, BluetoothActivity.class);
//            startActivityForResult(intent, RESULT_BLUETOOTH);
//        }
        else {
            new LoadDataFromServer(this, this).startFatching();
        }


        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnSignup.setOnClickListener(this);
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
            if (AppPref.getInstance().isLogin()) {
                Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_login:
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

            case R.id.btn_signup:
                Intent intent1 = new Intent(SplashActivity.this, RegisterActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                break;
        }
    }
}
