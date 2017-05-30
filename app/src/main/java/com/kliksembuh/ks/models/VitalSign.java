package com.kliksembuh.ks.models;

/**
 * Created by Trio Andianto on 5/30/2017.
 */

public class VitalSign {


    private int id;
    private String tekananDarahS;
    private String tekananDarahD;
    private String denyutNadi;
    private String pernafasan;
    private String suhuTubuh;
    private String date;
    public VitalSign (int id, String tekananDarahS, String tekananDarahD, String denyutNadi, String pernafasan, String suhuTubuh, String date){
        this.id=id;
        this.tekananDarahS =tekananDarahS;
        this.tekananDarahD =tekananDarahD;
        this.denyutNadi =denyutNadi;
        this.pernafasan =pernafasan;
        this.suhuTubuh =suhuTubuh;
        this.date =date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTekananDarahS() {
        return tekananDarahS;
    }

    public void setTekananDarahS(String tekananDarahS) {
        this.tekananDarahS = tekananDarahS;
    }

    public String getTekananDarahD() {
        return tekananDarahD;
    }

    public void setTekananDarahD(String tekananDarahD) {
        this.tekananDarahD = tekananDarahD;
    }

    public String getDenyutNadi() {
        return denyutNadi;
    }

    public void setDenyutNadi(String denyutNadi) {
        this.denyutNadi = denyutNadi;
    }

    public String getPernafasan() {
        return pernafasan;
    }

    public void setPernafasan(String pernafasan) {
        this.pernafasan = pernafasan;
    }

    public String getSuhuTubuh() {
        return suhuTubuh;
    }

    public void setSuhuTubuh(String suhuTubuh) {
        this.suhuTubuh = suhuTubuh;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
