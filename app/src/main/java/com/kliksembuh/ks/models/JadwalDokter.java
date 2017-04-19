package com.kliksembuh.ks.models;

/**
 * Created by Trio Andianto on 3/29/2017.
 */

public class JadwalDokter {


    private String dayProdramID;
    private String weekProgramID;
    private String detailProgramID;
    private String startDate;
    private String endDate;

    public JadwalDokter(String startDate, String endDate, String dayProdramID, String weekProgramID, String detailProgramID ){
        this.startDate = startDate;
        this.endDate = endDate;
        this.dayProdramID = dayProdramID;
        this.weekProgramID = weekProgramID;
        this.detailProgramID = detailProgramID;

    }

    public String getDayProdramID() {
        return dayProdramID;
    }

    public void setDayProdramID(String dayProdramID) {
        this.dayProdramID = dayProdramID;
    }

    public String getWeekProgramID() {
        return weekProgramID;
    }

    public void setWeekProgramID(String weekProgramID) {
        this.weekProgramID = weekProgramID;
    }

    public String getDetailProgramID() {
        return detailProgramID;
    }

    public void setDetailProgramID(String detailProgramID) {
        this.detailProgramID = detailProgramID;
    }
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }





}
