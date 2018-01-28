package com.grabbit.daily_deals.Utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by user on 08-08-2016.
 */
public class Constants {

    static ProgressDialog progressDialog;
    static SharedPreferences sharedPreferences;
    static ArrayList<HashMap<String,String>> arrayList;
    static HashMap<String, String> map;
    static JSONObject jsonObject;
    public static Context context, noInterentContext;
    static Geocoder geocoder;
    static List<Address> addresses;
    public static int dataAvailable =0, tripReportIndex, numberOfTrips;
    public static String fromDate, toDate;
    public static GoogleMap googleMap1;
    public static int lmvFlag = 0;

//    public static void customToast(Context context, String msg ){
//        Toast toast =  Toast.makeText(context, "  "+msg+"  ", Toast.LENGTH_SHORT);
//        toast.getView().setBackgroundResource(R.drawable.toast_background);
//        toast.show();
//    }

//    public static void customProgressDailog(Context context, String msg){
//        progressDialog = new ProgressDialog(context,R.style.MyTheme);
//        progressDialog.setMessage(msg);
//        progressDialog.setCancelable(false);
//        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.toast_background);
//        progressDialog.show();
//    }

    public static void cancelDialog(){
        progressDialog.dismiss();
    }

    public static void enterPrefrences(Context context, String key, String value){
        sharedPreferences = context.getSharedPreferences("MyPrefrences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static SharedPreferences getPrefrences(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefrences", Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public static String getPrefrencesValues(Context context, String values){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefrences", Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(values,null);
        return value;
    }

    public static void cleanPrefrences(Context context, String key){
        context.getSharedPreferences("MyPrefrences", Context.MODE_PRIVATE).edit().remove(key).commit();
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void hideKeyboard(View view, Context context){

        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static List<Address> getAddress(Context context){

        geocoder = new Geocoder(context, Locale.getDefault());

        Double latitude = Double.parseDouble(Constants.getPrefrencesValues(context,"latitude"));
        Double longitude = Double.parseDouble(Constants.getPrefrencesValues(context,"longitude"));

        try {
            addresses = geocoder.getFromLocation(latitude,longitude,1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address =  addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knowName = addresses.get(0).getFeatureName();

        return addresses;
    }


    public static void dropPinEffect(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 3000;

        final Interpolator interpolator = new BounceInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                marker.setAnchor(0.5f, 1.0f + 14 * t);

                if (t > 0.0) {
                    // Post again 15ms later.
                    handler.postDelayed(this, 15);
                } else {
                    // marker.showInfoWindow();

                }
            }
        });
    }


    public static Bitmap resizeMapIcons(Context context, int imageId , int width, int height){
        BitmapDrawable bitmapdraw=(BitmapDrawable) ContextCompat.getDrawable(context,imageId);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        return smallMarker;
    }



    public static List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }

    public static boolean isNetworkAvailable (Context context){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
