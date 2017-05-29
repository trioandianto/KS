package com.kliksembuh.ks.models;

/**
 * Created by Ucu Nurul Ulum on 29/05/2017.
 */

public class Reminder {
    private int rmdr_id;
    private String rmdrTitle;
    private String rmdrType;
    private String rmdrDosage;
    private String rmdrTimings;
    private String rmdrDays;
    private String rmdrDuration;

    public Reminder(int rmdr_id, String rmdrTitle, String rmdrType, String rmdrDosage, String rmdrTimings, String rmdrDays, String rmdrDuration) {

        this.rmdr_id = rmdr_id;
        this.rmdrTitle = rmdrTitle;
        this.rmdrType = rmdrType;
        this.rmdrDosage = rmdrDosage;
        this.rmdrTimings = rmdrTimings;
        this.rmdrDays = rmdrDays;
        this.rmdrDuration = rmdrDuration;
    }

    public int getRmdr_id() {
        return rmdr_id;
    }

    public void setRmdr_id(int rmdr_id) {
        this.rmdr_id = rmdr_id;
    }

    public String getRmdrTitle() {
        return rmdrTitle;
    }

    public void setRmdrTitle(String rmdrTitle) {
        this.rmdrTitle = rmdrTitle;
    }

    public String getRmdrType() {
        return rmdrType;
    }

    public void setRmdrType(String rmdrType) {
        this.rmdrType = rmdrType;
    }

    public String getRmdrDosage() {
        return rmdrDosage;
    }

    public void setRmdrDosage(String rmdrDosage) {
        this.rmdrDosage = rmdrDosage;
    }

    public String getRmdrTimings() {
        return rmdrTimings;
    }

    public void setRmdrTimings(String rmdrTimings) {
        this.rmdrTimings = rmdrTimings;
    }

    public String getRmdrDays() {
        return rmdrDays;
    }

    public void setRmdrDays(String rmdrDays) {
        this.rmdrDays = rmdrDays;
    }

    public String getRmdrDuration() {
        return rmdrDuration;
    }

    public void setRmdrDuration(String rmdrDuration) {
        this.rmdrDuration = rmdrDuration;
    }

}

