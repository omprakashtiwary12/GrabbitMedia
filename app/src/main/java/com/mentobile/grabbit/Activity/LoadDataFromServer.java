package com.mentobile.grabbit.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.mentobile.grabbit.Database.Database;
import com.mentobile.grabbit.GrabbitApplication;
import com.mentobile.grabbit.Model.ImageModel;
import com.mentobile.grabbit.Model.NearByModel;
import com.mentobile.grabbit.Utility.AppUrl;
import com.mentobile.grabbit.Utility.GetDataUsingWService;
import com.mentobile.grabbit.Utility.GetWebServiceData;
import com.mentobile.grabbit.Utility.Other;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Deepak Sharma on 4/19/2017.
 */

public class LoadDataFromServer implements GetWebServiceData {

    private static final String TAG = "LoadDataFromServer";
    private static final int LOAD_MERCHANT = 0;
    private double current_latitude = 0.00;
    private double current_longitude = 0.00;
    private Activity activity;
    private List<NearByModel> nearByModelList = new ArrayList<NearByModel>();

    public interface iGetResponse {
        void getLoadDataResponse(boolean isStatus);
    }

    private iGetResponse iGetResponse;

    public LoadDataFromServer() {

    }

    public LoadDataFromServer(Activity activity, iGetResponse iGetResponse) {
        this.iGetResponse = iGetResponse;
        this.activity = activity;
    }

    public void startFatching() {

        Double currentLocation[] = Other.getCurrentLocation(activity);
        current_latitude = currentLocation[0];
        current_longitude = currentLocation[1];
        Log.d(TAG, "::::current_latitude " + current_latitude + " ::: current_longitude" + current_longitude);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("api_key=").append(AppUrl.API_KEY);
        stringBuilder.append("&latitude=").append(current_latitude);
        stringBuilder.append("&longitude=").append(current_longitude);
        String content = stringBuilder.toString();
        Log.d(TAG, ":::::::Load Data " + content);
        GetDataUsingWService getDataUsingWService = new GetDataUsingWService(activity, AppUrl.MERCHANTS_URL, LOAD_MERCHANT, content, true, "Loading ...", this);
        getDataUsingWService.execute();
    }

