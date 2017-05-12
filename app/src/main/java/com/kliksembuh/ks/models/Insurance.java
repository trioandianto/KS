package com.kliksembuh.ks.models;

import android.graphics.drawable.Drawable;

/**
 * Created by Ucu Nurul Ulum on 10/05/2017.
 */

public class Insurance {
    private String instID;
    private String instInsuranceID;
    private String instName;
    private Drawable instImgUrl;

    public Insurance(String instID, String instInsuranceID, String instName, Drawable instImgUrl) {
        this.instID = instID;
        this.instInsuranceID = instInsuranceID;
        this.instName = instName;
        this.instImgUrl = instImgUrl;
    }
    public String getInstID() {
        return instID;
    }

    public void setInstID(String instID) {
        this.instID = instID;
    }

    public String getInstInsuranceID() {
        return instInsuranceID;
    }

    public void setInstInsuranceID(String instInsuranceID) {
        this.instInsuranceID = instInsuranceID;
    }

    public String getInstName() {
        return instName;
    }

    public void setInstName(String instName) {
        this.instName = instName;
    }

    public Drawable getInstImgUrl() {
        return instImgUrl;
    }

    public void setInstImgUrl(Drawable instImgUrl) {
        this.instImgUrl = instImgUrl;
    }
}
