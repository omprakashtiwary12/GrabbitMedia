package com.grabbit.daily_deals.Model;

/**
 * Created by Deepak Sharma on 2/21/2017.
 */

public class OfferList {

    private String id;
    private String message;
    private String outlet_id;
    private String merchant_id;
    private String bcon_name;
    private String bcon_UUID;

    public OfferList(String id, String message, String outlet_id, String merchant_id, String bcon_name, String bcon_UUID) {
        this.id = id;
        this.message = message;
        this.outlet_id = outlet_id;
        this.merchant_id = merchant_id;
        this.bcon_name = bcon_name;
        this.bcon_UUID = bcon_UUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOutlet_id() {
        return outlet_id;
    }

    public void setOutlet_id(String outlet_id) {
        this.outlet_id = outlet_id;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getBcon_name() {
        return bcon_name;
    }

    public void setBcon_name(String bcon_name) {
        this.bcon_name = bcon_name;
    }

    public String getBcon_UUID() {
        return bcon_UUID;
    }

    public void setBcon_UUID(String bcon_UUID) {
        this.bcon_UUID = bcon_UUID;
    }
}
