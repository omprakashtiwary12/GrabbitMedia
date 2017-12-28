package com.grabbit.daily_deals.Activity;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.BaseActivity;

/**
 * Created by Administrator on 12/22/2016.
 */

public class BluetoothActivity extends BaseActivity {

    @Override
    public int getActivityLayout() {
        return R.layout.activity_bluetooth;
    }

    @Override
    public void initialize() {
        getSupportActionBar().hide();
        Button Button_bluetooth = (Button) findViewById(R.id.Button_bluetooth);
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

    private void decision() {
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        final boolean isEnabled = bluetoothAdapter.isEnabled();
        if (!isEnabled) {
            bluetoothAdapter.enable();
            setResult(SplashActivity.RESULT_BLUETOOTH);
            finish();
        }
    }
}
