package com.kliksembuh.ks.models;

import android.graphics.drawable.Drawable;

/**
 * Created by Trio Andianto on 2/1/2017.
 */
public class Hospital {
    private String id;
    private Drawable hospital_pic_id;
    private String name;
    private String address;

    // Constructor

    public Hospital(String id, Drawable hospital_pic_id, String name, String address) {
        this.id = id;
        this.hospital_pic_id = hospital_pic_id;
        this.name = name;
        this.address = address;
    }


    // Getter, setter

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Drawable getHospital_pic_id() {
        return hospital_pic_id;
    }

    public void setHospital_pic_id(Drawable hospital_pic_id) {
        this.hospital_pic_id = hospital_pic_id;
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

}