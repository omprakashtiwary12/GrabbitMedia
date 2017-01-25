package com.mentobile.grabbit.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.mentobile.grabbit.Database.NotificationDatabase;
import com.mentobile.grabbit.GrabbitApplication;
import com.mentobile.grabbit.Model.NearByModel;
import com.mentobile.grabbit.Model.OfferImageModel;
import com.mentobile.grabbit.Utility.AppPref;
import com.mentobile.grabbit.Utility.AppUrl;
import com.mentobile.grabbit.Utility.BaseActivity;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.GetDataUsingWService;
import com.mentobile.grabbit.Utility.GetWebServiceData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Gokul on 11/15/2016.
 */
public class SplashActivity extends BaseActivity implements GetWebServiceData {

    private static final int LOAD_MERCHANT = 0;
    private static final int MERCHANT_DISTANCE = 1;
    private static final int LOAD_OFFER = 2;

    double current_latitude = 0;
    double current_longitude = 0;


    @Override
    public int getActivityLayout() {
        return R.layout.activity_splash;
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
        //for  load from server
        loadDataFromServer();
        //if load data  is  using  then comment desigion from  here
        // decision();
    }

//    private void decision() {
//
//        if (AppPref.getInstance().getUserID().equals("")) {
//            Handler handler = new Handler();
//            Runnable runnable = new Runnable() {
//                public void run() {
//                    sendToThisActivity(MainActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                }
//            };
//            handler.postDelayed(runnable, 2000);
//        } else if (!AppPref.getInstance().getUserID().equals("")) {
//            loadDataFromServer();
//        }
//    }

    @Override
    public void init(Bundle save) {

    }

    private void loadDataFromServer() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("api_key=").append(AppUrl.API_KEY);
        String content = stringBuilder.toString();
        GetDataUsingWService getDataUsingWService = new GetDataUsingWService(this, AppUrl.MERCHANTS_URL, LOAD_MERCHANT, content, false, "Loading ...", this);
        getDataUsingWService.execute();
        GetDataUsingWService getDataUsingWService1 = new GetDataUsingWService(this, AppUrl.ALL_OFFERS_URL, LOAD_OFFER, content, false, "Loading ...", this);
        getDataUsingWService1.execute();
    }

    @Override
    public void getWebServiceResponse(String responseData, int serviceCounter) {
        Log.w("responseData", responseData);
        switch (serviceCounter) {
            case LOAD_MERCHANT:
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONArray jsonArray = jsonObject.getJSONArray("merchants");
                    parseJSONArray(jsonArray);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MERCHANT_DISTANCE:
                parseMerchantDistance(responseData);
                break;

            case LOAD_OFFER:
                NotificationDatabase notificationDatabase = new NotificationDatabase(this);
                notificationDatabase.open();
                notificationDatabase.delete_offers();
                notificationDatabase.close();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    JSONArray jsonArray = jsonObject.getJSONArray("all_offers");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        String message = jsonObject1.getString("message");
                        String out_id = jsonObject1.getString("out_id");
                        String m_id = jsonObject1.getString("m_id");
                        String bcon_name = jsonObject1.getString("bcon_name");
                        String bcon_uuid = jsonObject1.getString("bcon_uuid");
                        notificationDatabase.open();
                        notificationDatabase.insert_offers(id, message, out_id, m_id, bcon_uuid, bcon_name);
                        notificationDatabase.close();
                    }
                    String data = "";
                } catch (Exception e) {
                    Log.w("database", "" + e);
                }
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

        decision();
    }

    private void decision() {
        if (AppPref.getInstance().getTakeATour().equals("")) {
            sendToThisActivity(TourActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else if (AppPref.getInstance().getUserID().equals("")) {
            sendToThisActivity(LoginActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            sendToThisActivity(DrawerActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
    }


    private void parseJSONArray(JSONArray jsonArray) throws Exception {

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            JSONObject jsonObject11 = jsonObject.getJSONObject("0");
            JSONArray jsonArray1 = jsonObject11.getJSONArray("compaign");
            List<OfferImageModel> offerImageModels = new ArrayList<OfferImageModel>();
            for (int j = 0; j < jsonArray1.length(); j++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                String offerimage = jsonObject1.getString("banner_name");
                OfferImageModel offerImageModel = new OfferImageModel("" + j, offerimage);
                offerImageModels.add(offerImageModel);
            }
            String m_id = jsonObject.getString("m_id");
            String business_name = jsonObject.getString("business_name");
            String logo = jsonObject.getString("logo");
            String email = jsonObject.getString("email");
            String about = jsonObject.getString("about");
            String open_time = jsonObject.getString("open_time");
            String close_time = jsonObject.getString("close_time");
            String opening_days = jsonObject.getString("opening_days");
            String website = jsonObject.getString("website");
            String facebook = jsonObject.getString("facebook");
            String twitter = jsonObject.getString("twitter");
            String Instagram = jsonObject.getString("Instagram");
            String gallery_img1 = jsonObject.getString("gallery_img1");
            String gallery_img2 = jsonObject.getString("gallery_img2");
            String gallery_img3 = jsonObject.getString("gallery_img3");
            String gallery_img4 = jsonObject.getString("gallery_img4");
            String gallery_img5 = jsonObject.getString("gallery_img5");
            String wishlist = jsonObject.getString("wishlist");
            String out_id = jsonObject.getString("out_id");
            String name = jsonObject.getString("name");
            String address = jsonObject.getString("address");
            String city_name = jsonObject.getString("city_name");
            String state_name = jsonObject.getString("state_name");
            String pincode = jsonObject.getString("pincode");
            String phone = jsonObject.getString("phone");
            String latitude = jsonObject.getString("latitude");
            String longitude = jsonObject.getString("longitude");
            String bcon_id = jsonObject.getString("bcon_id");
            String bcon_uuid = jsonObject.getString("bcon_uuid");

            NearByModel nearByModel = new NearByModel();
            nearByModel.setM_id(m_id);
            nearByModel.setBusiness_name(business_name);
            nearByModel.setLogo(logo);
            nearByModel.setEmail(email);
            nearByModel.setAbout(about);
            nearByModel.setOpen_time(open_time);
            nearByModel.setClose_time(close_time);
            nearByModel.setOpening_days(opening_days);
            nearByModel.setWebsite(website);
            nearByModel.setFacebook(facebook);
            nearByModel.setTwitter(twitter);
            nearByModel.setInstagram(Instagram);
            nearByModel.setGallery_img1(gallery_img1);
            nearByModel.setGallery_img2(gallery_img2);
            nearByModel.setGallery_img3(gallery_img3);
            nearByModel.setGallery_img4(gallery_img4);
            nearByModel.setGallery_img5(gallery_img5);
            nearByModel.setWishlist(wishlist);
            nearByModel.setOut_id(out_id);
            nearByModel.setName(name);
            nearByModel.setAddress(address);
            nearByModel.setCity_name(city_name);
            nearByModel.setState_name(state_name);
            nearByModel.setPincode(pincode);
            nearByModel.setPhone(phone);
            nearByModel.setLatitude(latitude);
            nearByModel.setLongitude(longitude);
            nearByModel.setBcon_id(bcon_id);
            nearByModel.setBcon_uuid(bcon_uuid);
            nearByModel.setOfferImageModels(offerImageModels);
            GrabbitApplication.nearByModelList.add(nearByModel);
            // decision();
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

    public void loadOffer() {

    }

}
