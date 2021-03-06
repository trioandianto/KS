package com.kliksembuh.ks.models;

/**
 * Created by Trio Andianto on 2/6/2017.
 */

public class Doctor {
    private String doc_id;
    private String doc_pic_id;
    private String frontTtlDoc;
    private String nameDoc;
    private String specialty;
    private String kualifikasi;
    private String imageUrl;
    private String tittle;



    // Constructor
    public Doctor(String doc_id, String doc_pic_id, String frontTtlDoc, String nameDoc, String specialty,
                  String imageUrl, String kualifikasi, String tittle) {
        this.doc_id = doc_id;
        this.doc_pic_id = doc_pic_id;
        this.frontTtlDoc = frontTtlDoc;
        this.nameDoc = nameDoc;
        this.specialty = specialty;
        this.imageUrl = imageUrl;
        this.kualifikasi = kualifikasi;
        this.tittle = tittle;
    }


    // Getter, Setter

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }
    public String getDoc_id() {
        return doc_id;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public String getDoc_pic_id() {
        return doc_pic_id;
    }

    public void setDoc_pic_id(String doc_pic_id) {
        this.doc_pic_id = doc_pic_id;
    }

    public String getFrontTtlDoc() {
        return frontTtlDoc;
    }

    public void setFrontTtlDoc(String frontTtlDoc) {
        this.frontTtlDoc = frontTtlDoc;
    }

    public String getNameDoc() {
        return nameDoc;
    }

    public void setNameDoc(String nameDoc) {
        this.nameDoc = nameDoc;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
    public String getKualifikasi() {
        return kualifikasi;
    }

    public void setKualifikasi(String kualifikasi) {
        this.kualifikasi = kualifikasi;
    }


}
