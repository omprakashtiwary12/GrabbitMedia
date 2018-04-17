package com.grabbit.daily_deals;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.grabbit.daily_deals.Activity.NotificationActivity;
import com.grabbit.daily_deals.Database.Database;
import com.grabbit.daily_deals.estimote.BeaconID;
import com.grabbit.daily_deals.estimote.BeaconNotificationsManager;

public class GrabbitApplication extends MultiDexApplication {

    private static final String TAG = "GrabbitApplication";
    private static GrabbitApplication mInstance;
    private BeaconNotificationsManager beaconNotificationsManager;
    private int notificationID = 0;
    public static Database database;
    private RequestQueue mRequestQueue;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        database = new Database(getApplicationContext(), 1);
        beaconNotificationsManager = new BeaconNotificationsManager(this);
    }

    public void setBeaconNotification() {
        Cursor cursor = database.getTableData(Database.TBL_BEACON_OFFERS);
        while (cursor.moveToNext()) {
            int message_id = cursor.getInt(0);
            String message = cursor.getString(1);
            int outlet_id = cursor.getInt(2);
            String title = cursor.getString(3);
            int mazor = cursor.getInt(6);
            int minor = cursor.getInt(7);
            String bconId = cursor.getString(8);
            BeaconID beaconID = new BeaconID(bconId, mazor, minor);
            beaconNotificationsManager.addNotification(beaconID, message, "Bye " + message, message_id, outlet_id);
        }
        beaconNotificationsManager.startMonitoring();
    }

    public void showNotification(String title, String message) {
        Intent resultIntent = new Intent(getApplicationContext(), NotificationActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.notification_transa)
                .setContentTitle(title)
                .setContentText(message)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(resultPendingIntent);
        builder.setColor(getResources().getColor(R.color.colorAccent));

        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID++, builder.build());
    }

    public void sendNotification(String title, String message) {
        if (database != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("message", message);
            contentValues.put("title", title);
            database.insertData(contentValues, Database.TBL_NOTIFICATION);
        }
        Intent intent = new Intent(this, NotificationActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_transa)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        notificationBuilder.setColor(getResources().getColor(R.color.colorAccent));
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


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}