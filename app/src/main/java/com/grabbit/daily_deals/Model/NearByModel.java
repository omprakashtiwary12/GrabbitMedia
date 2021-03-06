package com.grabbit.daily_deals.Model;

import android.media.Image;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* Created By Gokul */
public class NearByModel implements Serializable, Comparable {

    private String m_id;
    private String business_name;
    private String logo;
    private String banner;
    private String email;
    private String about;
    private String category_id;
    private String open_time;
    private String close_time;
    private String opening_days;
    private String website;
    private String facebook;
    private String twitter;
    private String Instagram;
    private String wishlist;
    private String out_id;
    private String name;
    private String address;
    private String city_name;
    private String state_name;
    private String pincode;
    private String phone;
    private String latitude;
    private String longitude;
    private String bcon_id;
    private String bcon_uuid;
    private String count_click;
    private String distance;
    private int intDistance;
    private String status;
    private List<ImageModel> offerImageModels = new ArrayList<ImageModel>();
    private List<ImageModel> gallerImageModels = new ArrayList<ImageModel>();

    public NearByModel() {

    }

    public NearByModel(List<ImageModel> offerImageModels, String m_id, String business_name,
                       String logo, String banner, String email, String about, String open_time,
                       String close_time, String opening_days, String website, String facebook,
                       String twitter, String instagram, String wishlist, String out_id, String name,
                       String address, String city_name, String state_name, String pincode, String phone,
                       String latitude, String longitude, String bcon_id, String bcon_uuid, String distance) {
        this.offerImageModels = offerImageModels;
        this.m_id = m_id;
        this.business_name = business_name;
        this.logo = logo;
        this.banner = banner;
        this.email = email;
        this.about = about;
        this.open_time = open_time;
        this.close_time = close_time;
        this.opening_days = opening_days;
        this.website = website;
        this.facebook = facebook;
        this.twitter = twitter;
        Instagram = instagram;
        this.wishlist = wishlist;
        this.out_id = out_id;
        this.name = name;
        this.address = address;
        this.city_name = city_name;
        this.state_name = state_name;
        this.pincode = pincode;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bcon_id = bcon_id;
        this.bcon_uuid = bcon_uuid;
        this.distance = distance;
    }

    public List<ImageModel> getOfferImageModels() {
        return offerImageModels;
    }

    public void setOfferImageModels(List<ImageModel> offerImageModels) {
        this.offerImageModels = offerImageModels;
    }

    public List<ImageModel> getGalleyImage() {
        return gallerImageModels;
    }

    public void setGalleryImage(List<ImageModel> gallerImageModels) {
        this.gallerImageModels = gallerImageModels;
    }

    public String getM_id() {
        return m_id;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    public String getOpening_days() {
        return opening_days;
    }

    public void setOpening_days(String opening_days) {
        this.opening_days = opening_days;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getInstagram() {
        return Instagram;
    }

    public void setInstagram(String instagram) {
        Instagram = instagram;
    }

    public String getWishlist() {
        return wishlist;
    }

    public void setWishlist(String wishlist) {
        this.wishlist = wishlist;
    }

    public String getOut_id() {
        return out_id;
    }

    public void setOut_id(String out_id) {
        this.out_id = out_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getBcon_id() {
        return bcon_id;
    }

    public void setBcon_id(String bcon_id) {
        this.bcon_id = bcon_id;
    }

    public String getBcon_uuid() {
        return bcon_uuid;
    }

    public void setBcon_uuid(String bcon_uuid) {
        this.bcon_uuid = bcon_uuid;
    }

    public String getCount_click() {
        return count_click;
    }

    public void setCount_click(String count_click) {
        this.count_click = count_click;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFullAddress() {
        String fullAddress = "";
        if (getAddress() != null && getAddress().length() > 0) {
            fullAddress += getAddress() + ", ";
        }
        if (getCity_name() != null || getCity_name().length() > 0) {
            fullAddress += getCity_name() + ", ";
        }
        if (getState_name() != null || getState_name().length() > 0) {
            fullAddress += getState_name() + ", ";
        }
        if (getPincode() != null && getPincode().length() > 0) {
            fullAddress += getPincode();
        }
        return fullAddress;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public int getIntDistance() {
        return intDistance;
    }

    public void setIntDistance(int intDistance) {
        this.intDistance = intDistance;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        int compareValue = ((NearByModel) o).getIntDistance();
        return this.intDistance - compareValue;
    }
}