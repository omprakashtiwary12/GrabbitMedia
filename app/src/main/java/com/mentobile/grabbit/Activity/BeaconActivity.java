package com.mentobile.grabbit.Activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.BaseActivity;

import java.util.List;
import java.util.UUID;


public class BeaconActivity extends BaseActivity {

    BeaconManager beaconManager;
    private Region region;
    ListView beacon_list;


    @Override
    public int getActivityLayout() {
        return R.layout.activity_beacon;
    }

    @Override
    public void initialize() {
        setTitle("Near By");
        beaconManager = new BeaconManager(this);
        region = new Region("ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);

        beacon_list = (ListView) findViewById(R.id.beacon_list);
    }

    @Override
    public void init(Bundle save) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                String beaconstring = "";
                if (!list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        Beacon beacon = list.get(i);
                        String macaddress = beacon.getMacAddress() + "\n";
                        String major = beacon.getMajor() + "\n";
                        String minor = beacon.getMinor() + ";";
                        beaconstring += macaddress + major + minor;
                    }
                    String beaconarray[] = beaconstring.split(";");
                    beacon_list.setAdapter(new ArrayAdapter<>(BeaconActivity.this, android.R.layout.simple_list_item_1, beaconarray));

                }
            }
        });

    }


    @Override
    protected void onPause() {
        try {
            beaconManager.stopRanging(region);
        } catch (Exception e) {

        }


        super.onPause();
    }

}
