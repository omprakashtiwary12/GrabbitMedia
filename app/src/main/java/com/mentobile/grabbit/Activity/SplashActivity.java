package com.mentobile.grabbit.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.estimote.sdk.SystemRequirementsChecker;
import com.mentobile.grabbit.GrabbitApplication;
import com.mentobile.grabbit.Model.ImageModel;
import com.mentobile.grabbit.Model.NearByModel;
import com.mentobile.grabbit.Utility.AppPref;
import com.mentobile.grabbit.Utility.AppUrl;
import com.mentobile.grabbit.Utility.BaseActivity;
import com.mentobile.grabbit.R;
import com.mentobile.grabbit.Utility.GetDataUsingWService;
import com.mentobile.grabbit.Utility.GetWebServiceData;
import com.mentobile.grabbit.Utility.NetworkErrorActivity;
import com.mentobile.grabbit.Utility.Other;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gokul on 11/15/2016.
 */

public class SplashActivity extends BaseActivity implements GetWebServiceData {

    private static final String TAG = "SplashActivity";

    private static final int LOAD_MERCHANT = 0;
    private static final int MERCHANT_DISTANCE = 1;

    double current_latitude = 28.4138570;
    double current_longitude = 77.0421780;

    public static List<NearByModel> nearByModelList = new ArrayList<NearByModel>();

    @Override
    public int getActivityLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void initialize() {
        getSupportActionBar().hide();
//        AppPref.getInstance().clearSharedPreferenceFile();
//        Log.d(TAG,"::::isLogin"+AppPref.getInstance().isLogin());
//        Double currentLocation[] = Other.getCurrentLocation(this);
//        current_latitude = currentLocation[0];
//        current_longitude = currentLocation[1];
        Log.d(TAG, "::::current_latitude " + current_latitude + " ::: current_longitude" + current_longitude);
        loadDataFromServer();
//        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

    public void onWindowFocusChanged() {
        if (!Other.isNetworkAvailable(getApplicationContext())) {
            Intent intent = new Intent(SplashActivity.this, NetworkErrorActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
//        else if (!Other.checkBluetoothConnection()) {
//            Intent intent = new Intent(SplashActivity.this, BluetoothActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();
//        }
        else if (!AppPref.getInstance().isLogin()) {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashActivity.this, CategoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void init(Bundle save) {

    }

    private void loadDataFromServer() {
        nearByModelList.clear();
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
                        JSONObject jsonObject1 = jsonObject.getJSONObject("0");
                        JSONArray jsonArray = jsonObject1.getJSONArray("merchants");
                        parseJSONArray(jsonArray);

                        // Get All Offers

                        JSONArray jsonArrayOffers = jsonObject1.getJSONArray("all_offers");
                        for (int i = 0; i < jsonArrayOffers.length(); i++) {
                            JSONObject objectOffers = jsonArrayOffers.getJSONObject(i);
                            ContentValues values = new ContentValues();
                            values.put("id", objectOffers.getInt("id"));
                            values.put("message", objectOffers.getString("message"));
                            values.put("out_id", objectOffers.getInt("out_id"));
                            values.put("m_id", objectOffers.getString("m_id"));
                            values.put("bcon_name", objectOffers.getString("bcon_name"));
                            values.put("bcon_mazor", objectOffers.getString("bcon_mazor"));
                            values.put("bcon_minor", objectOffers.getString("bcon_minor"));
                            values.put("bcon_uuid", objectOffers.getString("bcon_uuid"));
                            GrabbitApplication.database.insertData(values);
                        }
                        // End All Offers Data
                        calcualteDistance();
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
                nearByModelList.get(i).setDistance(distance);
            }
            onWindowFocusChanged();
        } catch (Exception e) {
            onWindowFocusChanged();
        }
    }

    private void parseJSONArray(JSONArray jsonArray) throws Exception {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            NearByModel nearByModel = new NearByModel();

            String m_id = jsonObject.getString("m_id");
            nearByModel.setM_id(m_id);

            String business_name = jsonObject.getString("business_name");
            nearByModel.setBusiness_name(business_name);

            String email = jsonObject.getString("email");
            nearByModel.setEmail(email);

            String phone = jsonObject.getString("phone");
            nearByModel.setPhone(phone);

            String latitude = jsonObject.getString("latitude");
            nearByModel.setLatitude(latitude);

            String longitude = jsonObject.getString("longitude");
            nearByModel.setLongitude(longitude);

            String address = jsonObject.getString("address");
            nearByModel.setAddress(address);

            String about = jsonObject.getString("about");
            nearByModel.setAbout(about);

            String website = jsonObject.getString("website");
            nearByModel.setWebsite(website);

            String facebook = jsonObject.getString("facebook");
            nearByModel.setFacebook(facebook);

            String twitter = jsonObject.getString("twitter");
            nearByModel.setTwitter(twitter);

            String Instagram = jsonObject.getString("Instagram");
            nearByModel.setInstagram(Instagram);

            String open_time = jsonObject.getString("open_time");
            nearByModel.setOpen_time(open_time);

            String close_time = jsonObject.getString("close_time");
            nearByModel.setClose_time(close_time);

            String city_name = jsonObject.getString("city_name");
            nearByModel.setCity_name(city_name);

            String state_name = jsonObject.getString("state_name");
            nearByModel.setState_name(state_name);

            String business_logo = jsonObject.getString("logo");
            nearByModel.setLogo(business_logo);

            String business_banner = jsonObject.getString("banner");
            nearByModel.setBanner(business_banner);

            for (int j = 1; j <= 6; j++) {
                String gallery_img = jsonObject.getString("gallery_img" + j);
                gallery_img = AppUrl.GET_IMAGE + m_id + "/" + gallery_img;
                ImageModel imageModel = new ImageModel(j - 1, gallery_img);
                nearByModel.getGalleyImage().add(imageModel);
            }
            String opening_days = jsonObject.getString("opening_days");
            nearByModel.setOpening_days(opening_days);

            String wishlist = jsonObject.getString("wishlist");
            nearByModel.setWishlist(wishlist);

            String out_id = jsonObject.getString("out_id");
            nearByModel.setOut_id(out_id);

            JSONArray jsonArray1 = jsonObject.getJSONObject("0").getJSONArray("compaign");
            for (int j = 0; j < jsonArray1.length(); j++) {
                String banner_name = jsonArray1.getJSONObject(j).getString("banner_name");
                banner_name = AppUrl.GET_COMPAIGN_IMAGE + m_id + "/" + banner_name;
                //Log.d(TAG, ":::::Banner url " + banner_name);
                ImageModel imageModel = new ImageModel(j, banner_name);
                nearByModel.getOfferImageModels().add(imageModel);
            }
            nearByModelList.add(nearByModel);
        }
    }

    private void calcualteDistance() {
        String finalURL = AppUrl.DISTANCE_URL + "&origins=" + current_latitude + "," + current_longitude + "&destinations=";
        for (int i = 0; i < nearByModelList.size(); i++) {
            finalURL += "%7C" + nearByModelList.get(i).getLatitude() + "%2C" + nearByModelList.get(i).getLongitude();
        }
        finalURL += AppUrl.DISTANCE_API_KEY;
        Log.w("finalUrl", finalURL);
        GetDataUsingWService getDataUsingWService = new GetDataUsingWService(this, finalURL, MERCHANT_DISTANCE, "", false, "Loading ...", this);
        getDataUsingWService.execute();
    }
}
