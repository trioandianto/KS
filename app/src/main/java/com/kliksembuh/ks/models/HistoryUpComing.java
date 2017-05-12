package com.kliksembuh.ks.models;

import android.graphics.drawable.Drawable;

/**
 * Created by Trio Andianto on 4/12/2017.
 */

public class HistoryUpComing {
    private int idHistoryUpComing;
    private String namaDokter;
    private Drawable imgHistoryDoc;
    private String rumahSakit;
    private String tanggal;
    // private String alamat;
    // To Do (Ucu)
    // Change data type string to int on noAppointment
    // Change string to date & time date on jadwalBerobat
    private String noAppointment;
    private String specialtyDoc;
    private String appointmentDate;
    private String statusHistory;
    private String waktuBerobat;
    private String timeStart;
    private String timeEnd;

    public HistoryUpComing(int idHistoryUpComing, String namaDokter, Drawable imgHistoryDoc, String rumahSakit, String tanggal,
                           String noAppointment, String specialtyDoc, String appointmentDate, String statusHistory, String waktuBerobat, String timeStart, String timeEnd) {

        this.idHistoryUpComing = idHistoryUpComing;
        this.namaDokter = namaDokter;
        this.imgHistoryDoc = imgHistoryDoc;
        this.rumahSakit = rumahSakit;
        this.tanggal = tanggal;
        this.noAppointment = noAppointment;
        this.specialtyDoc = specialtyDoc;
        this.appointmentDate = appointmentDate;

        this.statusHistory = statusHistory;
        this.waktuBerobat = waktuBerobat;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public int getIdHistoryUpComing() {
        return idHistoryUpComing;
    }

    public void setIdHistoryUpComing(int idHistoryUpComing) {
        this.idHistoryUpComing = idHistoryUpComing;
    }

    public String getNamaDokter() {
        return namaDokter;
    }

    public void setNamaDokter(String namaDokter) {
        this.namaDokter = namaDokter;
    }

    public Drawable getImgHistoryDoc() {
        return imgHistoryDoc;
    }

    public void setImgHistoryDoc(Drawable imgHistoryDoc) {
        this.imgHistoryDoc = imgHistoryDoc;
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

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
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

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }
}
