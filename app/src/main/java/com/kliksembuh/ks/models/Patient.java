package com.kliksembuh.ks.models;

/**
 * Created by Ucu Nurul Ulum on 07/04/2017.
 */

public class Patient {
    private int pat_id;
    private String pat_medicalR;
    private String patStatus;
    private String firstName;
    private String lastName;
    private String  patMobile;
    private String patGender;
    //To do, convert int / string to  Date Type
    //private Date patBirthday;
    private String patBirthday;
    private String noBPJSInsurance;
    private String patAddress;


    // Constructor
    public Patient(int pat_id, String pat_medicalR, String patStatus, String firstName, String lastName,
                   String patMobile, String patGender, String patBirthday, String noBPJSInsurance, String patAddress) {
        this.pat_id = pat_id;
        this.pat_medicalR = pat_medicalR;
        this.patStatus = patStatus;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patMobile = patMobile;
        this.patGender = patGender;
        this.patBirthday = patBirthday;
        this.noBPJSInsurance = noBPJSInsurance;
        this.patAddress = patAddress;
    }

    public int getPat_id() {
        return pat_id;
    }

    public void setPat_id(int pat_id) {
        this.pat_id = pat_id;
    }

    public String getPat_medicalR() {
        return pat_medicalR;
    }

    public void setPat_medicalR(String pat_medicalR) {
        this.pat_medicalR = pat_medicalR;
    }

    public String getPatStatus() {
        return patStatus;
    }

    public void setPatStatus(String patStatus) {
        this.patStatus = patStatus;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatMobile() {
        return patMobile;
    }

    public void setPatMobile(String patMobile) {
        this.patMobile = patMobile;
    }

    public String getPatGender() {
        return patGender;
    }

    public void setPatGender(String patGender) {
        this.patGender = patGender;
    }

    public String getPatBirthday() {
        return patBirthday;
    }

    public void setPatBirthday(String patBirthday) {
        this.patBirthday = patBirthday;
    }

    public String getNoBPJSInsurance() {
        return noBPJSInsurance;
    }

    public void setNoBPJSInsurance(String noBPJSInsurance) {
        this.noBPJSInsurance = noBPJSInsurance;
    }

    public String getPatAddress() {
        return patAddress;
    }

    public void setPatAddress(String patAddress) {
        this.patAddress = patAddress;
    }
}
