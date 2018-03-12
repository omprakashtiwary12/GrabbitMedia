package com.grabbit.daily_deals.estimote;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;
import com.grabbit.daily_deals.Database.Database;
import com.grabbit.daily_deals.GrabbitApplication;
import com.grabbit.daily_deals.Utility.Other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BeaconNotificationsManager {

    private static final String TAG = "BeaconNotifications";
    private BeaconManager beaconManager;

    private List<BeaconRegion> regionsToMonitor = new ArrayList<>();
    private HashMap<String, String> hashMapEnterMessage = new HashMap<>();
    private HashMap<String, String> hashMapExitMessage = new HashMap<>();
    private HashMap<String, Integer> hashMapOutlet = new HashMap<>();
    private HashMap<String, Integer> hashMapEnterMessageID = new HashMap<>();
    private Context context;

    public BeaconNotificationsManager(Context context) {
        this.context = context;
        beaconManager = new BeaconManager(context);
        beaconManager.setBackgroundScanPeriod(1000, 0);
        beaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener() {
            @Override
            public void onEnteredRegion(BeaconRegion beaconRegion, List<Beacon> beacons) {
                Log.d(TAG, "::::::::::::onEnteredRegion: " + beaconRegion.getIdentifier());
                String message = hashMapEnterMessage.get(beaconRegion.getIdentifier());
                int message_id1 = hashMapEnterMessageID.get(beaconRegion.getIdentifier());
                int outlet_id1 = hashMapOutlet.get(beaconRegion.getIdentifier());
                boolean isExists = GrabbitApplication.database.iSCompaignExits(message_id1, outlet_id1);
                if (message != null && !isExists) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("message", message);
                    contentValues.put("title", "Grabbit");
                    contentValues.put("out_id", outlet_id1);
                    contentValues.put("msg_id", message_id1);
                    contentValues.put("current_time", Other.getCurrentTime());
                    contentValues.put("notification_dt", Other.getCurrentTimeinFormat());
                    GrabbitApplication.database.insertData(contentValues, Database.TBL_NOTIFICATION);
                    GrabbitApplication.getInstance().showNotification("Grabbit", message);
                } else {
                    GrabbitApplication.getInstance().showNotification("Grabbit", message);
                }
            }

            @Override
            public void onExitedRegion(BeaconRegion beaconRegion) {
                //                String message = hashMapExitMessage.get(region.getIdentifier());
//                if (message != null) {
//                   // showNotification(message);
//                }

            }
        });
    }

    public void addNotification(BeaconID beaconID, String enterMessage, String exitMessage, int msg_id, int out_id) {
        BeaconRegion region = beaconID.toBeaconRegion();
        hashMapEnterMessage.put(region.getIdentifier(), enterMessage);
        hashMapExitMessage.put(region.getIdentifier(), exitMessage);
        hashMapOutlet.put(region.getIdentifier(), out_id);
        hashMapEnterMessageID.put(region.getIdentifier(), msg_id);
        regionsToMonitor.add(region);
    }

//    public void startMonitoring() {
//        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
//            @Override
//            public void onServiceReady() {
//                for (Region region : regionsToMonitor) {
//                    beaconManager.startMonitoring(region);
//                }
//            }
//        });
//    }

    public void startMonitoring() {
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                for (BeaconRegion region : regionsToMonitor) {
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
