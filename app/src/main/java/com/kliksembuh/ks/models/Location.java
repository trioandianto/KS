package com.kliksembuh.ks.models;

/**
 * Created by Trio Andianto on 3/8/2017.
 */

public class Location {
    private String name;
    private String id;
    public Location( String name, String code) {

        this.name = name;
        this.id = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
