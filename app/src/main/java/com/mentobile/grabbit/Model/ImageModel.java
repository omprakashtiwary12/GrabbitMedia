package com.mentobile.grabbit.Model;

/**
 * Created by Deepak Sharma on 2/17/2017.
 */

public class ImageModel {

    private int id;
    private String name;

    public ImageModel(int id, String name) {
        this.id = id;
        this.name = name;
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
