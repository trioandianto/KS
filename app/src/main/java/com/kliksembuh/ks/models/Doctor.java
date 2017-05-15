package com.kliksembuh.ks.models;

import android.graphics.drawable.Drawable;

/**
 * Created by Trio Andianto on 2/6/2017.
 */

public class Doctor {
    private String doc_id;
    private Drawable doc_pic_id;
    private String frontTtlDoc;
    private String nameDoc;
    private String specialty;




    private String imageUrl;

    // Constructor
    public Doctor(String doc_id, Drawable doc_pic_id, String frontTtlDoc, String nameDoc, String specialty, String imageUrl) {
        this.doc_id = doc_id;
        this.doc_pic_id = doc_pic_id;
        this.frontTtlDoc = frontTtlDoc;
        this.nameDoc = nameDoc;
        this.specialty = specialty;
        this.imageUrl = imageUrl;
    }


    // Getter, Setter

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

    public Drawable getDoc_pic_id() {
        return doc_pic_id;
    }

    public void setDoc_pic_id(Drawable doc_pic_id) {
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
}
