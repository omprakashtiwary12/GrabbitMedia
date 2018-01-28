package com.grabbit.daily_deals.Model;

/**
 * Created by Deepak Sharma on 1/4/2018.
 */

public class HospitalData {

    private String place_id;
    private String name;
    private String rating;
    private String icon;
    private String open_now;
    private String vicinity;
    private Double latitude;
    private Double longitude;
    private Double distance;


    public HospitalData(String place_id, String name, String rating, String icon, String open_now, String vicinity, Double latitude, Double longitude, Double distance) {
        this.place_id = place_id;
        this.name = name;
        this.rating = rating;
        this.icon = icon;
        this.open_now = open_now;
        this.vicinity = vicinity;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getOpen_now() {
        return open_now;
    }

    public void setOpen_now(String open_now) {
        this.open_now = open_now;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getDistance() {
        return distance;
    }
    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
