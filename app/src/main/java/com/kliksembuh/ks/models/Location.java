package com.kliksembuh.ks.models;

/**
 * Created by Trio Andianto on 3/8/2017.
 */

public class Location {
    private String name;
    private String id;
    private String code;


    public Location( String name, String id, String code) {

        this.name = name;
        this.id = id;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
