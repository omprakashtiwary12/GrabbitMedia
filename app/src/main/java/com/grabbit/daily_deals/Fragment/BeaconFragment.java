package com.grabbit.daily_deals.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.grabbit.daily_deals.Activity.BeaconActivity;
import com.grabbit.daily_deals.R;

import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 1/3/2017.
 */

public class BeaconFragment extends Fragment {
    BeaconManager beaconManager;
    private Region region;
    ListView beacon_list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_beacon, container, false);
        beaconManager = new BeaconManager(getActivity());
        region = new Region("ranged region",
                null, null, null);

        beacon_list = (ListView) view.findViewById(R.id.beacon_list);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(getActivity());

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
                        String macaddress = beacon.getProximityUUID() + "\n";
                        String major = beacon.getMajor() + "\n";
                        String minor = beacon.getMinor() + ";";
                        beaconstring += macaddress + major + minor;
                    }
                    String beaconarray[] = beaconstring.split(";");
                    //send  to  webservice  this id
                    beacon_list.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, beaconarray));
                }
            }
        });

    }


    @Override
    public void onPause() {
        try {
            beaconManager.stopRanging(region);
        } catch (Exception e) {

        }
        super.onPause();
    }
}
