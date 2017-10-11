package com.mentobile.grabbit.estimote;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Nearable;
import com.estimote.sdk.Region;
import com.mentobile.grabbit.Activity.SplashActivity;
import com.mentobile.grabbit.GrabbitApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BeaconNotificationsManager {

    private static final String TAG = "BeaconNotifications";
    private BeaconManager beaconManager;

    private List<Region> regionsToMonitor = new ArrayList<>();
    private HashMap<String, String> enterMessages = new HashMap<>();
    private HashMap<String, String> exitMessages = new HashMap<>();

    private Context context;
    public BeaconNotificationsManager(Context context) {
        this.context = context;
        beaconManager = new BeaconManager(context);
        beaconManager.setBackgroundScanPeriod(300, 100);
//        beaconManager.setNearableListener(new BeaconManager.NearableListener() {
//            @Override
//            public void onNearablesDiscovered(List<Nearable> list) {
//                Log.d(TAG,"::::::Beacon Color "+list.get(0).currentMotionStateDuration);
//            }
//        });
//
//        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
//            @Override
//            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
//                Log.d(TAG,"::::::Beacon Color "+list.get(0).getProximityUUID());
//                Log.d(TAG,"::::::Beacon Color "+region.getProximityUUID());
//            }
//        });

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                Log.d(TAG, "::::::::::::onEnteredRegion: " + region.getIdentifier());
                String message = enterMessages.get(region.getIdentifier());
                if (message != null) {
                    GrabbitApplication.getInstance().showNotification("Grabbit", message);
                }
            }

            @Override
            public void onExitedRegion(Region region) {
                Log.d(TAG, ":::::::::::onExitedRegion: " + region.getIdentifier());
//                String message = exitMessages.get(region.getIdentifier());
//                if (message != null) {
//                   // showNotification(message);
//                }
            }
        });
    }

    public void addNotification(BeaconID beaconID, String enterMessage, String exitMessage) {
        Region region = beaconID.toBeaconRegion();
        enterMessages.put(region.getIdentifier(), enterMessage);
        exitMessages.put(region.getIdentifier(), exitMessage);
        regionsToMonitor.add(region);
    }

    public void startMonitoring() {
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                for (Region region : regionsToMonitor) {
                    beaconManager.startMonitoring(region);
                }
            }
        });
    }
}
