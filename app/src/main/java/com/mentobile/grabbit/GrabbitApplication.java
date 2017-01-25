package com.mentobile.grabbit;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.mentobile.grabbit.Activity.BeaconActivity;
import com.mentobile.grabbit.Activity.NotificationActivity;
import com.mentobile.grabbit.Database.NotificationDatabase;
import com.mentobile.grabbit.Model.NearByModel;
import com.mentobile.grabbit.Utility.AppPref;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GrabbitApplication extends Application {

    private BeaconManager beaconManager;
    public static List<NearByModel> nearByModelList = new ArrayList<NearByModel>();

    private static GrabbitApplication mInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        beaconManager = new BeaconManager(getApplicationContext());

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region("monitored region",
                        null, null, null));
            }
        });

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                UUID beacon = list.get(0).getProximityUUID();
                if (AppPref.getInstance().getNotification().equalsIgnoreCase("1") || AppPref.getInstance().getNotification().equalsIgnoreCase("")) {
                    NotificationDatabase notificationDatabase = new NotificationDatabase(getApplicationContext());
                    notificationDatabase.open();
                    int i = 0;
                    Cursor cursor = notificationDatabase.getOffer(beacon.toString());
                    if (cursor.moveToFirst()) {
                        do {
                            i++;
                            String text = cursor.getString(1);
                            String title = cursor.getString(2);
                            String uuid = cursor.getString(4);
                            if (uuid.equalsIgnoreCase(beacon.toString())) {
                                showNotification(title, text);
                            }
                        }
                        while (cursor.moveToNext());
                    }
                    cursor.close();
                    notificationDatabase.close();
                }
            }

            @Override
            public void onExitedRegion(Region region) {
                // could add an "exit" notification too if you want (-:
            }
        });
    }


    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, NotificationActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
        NotificationDatabase notificationDatabase = new NotificationDatabase(this);
        notificationDatabase.open();
        notificationDatabase.insert(message, title, 0 + "");
        notificationDatabase.close();
    }

    public static synchronized GrabbitApplication getInstance() {
        return mInstance;
    }

    public Context getApplicationContext() {
        return super.getApplicationContext();
    }


}