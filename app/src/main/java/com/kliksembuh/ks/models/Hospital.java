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
    private String phoneNbr;
    private String capabilitiesDesc;
    private String stringImg;
    private Drawable iv_image1;
    private Drawable iv_image2;
    private Drawable iv_image3;


    private String moreInfoInsurance;

    // Constructor

    public Hospital(String id, Drawable hospital_pic_id, String name, String address, String phoneNbr, String capabilitiesDesc, String stringImg
                    ) {
        this.id = id;
        this.hospital_pic_id = hospital_pic_id;
        this.name = name;
        this.address = address;
        this.phoneNbr = phoneNbr;
        this.capabilitiesDesc = capabilitiesDesc;
        this.stringImg = stringImg;
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

    public String getPhoneNbr() {
        return phoneNbr;
    }

    public void setPhoneNbr(String phoneNbr) {
        this.phoneNbr = phoneNbr;
    }

    public String getCapabilitiesDesc() {
        return capabilitiesDesc;
    }

    public void setCapabilitiesDesc(String capabilitiesDesc) {
        this.capabilitiesDesc = capabilitiesDesc;
    }
    public String getStringImg() {
        return stringImg;
    }

    public void setStringImg(String stringImg) {
        this.stringImg = stringImg;
    }
    public Drawable getIv_image1() {
        return iv_image1;
    }

    public void setIv_image1(Drawable iv_image1) {
        this.iv_image1 = iv_image1;
    }

    public Drawable getIv_image2() {
        return iv_image2;
    }

    public void setIv_image2(Drawable iv_image2) {
        this.iv_image2 = iv_image2;
    }

    public Drawable getIv_image3() {
        return iv_image3;
    }

    public void setIv_image3(Drawable iv_image3) {
        this.iv_image3 = iv_image3;
    }
    public String getMoreInfoInsurance() {
        return moreInfoInsurance;
    }

    public void setMoreInfoInsurance(String moreInfoInsurance) {
        this.moreInfoInsurance = moreInfoInsurance;
    }



}