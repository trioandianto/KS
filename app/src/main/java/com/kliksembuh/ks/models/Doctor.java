package com.kliksembuh.ks.models;

/**
 * Created by Trio Andianto on 2/6/2017.
 */

public class Doctor {
    private int doc_id;
    private int doc_pic_id;
    private String nameDoc;
    private String specialty;

    // Constructor
    public Doctor(int doc_id, int doc_pic_id, String nameDoc, String specialty) {
        this.doc_id = doc_id;
        this.doc_pic_id = doc_pic_id;
        this.nameDoc = nameDoc;
        this.specialty = specialty;
    }

    // Getter, Setter

    public int getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(int doc_id) {
        this.doc_id = doc_id;
    }

    public int getDoc_pic_id() {
        return doc_pic_id;
    }

    public void setDoc_pic_id(int doc_pic_id) {
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