    @Override
    public void getWebServiceResponse(String responseData, int serviceCounter) {
        Log.w("responseData", responseData);
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
                    values.put("buz_name", objectOffers.getString("name"));
                    values.put("m_id", objectOffers.getString("m_id"));
                    values.put("bcon_name", objectOffers.getString("bcon_name"));
                    values.put("bcon_mazor", objectOffers.getString("bcon_mazor"));
                    values.put("bcon_minor", objectOffers.getString("bcon_minor"));
                    values.put("bcon_uuid", objectOffers.getString("bcon_uuid"));
                    GrabbitApplication.database.insertData(values, Database.TBL_BEACON_OFFERS);
                }
                GrabbitApplication.getInstance().setBeaconNotification();
                iGetResponse.getLoadDataResponse(true);
            }
        } catch (Exception e) {
            iGetResponse.getLoadDataResponse(true);
            Toast.makeText(activity.getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void parseJSONArray(JSONArray jsonArray) throws Exception {

        if (GrabbitApplication.database != null) {
            GrabbitApplication.database = new Database(activity, 1);
        }
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);

            ContentValues contentValues = new ContentValues();

            String m_id = jsonObject.getString("m_id");
            contentValues.put("m_id", m_id);

            String business_name = jsonObject.getString("business_name");
            contentValues.put("business_name", business_name);

            String business_logo = jsonObject.getString("logo");
            contentValues.put("logo", business_logo);

            String business_banner = jsonObject.getString("banner");
            contentValues.put("business_banner", business_banner);

            String email = jsonObject.getString("email");
            contentValues.put("email", email);

            String cat_id = jsonObject.getString("cat_id");
            contentValues.put("cat_id", cat_id);

            String about = jsonObject.getString("about");
            contentValues.put("about", about);

            String open_time = jsonObject.getString("open_time");
            contentValues.put("open_time", open_time);

            String close_time = jsonObject.getString("close_time");
            contentValues.put("close_time", close_time);

            String opening_days = jsonObject.getString("opening_days");
            contentValues.put("opening_days", opening_days);

            String website = jsonObject.getString("website");
            contentValues.put("website", website);

            String facebook = jsonObject.getString("facebook");
            contentValues.put("facebook", facebook);

            String twitter = jsonObject.getString("twitter");
            contentValues.put("twitter", twitter);

            String Instagram = jsonObject.getString("Instagram");
            contentValues.put("Instagram", Instagram);

            for (int j = 1; j <= 6; j++) {
                String gallery_img = jsonObject.getString("gallery_img" + j);
                contentValues.put("gallery_img" + j, "" + gallery_img);
            }

            String status = jsonObject.getString("status");
            contentValues.put("status", status);

            String wishlist = jsonObject.getString("wishlist");
            contentValues.put("wishlist", wishlist);

            String out_id = jsonObject.getString("out_id");
            contentValues.put("out_id", out_id);

            String name = jsonObject.getString("name");
            contentValues.put("name", name);

            String address = jsonObject.getString("address");
            contentValues.put("address", address);

            String city_name = jsonObject.getString("city_name");
            contentValues.put("city_name", city_name);

            String state_name = jsonObject.getString("state_name");
            contentValues.put("state_name", state_name);

            String pincode = jsonObject.getString("pincode");
            contentValues.put("pincode", pincode);

            String phone = jsonObject.getString("phone");
            contentValues.put("phone", phone);

            String latitude = jsonObject.getString("latitude");
            contentValues.put("latitute", latitude);

            String longitude = jsonObject.getString("longitude");
            contentValues.put("longtitute", longitude);

            String bcon_id = jsonObject.getString("bcon_id");
            contentValues.put("bcon_id", "" + bcon_id);

            String bcon_uuid = jsonObject.getString("bcon_uuid");
            contentValues.put("bcon_uuid", "" + bcon_uuid);

            String distance = jsonObject.getString("distance");
            contentValues.put("distance", "" + distance);

            String outlet_status = jsonObject.getString("outlet_status");
            contentValues.put("outlet_status", outlet_status);

            JSONArray jsonArray1 = jsonObject.getJSONObject("0").getJSONArray("compaign");
            for (int j = 0; j < jsonArray1.length(); j++) {
                ContentValues contentValues1 = new ContentValues();
                String banner_name = jsonArray1.getJSONObject(j).getString("banner_name");
                String start_date = jsonArray1.getJSONObject(j).getString("start_date");
                String end_date = jsonArray1.getJSONObject(j).getString("end_date");
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
                contentValues1.put("m_id", m_id);
                contentValues1.put("out_id", out_id);
                contentValues1.put("banner_name", banner_name);
                contentValues1.put("start_dt", start_date);
                contentValues1.put("end_dt", end_date);
                GrabbitApplication.database.insertData(contentValues1, Database.TBL_COMPAIGNS);
            }
            GrabbitApplication.database.insertData(contentValues, Database.TBL_MERCHANTS);
        }

//        Collections.sort(nearByModelList, new Comparator<NearByModel>() {
//            @Override
//            public int compare(NearByModel lhs, NearByModel rhs) {
//                int distance1 = (int) lhs.getDistance();
//                int distance2 = (int) rhs.getDistance();
//                int distance_value = (distance1 < distance2) ? -1 : (distance1 > distance2) ? 0 : 1;
//                return distance_value;
//            }
//        });
    }

    public List<NearByModel> getMerchantList(String ASC, String filterCategory) {
        String query;
        if (filterCategory.equals("0")) {
            query = "SELECT * FROM tbl_merchants where status='ACTIVE' AND outlet_status='ACTIVE' AND distance < '50' order by distance ASC, business_name " + ASC;
        } else {
            query = "SELECT * FROM tbl_merchants where status='ACTIVE' AND outlet_status='ACTIVE' AND distance < '50' and cat_id IN" + "(" + filterCategory + ")" +
                    " order by distance ASC, business_name " + ASC;
        }

        Log.d(TAG, "::::Query " + query);
        Cursor cursor = GrabbitApplication.database.getMerchantDetailsFromLocalDB(query);
        Log.d(TAG, "::::Cursor Value  " + cursor.getCount());
        while (cursor.getCount() > 0 && cursor.moveToNext()) {

            NearByModel nearByModel = new NearByModel();

            String m_id = cursor.getString(1);
            nearByModel.setM_id(m_id);

            String business_name = cursor.getString(2);
            nearByModel.setBusiness_name(business_name);

            String business_logo = cursor.getString(3);
            nearByModel.setLogo(business_logo);

            String business_banner = cursor.getString(4);
            nearByModel.setBanner(business_banner);

            String email = cursor.getString(5);
            nearByModel.setEmail(email);

            String category_id = cursor.getString(6);
            nearByModel.setCategory_id(category_id);

            String about = cursor.getString(7);
            nearByModel.setAbout(about);

            String open_time = cursor.getString(8);
            nearByModel.setOpen_time(open_time);

            String close_time = cursor.getString(9);
            nearByModel.setClose_time(close_time);

            String opening_days = cursor.getString(10);
            nearByModel.setOpening_days(opening_days);

            String website = cursor.getString(11);
            nearByModel.setWebsite(website);

            String facebook = cursor.getString(12);
            nearByModel.setFacebook(facebook);

            String twitter = cursor.getString(13);
            nearByModel.setTwitter(twitter);

            String Instagram = cursor.getString(14);
            nearByModel.setInstagram(Instagram);

            for (int j = 1; j <= 6; j++) {
                String gallery_img = cursor.getString(14 + j);
                if (gallery_img != null && gallery_img.length() > 5) {
                    gallery_img = AppUrl.GET_IMAGE + m_id + "/" + gallery_img;
                    ImageModel imageModel = new ImageModel(j - 1, gallery_img);
                    nearByModel.getGalleyImage().add(imageModel);
                }
            }

            String status = cursor.getString(21);
            nearByModel.setStatus(status);

            String wishlist = cursor.getString(22);
            nearByModel.setWishlist(wishlist);

            String out_id = cursor.getString(23);
            nearByModel.setOut_id(out_id);

            String name = cursor.getString(24);
            nearByModel.setName(name);

            String address = cursor.getString(25);
            nearByModel.setAddress(address);

            String city_name = cursor.getString(26);
            nearByModel.setCity_name(city_name);

            String state_name = cursor.getString(27);
            nearByModel.setState_name(state_name);

            String pincode = cursor.getString(28);
            nearByModel.setPincode(pincode);

            String distance = cursor.getString(29);
            nearByModel.setDistance(distance);

            String phone = cursor.getString(30);
            nearByModel.setPhone(phone);

            String latitude = cursor.getString(31);
            nearByModel.setLatitude(latitude);

            String longitude = cursor.getString(32);
            nearByModel.setLongitude(longitude);

            String bcon_id = cursor.getString(33);
            nearByModel.setBcon_id(bcon_id);

            String bcon_uuid = cursor.getString(34);
            nearByModel.setBcon_uuid(bcon_uuid);

            Cursor cursor_compaign = GrabbitApplication.database.getMerchantCompaign(out_id, Other.getCurrentTime());
            while (cursor_compaign.getCount() > 0 && cursor_compaign.moveToNext()) {
                int id = cursor_compaign.getInt(0);
                String compaign_name = cursor_compaign.getString(3);
                compaign_name = AppUrl.GET_COMPAIGN_IMAGE + "/" + m_id + "/" + compaign_name;
                ImageModel imageModel = new ImageModel(id, compaign_name);
                nearByModel.getOfferImageModels().add(imageModel);
            }
            Log.d(TAG, ":::::Offer Model " + nearByModel.getOfferImageModels().size());
            if (nearByModel.getOfferImageModels().size() > 0)
                nearByModelList.add(nearByModel);
        }
        return nearByModelList;
    }

    public List<NearByModel> getWishList() {
        String query = "SELECT * FROM tbl_merchants where status='ACTIVE' AND outlet_status='ACTIVE' AND distance < '50' order by distance ASC, business_name ASC";
        Cursor cursor = GrabbitApplication.database.getMerchantDetailsFromLocalDB(query);
        while (cursor.getCount() > 0 && cursor.moveToNext()) {

            NearByModel nearByModel = new NearByModel();

            String wishlist = cursor.getString(22);
            if (wishlist.equals("1")) {

                nearByModel.setWishlist(wishlist);

                String m_id = cursor.getString(1);
                nearByModel.setM_id(m_id);

                String business_name = cursor.getString(2);
                nearByModel.setBusiness_name(business_name);

                String business_logo = cursor.getString(3);
                nearByModel.setLogo(business_logo);

                String business_banner = cursor.getString(4);
                nearByModel.setBanner(business_banner);

                String email = cursor.getString(5);
                nearByModel.setEmail(email);

                String category_id = cursor.getString(6);
                nearByModel.setCategory_id(category_id);

                String about = cursor.getString(7);
                nearByModel.setAbout(about);

                String open_time = cursor.getString(8);
                nearByModel.setOpen_time(open_time);

                String close_time = cursor.getString(9);
                nearByModel.setClose_time(close_time);

                String opening_days = cursor.getString(10);
                nearByModel.setOpening_days(opening_days);

                String website = cursor.getString(11);
                nearByModel.setWebsite(website);

                String facebook = cursor.getString(12);
                nearByModel.setFacebook(facebook);

                String twitter = cursor.getString(13);
                nearByModel.setTwitter(twitter);

                String Instagram = cursor.getString(14);
                nearByModel.setInstagram(Instagram);

                for (int j = 1; j <= 6; j++) {
                    String gallery_img = cursor.getString(14 + j);
                    if (gallery_img != null && gallery_img.length() > 5) {
                        gallery_img = AppUrl.GET_IMAGE + m_id + "/" + gallery_img;
                        ImageModel imageModel = new ImageModel(j - 1, gallery_img);
                        nearByModel.getGalleyImage().add(imageModel);
                    }
                }

                String status = cursor.getString(21);
                nearByModel.setStatus(status);

                String out_id = cursor.getString(23);
                nearByModel.setOut_id(out_id);

                String name = cursor.getString(24);
                nearByModel.setName(name);

                String address = cursor.getString(25);
                nearByModel.setAddress(address);

                String city_name = cursor.getString(26);
                nearByModel.setCity_name(city_name);

                String state_name = cursor.getString(27);
                nearByModel.setState_name(state_name);

                String pincode = cursor.getString(28);
                nearByModel.setPincode(pincode);

                String distance = cursor.getString(29);
                nearByModel.setDistance(distance);

                String phone = cursor.getString(30);
                nearByModel.setPhone(phone);

                String latitude = cursor.getString(31);
                nearByModel.setLatitude(latitude);

                String longitude = cursor.getString(32);
                nearByModel.setLongitude(longitude);

                String bcon_id = cursor.getString(33);
                nearByModel.setBcon_id(bcon_id);

                String bcon_uuid = cursor.getString(34);
                nearByModel.setBcon_uuid(bcon_uuid);

                Cursor cursor_compaign = GrabbitApplication.database.getMerchantCompaign(out_id, Other.getCurrentTime());
                while (cursor_compaign.getCount() > 0 && cursor_compaign.moveToNext()) {
                    int id = cursor_compaign.getInt(0);
                    String compaign_name = cursor_compaign.getString(3);
                    compaign_name = AppUrl.GET_COMPAIGN_IMAGE + "/" + m_id + "/" + compaign_name;
                    ImageModel imageModel = new ImageModel(id, compaign_name);
                    nearByModel.getOfferImageModels().add(imageModel);
                }
                nearByModelList.add(nearByModel);
            }
        }
        return nearByModelList;
    }
}
