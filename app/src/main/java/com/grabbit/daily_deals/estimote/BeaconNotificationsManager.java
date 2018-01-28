package com.grabbit.daily_deals.estimote;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.grabbit.daily_deals.Database.Database;
import com.grabbit.daily_deals.GrabbitApplication;
import com.grabbit.daily_deals.Utility.Other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BeaconNotificationsManager {

    private static final String TAG = "BeaconNotifications";
    private BeaconManager beaconManager;

    private List<Region> regionsToMonitor = new ArrayList<>();
    private HashMap<String, String> hashMapEnterMessage = new HashMap<>();
    private HashMap<String, String> hashMapExitMessage = new HashMap<>();
    private HashMap<String, Integer> hashMapOutlet = new HashMap<>();
    private HashMap<String, Integer> hashMapEnterMessageID = new HashMap<>();
    private Context context;

    public BeaconNotificationsManager(Context context) {
        this.context = context;
        beaconManager = new BeaconManager(context);
        beaconManager.setBackgroundScanPeriod(3000, 100);
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
                String message = hashMapEnterMessage.get(region.getIdentifier());
                int message_id1 = hashMapEnterMessageID.get(region.getIdentifier());
                int outlet_id1 = hashMapOutlet.get(region.getIdentifier());
                boolean isExists = GrabbitApplication.database.iSCompaignExits(message_id1, outlet_id1);
                if (message != null && isExists == false) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("message", message);
                    contentValues.put("title", "Grabbit");
                    contentValues.put("out_id", outlet_id1);
                    contentValues.put("msg_id", message_id1);
                    contentValues.put("current_time", Other.getCurrentTime());
                    contentValues.put("notification_dt", Other.getCurrentTimeinFormat());
                    GrabbitApplication.database.insertData(contentValues, Database.TBL_NOTIFICATION);
                    GrabbitApplication.getInstance().showNotification("Grabbit", message);
                }
            }

            @Override
            public void onExitedRegion(Region region) {
                Log.d(TAG, ":::::::::::onExitedRegion: " + region.getIdentifier());
//                String message = hashMapExitMessage.get(region.getIdentifier());
//                if (message != null) {
//                   // showNotification(message);
//                }
            }
        });
    }

    public void addNotification(BeaconID beaconID, String enterMessage, String exitMessage, int msg_id, int out_id) {
        Region region = beaconID.toBeaconRegion();
        hashMapEnterMessage.put(region.getIdentifier(), enterMessage);
        hashMapExitMessage.put(region.getIdentifier(), exitMessage);
        hashMapOutlet.put(region.getIdentifier(), out_id);
        hashMapEnterMessageID.put(region.getIdentifier(), msg_id);
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

    public static void setDatabaseValue() {
        int message_id1 = 7;
        int outlet_id1 = 4;
        String message = "hello world";
        boolean isExists = GrabbitApplication.database.iSCompaignExits(message_id1, outlet_id1);
        if (message != null && isExists == false) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("message", message);
            contentValues.put("title", "Grabbit");
            contentValues.put("out_id", outlet_id1);
            contentValues.put("msg_id", message_id1);
            contentValues.put("current_time", System.currentTimeMillis());
            GrabbitApplication.database.insertData(contentValues, Database.TBL_NOTIFICATION);
            GrabbitApplication.getInstance().showNotification("Grabbit", message);
        }
    }
}
