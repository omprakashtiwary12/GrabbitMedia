package com.grabbit.daily_deals.Model;

/**
 * Created by Deepak Sharma on 1/20/2018.
 */

public class Helpline {

    private int id;
    private String title;
    private String phone;

    public Helpline(int id, String title, String phone) {
        this.id = id;
        this.title = title;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
