package com.grabbit.daily_deals.Activity;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.AppPref;
import com.grabbit.daily_deals.Utility.BaseActivity;

/**
 * Created by Administrator on 12/22/2016.
 */

public class BluetoothActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvNoThanks;
    private Button btnBluetoothEnable;

    @Override
    public int getActivityLayout() {
        return R.layout.activity_bluetooth;
    }

    @Override
    public void initialize() {
        getSupportActionBar().hide();
        tvNoThanks = (TextView) findViewById(R.id.bluetooth_no_thanks);
        tvNoThanks.setOnClickListener(this);
        btnBluetoothEnable = (Button) findViewById(R.id.Button_bluetooth);
        btnBluetoothEnable.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bluetooth_no_thanks:
                AppPref.getInstance().setBleEnableStatus("block");
                setResult(SplashActivity.RESULT_BLUETOOTH);
                finish();
                break;
            case R.id.Button_bluetooth:
                decision();
                break;
        }
    }
}
