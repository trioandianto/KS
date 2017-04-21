package com.kliksembuh.ks.models;

/**
 * Created by Trio Andianto on 4/19/2017.
 */

public class PraktekDokter {
    private String id;
    private String namaRumahSakit;
    public PraktekDokter(String id, String namaRumahSakit){
        this.id=id;
        this.namaRumahSakit = namaRumahSakit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaRumahSakit() {
        return namaRumahSakit;
    }

    public void setNamaRumahSakit(String namaRumahSakit) {
        this.namaRumahSakit = namaRumahSakit;
    }
}
