package com.mentobile.grabbit.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mentobile.grabbit.Activity.DrawerActivity;
import com.mentobile.grabbit.GrabbitApplication;
import com.mentobile.grabbit.Model.NearByModel;
import com.mentobile.grabbit.Utility.AppPref;
import com.mentobile.grabbit.Utility.AppUrl;
import com.mentobile.grabbit.Utility.GetDataUsingWService;
import com.mentobile.grabbit.Utility.GetWebServiceData;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Administrator on 12/30/2016.
 */

public class DownloadDataService extends Service implements GetWebServiceData {

    private static final int LOAD_MERCHANT = 0;
    private static final int MERCHANT_DISTANCE = 1;

    double current_latitude = 0;
    double current_longitude = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        loadDataFromServer();
        //return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    private void loadDataFromServer() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("api_key=").append(AppUrl.API_KEY);
        String content = stringBuilder.toString();
        GetDataUsingWService getDataUsingWService = new GetDataUsingWService(this, AppUrl.MERCHANTS_URL, LOAD_MERCHANT, content, false, "Loading ...", this);
        getDataUsingWService.execute();
    }

    @Override
    public void getWebServiceResponse(String responseData, int serviceCounter) {
        Log.w("responseData", responseData);
        switch (serviceCounter) {
            case LOAD_MERCHANT:
                Log.w("downloaddataservice", responseData);
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("details");
                        parseJSONArray(jsonArray);
                    } else if (status.equalsIgnoreCase("0")) {
                        // toastMessage(jsonObject.getString("msg"));
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
        if (!AppPref.getInstance().getTakeATour().equals("")) {
            //decision();
            // sendToThisActivity(DrawerActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        // sendToThisActivity(DrawerActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
            nearByModel.setBusiness_logo(business_logo);
            nearByModel.setBusiness_banner(business_banner);
            nearByModel.setGallery_img1(gallery_img1);
            nearByModel.setGallery_img2(gallery_img2);
            nearByModel.setGallery_img3(gallery_img3);
            nearByModel.setGallery_img4(gallery_img4);
            nearByModel.setGallery_img5(gallery_img5);
            nearByModel.setOpening_days(opening_days);
            nearByModel.setCat_name(cat_name);
            nearByModel.setStatus(status);
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
