package com.grabbit.daily_deals.Model;

/**
 * Created by Deepak Sharma on 2/17/2017.
 */

public class ImageModel {

    private int id;
    private String name;
    private String offer_description;
    private String offer_details;

    public ImageModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public ImageModel(int id, String name, String offer_description, String offer_details) {
        this.id = id;
        this.name = name;
        this.offer_description = offer_description;
        this.offer_details = offer_details;
    }

    public String getOffer_description() {
        return offer_description;
    }

    public void setOffer_description(String offer_description) {
        this.offer_description = offer_description;
    }

    public String getOffer_details() {
        return offer_details;
    }

    public void setOffer_details(String offer_details) {
        this.offer_details = offer_details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
