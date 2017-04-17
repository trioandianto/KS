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
    // To Do (Ucu)
    // Change data type string to int on noAppointment
    // Change string to date & time date on jadwalBerobat
    private String noAppointment;
    private String specialtyDoc;
    private String statusHistory;
    private String waktuBerobat;
    private String jamBerobat;

    public HistoryUpComing(String namaDokter, Drawable image, String rumahSakit, String tanggal, String alamat,
                           String noAppointment, String specialtyDoc, String statusHistory, String waktuBerobat, String jamBerobat){

        this.namaDokter = namaDokter;
        this.image = image;
        this.rumahSakit = rumahSakit;
        this.tanggal = tanggal;
        this.alamat = alamat;
        this.noAppointment = noAppointment;
        this.specialtyDoc = specialtyDoc;
        this.statusHistory = statusHistory;
        this.waktuBerobat = waktuBerobat;
        this.jamBerobat = jamBerobat;
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

    public String getNoAppointment() {
        return noAppointment;
    }

    public void setNoAppointment(String noAppointment) {
        this.noAppointment = noAppointment;
    }

    public String getSpecialtyDoc() {
        return specialtyDoc;
    }

    public void setSpecialtyDoc(String specialtyDoc) {
        this.specialtyDoc = specialtyDoc;
    }

    public String getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(String statusHistory) {
        this.statusHistory = statusHistory;
    }

    public String getWaktuBerobat() {
        return waktuBerobat;
    }

    public void setWaktuBerobat(String waktuBerobat) {
        this.waktuBerobat = waktuBerobat;
    }

    public String getJamBerobat() {
        return jamBerobat;
    }

    public void setJamBerobat(String jamBerobat) {
        this.jamBerobat = jamBerobat;
    }


}
