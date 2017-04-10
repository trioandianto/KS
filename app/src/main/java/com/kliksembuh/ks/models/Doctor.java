package com.kliksembuh.ks.models;

import android.graphics.drawable.Drawable;

/**
 * Created by Trio Andianto on 2/6/2017.
 */

public class Doctor {
    private String doc_id;
    private Drawable doc_pic_id;
    private String nameDoc;
    private String specialty;

    // Constructor
    public Doctor(String doc_id, Drawable doc_pic_id, String nameDoc, String specialty) {
        this.doc_id = doc_id;
        this.doc_pic_id = doc_pic_id;
        this.nameDoc = nameDoc;
        this.specialty = specialty;
    }

    // Getter, Setter

    public String getDoc_id() {
        return doc_id;
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
