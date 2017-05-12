package com.kliksembuh.ks.models;

/**
 * Created by Ucu Nurul Ulum on 07/04/2017.
 */

public class Patient {
    private int pat_id;
    private String patCloseRelativeName;
    private String patCloseRelativePhoneNbr;
    private String patRelativeStatus;
    private String patFirstName;
    private String patLastName;
    private String patCellPhoneNbr;
    private String patGender;
    private String patAddress;
    //To do, convert int / string to  Date Type
    //private Date patBirthday;
    private String patBirthday;
    private String patBPJSNbr;



    // Constructor
    public Patient(int pat_id, String patCloseRelativeName, String patStatus, String firstName, String patLastName,
                   String patMobile, String patGender, String patBirthday, String noBPJSInsurance, String patAddress, String patCloseRelativePhoneNbr) {
        this.pat_id = pat_id;
        this.patCloseRelativeName = patCloseRelativeName;
        this.patRelativeStatus = patStatus;
        this.patFirstName = firstName;
        this.patLastName = patLastName;
        this.patCellPhoneNbr = patMobile;
        this.patGender = patGender;
        this.patBirthday = patBirthday;
        this.patBPJSNbr = noBPJSInsurance;
        this.patAddress = patAddress;
        this.patCloseRelativePhoneNbr = patCloseRelativePhoneNbr;
    }

    public int getPat_id() {
        return pat_id;
    }

    public void setPat_id(int pat_id) {
        this.pat_id = pat_id;
    }

    public String getPatCloseRelativeName() {
        return patCloseRelativeName;
    }

    public void setPatCloseRelativeName(String patCloseRelativeName) {
        this.patCloseRelativeName = patCloseRelativeName;
    }

    public String getPatRelativeStatus() {
        return patRelativeStatus;
    }

    public void setPatRelativeStatus(String patRelativeStatus) {
        this.patRelativeStatus = patRelativeStatus;
    }

    public String getPatFirstName() {
        return patFirstName;
    }

    public void setPatFirstName(String patFirstName) {
        this.patFirstName = patFirstName;
    }

    public String getPatLastName() {
        return patLastName;
    }

    public void setPatLastName(String patLastName) {
        this.patLastName = patLastName;
    }

    public String getPatCellPhoneNbr() {
        return patCellPhoneNbr;
    }

    public void setPatCellPhoneNbr(String patCellPhoneNbr) {
        this.patCellPhoneNbr = patCellPhoneNbr;
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

    public String getPatBPJSNbr() {
        return patBPJSNbr;
    }

    public void setPatBPJSNbr(String patBPJSNbr) {
        this.patBPJSNbr = patBPJSNbr;
    }

    public String getPatAddress() {
        return patAddress;
    }

    public void setPatAddress(String patAddress) {
        this.patAddress = patAddress;
    }
    public String getPatCloseRelativePhoneNbr() {
        return patCloseRelativePhoneNbr;
    }

    public void setPatCloseRelativePhoneNbr(String patCloseRelativePhoneNbr) {
        this.patCloseRelativePhoneNbr = patCloseRelativePhoneNbr;
    }
}
