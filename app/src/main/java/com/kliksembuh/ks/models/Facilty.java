package com.kliksembuh.ks.models;

/**
 * Created by Trio Andianto on 5/24/2017.
 */

public class Facilty {


    private int id;
    private String nama;

    public Facilty(int id, String nama){
        this.id=id;
        this.nama=nama;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
