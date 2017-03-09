package com.mentobile.grabbit.Activity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mentobile.grabbit.GrabbitApplication;
import com.mentobile.grabbit.Model.NearByModel;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.AppPref;
import com.mentobile.grabbit.Utility.AppUrl;
import com.mentobile.grabbit.Utility.BaseActivity;
import com.mentobile.grabbit.Utility.GetDataUsingWService;
import com.mentobile.grabbit.Utility.GetWebServiceData;
import com.mentobile.grabbit.Utility.NetworkErrorActivity;
import com.mentobile.grabbit.Utility.Other;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Administrator on 12/22/2016.
 */

public class BluetoothActivity extends BaseActivity implements GetWebServiceData {

    private static final int LOAD_MERCHANT = 0;
    private static final int MERCHANT_DISTANCE = 1;
    Button Button_bluetooth;

    @Override
    public int getActivityLayout() {
        return R.layout.activity_bluetooth;
    }

    @Override
    public void initialize() {
        getSupportActionBar().hide();
        Button_bluetooth = (Button) findViewById(R.id.Button_bluetooth);
        Button_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decision();
            }
        });

    }

    @Override
    public void init(Bundle save) {

    }

    public void onWindowFocusChanged() {
        if (!Other.isNetworkAvailable(getApplicationContext())) {
            Intent intent = new Intent(BluetoothActivity.this, NetworkErrorActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (!AppPref.getInstance().isLogin()) {
            Intent intent = new Intent(BluetoothActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(BluetoothActivity.this, DrawerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void decision() {
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        final boolean isEnabled = bluetoothAdapter.isEnabled();
        if (!isEnabled) {
            bluetoothAdapter.enable();
            onWindowFocusChanged();
        }
    }

    @Override
    public void getWebServiceResponse(String responseData, int serviceCounter) {
        Log.w("responseData", responseData);
    }
}
