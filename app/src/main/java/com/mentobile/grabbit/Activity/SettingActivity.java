package com.mentobile.grabbit.Activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.BaseActivity;

/**
 * Created by Gokul on 11/22/2016.
 */
public class SettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    Switch bluetooth, gps, notification;

    BluetoothAdapter bluetoothAdapter;

    private final static int REQUEST_BLUETOOTH = 1;
    private final static int REQUEST_GPS = 2;
    private final static int REQUEST_NOTIFICATION = 3;

    @Override
    public int getActivityLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void initialize() {
        setTitle("Setting");
        bluetooth = (Switch) findViewById(R.id.bluetooth);
        gps = (Switch) findViewById(R.id.gps);
        notification = (Switch) findViewById(R.id.notification);


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                bluetooth.setChecked(true);
            } else {
                bluetooth.setChecked(false);
            }
        }

        bluetooth.setOnCheckedChangeListener(this);
        gps.setOnCheckedChangeListener(this);
        notification.setOnCheckedChangeListener(this);

    }

    @Override
    public void init(Bundle save) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case REQUEST_BLUETOOTH:
                if (resultCode == 0) {
                    bluetooth.setChecked(false);
                } else {
                    bluetooth.setChecked(true);
                }
                break;
            case REQUEST_GPS:
                break;
            case REQUEST_NOTIFICATION:
                break;

        }


    }

    private void turnGPSOn() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    private void turnGPSOff() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (provider.contains("gps")) { //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.bluetooth:
                if (b) {
                    Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(turnOn, REQUEST_BLUETOOTH);
                }
                break;
            case R.id.gps:
                if (b) {
                    turnGPSOn();
                } else {
                    turnGPSOff();
                }
                break;
            case R.id.notification:
                break;
        }
    }
}
