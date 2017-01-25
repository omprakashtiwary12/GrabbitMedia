package com.mentobile.grabbit.Activity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mentobile.grabbit.GrabbitApplication;
import com.mentobile.grabbit.Model.NearByModel;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.AppPref;
import com.mentobile.grabbit.Utility.AppUrl;
import com.mentobile.grabbit.Utility.BaseActivity;
import com.mentobile.grabbit.Utility.GetDataUsingWService;
import com.mentobile.grabbit.Utility.GetWebServiceData;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Administrator on 12/22/2016.
 */

public class BluetoothActivity extends BaseActivity implements GetWebServiceData {
    private static final int LOAD_MERCHANT = 0;
    private static final int MERCHANT_DISTANCE = 1;

    double current_latitude = 0;
    double current_longitude = 0;
    Button Button_bluetooth;

    @Override
    public int getActivityLayout() {
        return R.layout.activity_bluetooth;
    }

    @Override
    public void initialize() {
        getSupportActionBar().hide();
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isNetworkEnabled || isGpsEnabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
                return;
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) == null ? locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) : locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                current_latitude = location.getLatitude();
                current_longitude = location.getLongitude();
            }
        }

        Button_bluetooth = (Button) findViewById(R.id.Button_bluetooth);
        Button_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decision();
            }
        });

    }

    @Override
    public void init(Bundle save) {

    }

    private void decision() {
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        final boolean isEnabled = bluetoothAdapter.isEnabled();
        if (!isEnabled) {
            bluetoothAdapter.enable();
            sendToThisActivity(DrawerActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // loadDataFromServer();
        } else {
//            loadDataFromServer();
            sendToThisActivity(DrawerActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
    }

    public void bluetoothDEsigion() {
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        final boolean isEnabled = bluetoothAdapter.isEnabled();
        if (isEnabled) {
            loadDataFromServer();
        } else {
            Toast.makeText(this, "please Enable Bluetooth", Toast.LENGTH_LONG).show();
        }
    }

    private void loadDataFromServer() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("api_key=").append(AppUrl.API_KEY);
        String content = stringBuilder.toString();
        GetDataUsingWService getDataUsingWService = new GetDataUsingWService(this, AppUrl.MERCHANTS_URL, LOAD_MERCHANT, content, true, "Loading ...", this);
        getDataUsingWService.execute();
    }

    @Override
    public void getWebServiceResponse(String responseData, int serviceCounter) {
        Log.w("responseData", responseData);
        switch (serviceCounter) {
            case LOAD_MERCHANT:
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("details");
                        parseJSONArray(jsonArray);
                    } else if (status.equalsIgnoreCase("0")) {
                        toastMessage(jsonObject.getString("msg"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MERCHANT_DISTANCE:
                parseMerchantDistance(responseData);
                break;
        }

    }

    private void parseMerchantDistance(String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            JSONArray jsonArray = jsonObject.getJSONArray("rows");
            JSONArray jsonArray1 = jsonArray.getJSONObject(0).getJSONArray("elements");
            Log.w("jsonArray1", jsonArray1.toString());
            for (int i = 0; i < jsonArray1.length(); i++) {
                jsonObject = jsonArray1.getJSONObject(i);
                jsonObject = jsonObject.getJSONObject("distance");
                String distance = jsonObject.getString("text");
                GrabbitApplication.nearByModelList.get(i).setDistance(distance);
            }
        } catch (Exception e) {

        }
        sendToThisActivity(DrawerActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }


    private void parseJSONArray(JSONArray jsonArray) throws Exception {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String m_id = jsonObject.getString("m_id");
            String business_name = jsonObject.getString("business_name");
            String email = jsonObject.getString("email");
            String phone = jsonObject.getString("phone");
            String latitude = jsonObject.getString("latitude");
            String longitude = jsonObject.getString("longitude");
            String address = jsonObject.getString("address");
            String about = jsonObject.getString("about");
            String website = jsonObject.getString("website");
            String facebook = jsonObject.getString("facebook");
            String twitter = jsonObject.getString("twitter");
            String Instagram = jsonObject.getString("Instagram");
            String open_time = jsonObject.getString("open_time");
            String close_time = jsonObject.getString("close_time");
            String city_name = jsonObject.getString("city_name");
            String state_name = jsonObject.getString("state_name");
            String business_logo = jsonObject.getString("business_logo");
            String business_banner = jsonObject.getString("business_banner");
            String gallery_img1 = jsonObject.getString("gallery_img1");
            String gallery_img2 = jsonObject.getString("gallery_img2");
            String gallery_img3 = jsonObject.getString("gallery_img3");
            String gallery_img4 = jsonObject.getString("gallery_img4");
            String gallery_img5 = jsonObject.getString("gallery_img5");
            String opening_days = jsonObject.getString("opening_days");
            String cat_name = jsonObject.getString("cat_name");
            String status = jsonObject.getString("status");
            String wishlist = jsonObject.getString("wishlist");

            NearByModel nearByModel = new NearByModel();
            nearByModel.setM_id(m_id);
            nearByModel.setBusiness_name(business_name);
            nearByModel.setEmail(email);
            nearByModel.setPhone(phone);
            nearByModel.setLatitude(latitude);
            nearByModel.setLongitude(longitude);
            nearByModel.setAddress(address);
            nearByModel.setAbout(about);
            nearByModel.setWebsite(website);
            nearByModel.setFacebook(facebook);
            nearByModel.setTwitter(twitter);
            nearByModel.setInstagram(Instagram);
            nearByModel.setOpen_time(open_time);
            nearByModel.setClose_time(close_time);
            nearByModel.setCity_name(city_name);
            nearByModel.setState_name(state_name);
            nearByModel.setGallery_img1(gallery_img1);
            nearByModel.setGallery_img2(gallery_img2);
            nearByModel.setGallery_img3(gallery_img3);
            nearByModel.setGallery_img4(gallery_img4);
            nearByModel.setGallery_img5(gallery_img5);
            nearByModel.setOpening_days(opening_days);
            nearByModel.setWishlist(wishlist);
            GrabbitApplication.nearByModelList.add(nearByModel);
        }
        calcualteDistance();
    }

    private void calcualteDistance() {
        String finalURL = AppUrl.DISTANCE_URL + "&origins=" + current_latitude + "," + current_longitude + "&destinations=";
        for (int i = 0; i < GrabbitApplication.nearByModelList.size(); i++) {
            finalURL += "%7C" + GrabbitApplication.nearByModelList.get(i).getLatitude() + "%2C" + GrabbitApplication.nearByModelList.get(i).getLongitude();
        }
        finalURL += AppUrl.DISTANCE_API_KEY;
        Log.w("finalUrl", finalURL);
        GetDataUsingWService getDataUsingWService = new GetDataUsingWService(this, finalURL, MERCHANT_DISTANCE, "", false, "Loading ...", this);
        getDataUsingWService.execute();
    }
}
