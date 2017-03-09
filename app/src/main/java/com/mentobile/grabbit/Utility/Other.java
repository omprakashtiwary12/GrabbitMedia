package com.mentobile.grabbit.Utility;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;

import com.mentobile.grabbit.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gokul on 7/28/2016.
 */
public class Other {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static void sendNotification(Context context, String title, String message, Class activity) {

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, activity), PendingIntent.FLAG_UPDATE_CURRENT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.ic_menu_camera)
                .setContentTitle(title)
                .setContentText(message).setContentIntent(contentIntent)
                .setSound(soundUri);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(1, mBuilder.build());
    }

    public static void sendNotification(Context context, String title, String message, Class activity, int icon) {

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, activity), PendingIntent.FLAG_UPDATE_CURRENT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(message).setContentIntent(contentIntent)
                .setSound(soundUri);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(1, mBuilder.build());
    }

    public static void sendToThisActivity(Context context, Class activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }

    public static void sendToThisActivity(Context context, Class activity, int flag) {
        Intent intent = new Intent(context, activity);
        intent.setFlags(flag);
        context.startActivity(intent);
    }

    public static void sendToThisActivity(Context context, Class activity, String s[]) {
        Intent intent = new Intent(context, activity);
        for (int i = 0; i < s.length; i++) {
            String p[] = s[i].split(";");
            intent.putExtra(p[0], p[1]);
        }
        context.startActivity(intent);
    }

    public static void saveDataInSharedPreferences(String userId, String userName, String userEmail, String userPhone) {
        AppPref.getInstance().setUserID(userId);
        AppPref.getInstance().setUserName(userName);
        AppPref.getInstance().setUserEmail(userEmail);
        AppPref.getInstance().setUserMobile(userPhone);

    }

    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static Double[] getCurrentLocation(Activity activity) {
        LocationManager locationManager = (LocationManager) activity.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isNetworkEnabled || isGpsEnabled) {
            if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity.getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        activity,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) == null ? locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) : locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {

                return new Double[]{location.getLatitude(), location.getLongitude()};
            }
        }
        return new Double[]{0.0, 0.0};
    }

    public static boolean checkBluetoothConnection() {
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        final boolean isEnabled = bluetoothAdapter.isEnabled();
        return isEnabled;
    }
}
