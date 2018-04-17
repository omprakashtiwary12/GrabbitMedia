package com.grabbit.daily_deals.Activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.util.Log;

import com.facebook.Profile;
import com.facebook.internal.Utility;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.grabbit.daily_deals.Database.Database;
import com.grabbit.daily_deals.Fragment.LocationFragment;
import com.grabbit.daily_deals.GrabbitApplication;
import com.grabbit.daily_deals.Model.ImageModel;
import com.grabbit.daily_deals.Model.NearByModel;
import com.grabbit.daily_deals.R;
import com.grabbit.daily_deals.Utility.AppPref;
import com.grabbit.daily_deals.Utility.AppUrl;
import com.grabbit.daily_deals.Utility.GetDataUsingWService;
import com.grabbit.daily_deals.Utility.GetWebServiceData;
import com.grabbit.daily_deals.Utility.Other;
import com.grabbit.daily_deals.currentLocation.CurrentLocation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Deepak Sharma on 4/19/2017.
 */

public class LoadDataFromServer extends CurrentLocation.LocationResult implements GetWebServiceData, LocationFragment.iLocationFragment {

    private static final String TAG = "LoadDataFromServer";
    private static final int LOAD_MERCHANT = 0;
    private Activity activity;
    private boolean isShowProgressBar = true;
    private List<NearByModel> nearByModelList = new ArrayList<NearByModel>();
    private iGetResponse iGetResponse;

    public interface iGetResponse {
        void getLoadDataResponse(boolean isStatus);
    }

    public LoadDataFromServer() {

    }

    public LoadDataFromServer(Activity activity, iGetResponse iGetResponse) {
        this.iGetResponse = iGetResponse;
        this.activity = activity;
    }

    public LoadDataFromServer(Activity activity, iGetResponse iGetResponse, boolean isShowProgressBar) {
        this.iGetResponse = iGetResponse;
        this.activity = activity;
        this.isShowProgressBar = isShowProgressBar;
    }

    public void startFatching(int page_no, int cat_id) {
        SimpleDateFormat s = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        s.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("api_key=").append(AppUrl.API_KEY);
        stringBuilder.append("&latitude=").append(AppPref.getInstance().getLat());
        stringBuilder.append("&longitude=").append(AppPref.getInstance().getLong());
        stringBuilder.append("&cat_id=").append(cat_id);
        stringBuilder.append("&page_no=").append(page_no);
        String content = stringBuilder.toString();
//        Log.d(TAG, ":::::::Load Data 1 " + content);
        GetDataUsingWService getDataUsingWService = new GetDataUsingWService(activity, AppUrl.MERCHANTS_URL, LOAD_MERCHANT, content, isShowProgressBar, "Loading ...", LoadDataFromServer.this);
        getDataUsingWService.execute();
    }

    @Override
    public void gotLocation(Location location) {
//        if(location!=null) {

//        }
//        else{
//            LocationFragment locationFragment = new LocationFragment();
//            locationFragment.setCommunicator(this);
//            FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();
//            fragmentTransaction.add(android.R.id.content, locationFragment);
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
//        }
    }

    @Override
    public void getCurrentLocation(String latitute, String longtitute) {
        AppPref.getInstance().setLat("" + latitute);
        AppPref.getInstance().setLong("" + longtitute);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("api_key=").append(AppUrl.API_KEY);
        stringBuilder.append("&latitude=").append(latitute);
        stringBuilder.append("&longitude=").append(longtitute);
        String content = stringBuilder.toString();
        Log.d(TAG, ":::::::Load Data " + content);
        GetDataUsingWService getDataUsingWService = new GetDataUsingWService(activity, AppUrl.MERCHANTS_URL, 0, content, true, "Loading ...", this);
        getDataUsingWService.execute();
    }

