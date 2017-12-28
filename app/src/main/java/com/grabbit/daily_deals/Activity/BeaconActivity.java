package com.grabbit.daily_deals.Activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.BaseActivity;

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
        region = new Region("ranged region",null, null, null);
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
                        String minor = beacon.getProximityUUID() + ";";
                        beaconstring += macaddress + major + minor;
                    }
                    String beaconarray[] = beaconstring.split(";");
                    //send  to  webservice  this id
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
