package com.kliksembuh.ks;

/**
 * Created by Trio Andianto on 2/1/2017.
 */
public class Hospital {
    private int id;
    private int hospital_pic_id;
    private String name;
    private String address;


    // Constructor

    public Hospital(int id, int hospital_pic_id, String name, String address) {
        this.id = id;
        this.hospital_pic_id = hospital_pic_id;
        this.name = name;
        this.address = address;
    }


    // Getter, setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHospital_pic_id() {
        return hospital_pic_id;
    }

    public void setHospital_pic_id(int hospital_pic_id) {
        this.hospital_pic_id = hospital_pic_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}