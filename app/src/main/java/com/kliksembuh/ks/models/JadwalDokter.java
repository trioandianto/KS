package com.kliksembuh.ks.models;

/**
 * Created by Trio Andianto on 3/29/2017.
 */

public class JadwalDokter {

    private String jadwalDokter;
    private String dayProdramID;
    private String weekProgramID;
    private String detailProgramID;

    public JadwalDokter(String jadwalDokter, String dayProdramID, String weekProgramID, String detailProgramID){
        this.jadwalDokter = jadwalDokter;
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


    public String getJadwalDokter() {
        return jadwalDokter;
    }

    public void setJadwalDokter(String jadwalDokter) {
        this.jadwalDokter = jadwalDokter;
    }




}
