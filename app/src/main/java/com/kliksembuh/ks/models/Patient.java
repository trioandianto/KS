package com.kliksembuh.ks.models;

/**
 * Created by Ucu Nurul Ulum on 07/04/2017.
 */

public class Patient {
    private int pat_id;
    private double pat_medicalR;
    private String patStatus;
    private String firstName;
    private String lastName;
    private double patMobile;
    private boolean patGender;
    //To do, convert int / string to  Date Type
    //private Date patBirthday;
    private int patBirthday;
    private double noBPJSInsurance;
    private String patAddress;


    // Constructor
    public Patient(int pat_id, double pat_medicalR, String patStatus, String firstName, String lastName,
                   double patMobile, boolean patGender, int patBirthday, double noBPJSInsurance, String patAddress) {
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

    public double getPat_medicalR() {
        return pat_medicalR;
    }

    public void setPat_medicalR(double pat_medicalR) {
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

    public double getPatMobile() {
        return patMobile;
    }

    public void setPatMobile(int patMobile) {
        this.patMobile = patMobile;
    }

    public boolean isPatGender() {
        return patGender;
    }

    public void setPatGender(boolean patGender) {
        this.patGender = patGender;
    }

    public int getPatBirthday() {
        return patBirthday;
    }

    public void setPatBirthday(int patBirthday) {
        this.patBirthday = patBirthday;
    }

    public double getNoBPJSInsurance() {
        return noBPJSInsurance;
    }

    public void setNoBPJSInsurance(int noBPJSInsurance) {
        this.noBPJSInsurance = noBPJSInsurance;
    }

    public String getPatAddress() {
        return patAddress;
    }

    public void setPatAddress(String patAddress) {
        this.patAddress = patAddress;
    }
}
