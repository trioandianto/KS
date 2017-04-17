package com.kliksembuh.ks.library;

/**
 * Created by Trio Andianto on 1/24/2017.
 */
public abstract class ListImage {
    private String movie;
    private String listName;
    private String alamat;
    private float rating;

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
