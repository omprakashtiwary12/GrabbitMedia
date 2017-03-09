package com.mentobile.grabbit;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.mentobile.grabbit.Database.Database;
import com.mentobile.grabbit.estimote.BeaconID;
import com.mentobile.grabbit.estimote.BeaconNotificationsManager;

public class GrabbitApplication extends Application {

    private static final String TAG = "GrabbitApplication";
    private static GrabbitApplication mInstance;
    private BeaconNotificationsManager beaconNotificationsManager;

    public static Database database;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        database = new Database(getApplicationContext(), 1);
        beaconNotificationsManager = new BeaconNotificationsManager(this);
        setBeaconNotification();
    }

    public void setBeaconNotification() {
        Cursor cursor = database.getTableData(Database.TBL_BEACON_NOTIFICATION);
        while (cursor.moveToNext()) {
            String bconId = cursor.getString(7);
            int mazor = cursor.getInt(5);
            int minor = cursor.getInt(6);
            String message = cursor.getString(1);
            BeaconID beaconID = new BeaconID(bconId, mazor, minor);
            beaconNotificationsManager.addNotification(beaconID, message, "");
            Log.d(TAG, ":::BCon UDID " + mazor + "  Message " + message);
        }
        beaconNotificationsManager.startMonitoring();
    }

    public static synchronized GrabbitApplication getInstance() {
        return mInstance;
    }

    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

}