    @Override
    public void getWebServiceResponse(String responseData, int serviceCounter) {
//        Log.w("responseData", responseData);
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("1")) {
                JSONObject jsonObject1 = jsonObject.getJSONObject("0");
                JSONArray jsonArray = jsonObject1.getJSONArray("merchants");
                parseJSONArray(jsonArray);
                // Get All Offers

//                JSONArray jsonArrayOffers = jsonObject1.getJSONArray("all_offers");
//                for (int i = 0; i < jsonArrayOffers.length(); i++) {
//                    JSONObject objectOffers = jsonArrayOffers.getJSONObject(i);
//                    ContentValues values = new ContentValues();
//                    values.put("id", objectOffers.getInt("id"));
//                    values.put("message", objectOffers.getString("message"));
//                    values.put("out_id", objectOffers.getInt("out_id"));
//                    values.put("buz_name", objectOffers.getString("name"));
//                    values.put("m_id", objectOffers.getString("m_id"));
//                    values.put("bcon_name", objectOffers.getString("bcon_name"));
//                    values.put("bcon_mazor", objectOffers.getString("bcon_mazor"));
//                    values.put("bcon_minor", objectOffers.getString("bcon_minor"));
//                    values.put("bcon_uuid", objectOffers.getString("bcon_uuid"));
//                    GrabbitApplication.database.insertData(values, Database.TBL_BEACON_OFFERS);
//                }
//                GrabbitApplication.getInstance().setBeaconNotification();
                iGetResponse.getLoadDataResponse(true);
            }
        } catch (Exception e) {
            iGetResponse.getLoadDataResponse(true);
            // Toast.makeText(activity.getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void parseJSONArray(JSONArray jsonArray) throws Exception {
        nearByModelList.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            NearByModel nearByModel = new NearByModel();
            String m_id = jsonObject.getString("m_id");
            nearByModel.setM_id(m_id);

            String business_name = jsonObject.getString("business_name");
            nearByModel.setBusiness_name(business_name);

            String business_logo = jsonObject.getString("logo");
            nearByModel.setLogo(business_logo);

            String business_banner = jsonObject.getString("banner");
            nearByModel.setBanner(business_banner);

            String email = jsonObject.getString("email");
            nearByModel.setEmail(email);

            String cat_id = jsonObject.getString("cat_id");
            nearByModel.setCategory_id(cat_id);

            String about = jsonObject.getString("about");
            nearByModel.setAbout(about);

            String open_time = jsonObject.getString("open_time");
            nearByModel.setOpen_time(open_time);

            String close_time = jsonObject.getString("close_time");
            nearByModel.setClose_time(close_time);

            String opening_days = jsonObject.getString("opening_days");
            nearByModel.setOpening_days(opening_days);

            String website = jsonObject.getString("website");
            nearByModel.setWebsite(website);

            String facebook = jsonObject.getString("facebook");
            nearByModel.setFacebook(facebook);

            String twitter = jsonObject.getString("twitter");
            nearByModel.setTwitter(twitter);

            String Instagram = jsonObject.getString("Instagram");
            nearByModel.setInstagram(Instagram);

            for (int j = 1; j <= 6; j++) {
                String gallery_img = jsonObject.getString("gallery_img" + j);
                if (gallery_img != null && gallery_img.length() > 5) {
                    gallery_img = AppUrl.GET_IMAGE + m_id + "/" + gallery_img;
                    ImageModel imageModel = new ImageModel(j - 1, gallery_img);
                    nearByModel.getGalleyImage().add(imageModel);
                }
            }

            String status = jsonObject.getString("status");
            nearByModel.setStatus(status);

            String wishlist = jsonObject.getString("wishlist");
            nearByModel.setWishlist(wishlist);

            String out_id = jsonObject.getString("out_id");
            nearByModel.setOut_id(out_id);

            String name = jsonObject.getString("name");
            nearByModel.setName(name);

            String address = jsonObject.getString("address");
            nearByModel.setAddress(address);

            String city_name = jsonObject.getString("city_name");
            nearByModel.setCity_name(city_name);

            String state_name = jsonObject.getString("state_name");
            nearByModel.setState_name(state_name);

            String pincode = jsonObject.getString("pincode");
            nearByModel.setPincode(pincode);

            String phone = jsonObject.getString("phone");
            nearByModel.setPhone(phone);

            String latitude = jsonObject.getString("latitude");
            nearByModel.setLatitude(latitude);

            String longitude = jsonObject.getString("longitude");
            nearByModel.setLongitude(longitude);

            String bcon_id = jsonObject.getString("bcon_id");
            nearByModel.setBcon_id(bcon_id);

            String bcon_uuid = jsonObject.getString("bcon_uuid");
            nearByModel.setBcon_uuid(bcon_uuid);

            String count_click = jsonObject.getString("count_click");
            nearByModel.setCount_click(count_click);

            double distance1 = jsonObject.getDouble("distance");
            distance1 = Other.round(distance1, 1);
            nearByModel.setDistance("" + distance1);

            int intDistance = jsonObject.getInt("distance");
            nearByModel.setIntDistance(intDistance);

            String outlet_status = jsonObject.getString("outlet_status");
            //contentValues.put("outlet_status", outlet_status);

            JSONArray jsonArray1 = jsonObject.getJSONObject("0").getJSONArray("compaign");
            for (int j = 0; j < jsonArray1.length(); j++) {
                //ContentValues contentValues1 = new ContentValues();
                String banner_name = jsonArray1.getJSONObject(j).getString("banner_name");
                String start_date = jsonArray1.getJSONObject(j).getString("start_date");
                String end_date = jsonArray1.getJSONObject(j).getString("end_date");
                String offer_description = jsonArray1.getJSONObject(j).getString("offer_description");
                String offer_details = jsonArray1.getJSONObject(j).getString("offer_details");
                try {
                    SimpleDateFormat f = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                    f.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
                    Date d = f.parse(start_date);
                    start_date = "" + d.getTime();
                    Date d1 = f.parse(end_date);
                    end_date = "" + d1.getTime();
                } catch (Exception e) {
                    Log.d(TAG, "::::Exception " + e.getMessage());
                }
                banner_name = AppUrl.GET_COMPAIGN_IMAGE + "/" + m_id + "/" + banner_name;
                ImageModel imageModel = new ImageModel(j, banner_name, offer_description, offer_details);
                nearByModel.getOfferImageModels().add(imageModel);
            }
            if (nearByModel.getOfferImageModels().size() > 0)
                nearByModelList.add(nearByModel);
        }
    }

    public List<NearByModel> getMerchantList(int filterCategory) {
        if (filterCategory == 0) {
            Collections.sort(nearByModelList);
            return nearByModelList;
        }
        ArrayList<NearByModel> tempList = new ArrayList<>();
        for (int i = 0; i < nearByModelList.size(); i++) {
            NearByModel nearByModel = nearByModelList.get(i);
            if (nearByModel.getCategory_id().equals("" + filterCategory)) {
                tempList.add(nearByModel);
            }
        }
        Collections.sort(tempList);
        return tempList;
    }
}
