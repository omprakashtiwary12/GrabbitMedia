package com.grabbit.daily_deals.Model;

public class Notification {

    private String id;
    private String title;
    private String description;
    private String view_status;
    private String date;

    public Notification(String id, String title, String description, String view_status, String date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getView_status() {
        return view_status;
    }

    public void setView_status(String view_status) {
        this.view_status = view_status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
