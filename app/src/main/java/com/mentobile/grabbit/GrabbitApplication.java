package com.mentobile.grabbit;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.mentobile.grabbit.Activity.DrawerActivity;
import com.mentobile.grabbit.Activity.NotificationActivity;
import com.mentobile.grabbit.Database.Database;
import com.mentobile.grabbit.Utility.AppPref;
import com.mentobile.grabbit.Utility.AppUrl;
import com.mentobile.grabbit.estimote.BeaconID;
import com.mentobile.grabbit.estimote.BeaconNotificationsManager;

public class GrabbitApplication extends Application {

    private static final String TAG = "GrabbitApplication";
    private static GrabbitApplication mInstance;
    private BeaconNotificationsManager beaconNotificationsManager;
    private int notificationID = 0;
    public static Database database;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "::::::Application on Create method");
        mInstance = this;
        database = new Database(getApplicationContext(), 1);
        beaconNotificationsManager = new BeaconNotificationsManager(this);
    }

    public void setBeaconNotification() {
        Cursor cursor = database.getTableData(Database.TBL_BEACON_OFFERS);
        while (cursor.moveToNext()) {
            String bconId = cursor.getString(8);
            int mazor = cursor.getInt(6);
            int minor = cursor.getInt(7);
            String message = cursor.getString(1);
            String title = cursor.getString(3);
            BeaconID beaconID = new BeaconID(bconId, mazor, minor);
            beaconNotificationsManager.addNotification(beaconID, message, "Bye " + message);
            Log.d(TAG, ":::BCon UDID " + mazor + "  Message " + message);
        }
        beaconNotificationsManager.startMonitoring();
    }

    public void showNotification(String title, String message) {

        if (database != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("message",message);
            contentValues.put("title",title);
            database.insertData(contentValues,Database.TBL_NOTIFICATION);
        }
        Intent resultIntent = new Intent(getApplicationContext(), NotificationActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.grabbit)
                .setContentTitle(title)
                .setContentText(message)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(resultPendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID++, builder.build());
    }

    public void sendNotification(String title, String message) {
        if (database != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("message",message);
            contentValues.put("title",title);
            database.insertData(contentValues,Database.TBL_NOTIFICATION);
        }
        Intent intent = new Intent(this, NotificationActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.grabbit)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationID++ /* ID of notification */, notificationBuilder.build());
    }

    public static synchronized GrabbitApplication getInstance() {
        return mInstance;
    }

    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}