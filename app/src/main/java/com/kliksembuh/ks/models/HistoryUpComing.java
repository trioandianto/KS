package com.kliksembuh.ks.models;

import android.graphics.drawable.Drawable;

/**
 * Created by Trio Andianto on 4/12/2017.
 */

public class HistoryUpComing {
    private String namaDokter;
    private Drawable image;
    private String rumahSakit;
    private String tanggal;
    private String alamat;
    public HistoryUpComing(String namaDokter, Drawable image, String rumahSakit, String tanggal, String alamat){
        this.namaDokter = namaDokter;
        this.image = image;
        this.rumahSakit = rumahSakit;
        this.tanggal = tanggal;
        this.alamat = alamat;
    }

    public String getNamaDokter() {
        return namaDokter;
    }

    public void setNamaDokter(String namaDokter) {
        this.namaDokter = namaDokter;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getRumahSakit() {
        return rumahSakit;
    }

    public void setRumahSakit(String rumahSakit) {
        this.rumahSakit = rumahSakit;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }


}
