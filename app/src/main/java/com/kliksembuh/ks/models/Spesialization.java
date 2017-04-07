package com.kliksembuh.ks.models;

/**
 * Created by Trio Andianto on 4/6/2017.
 */

public class Spesialization {

    private String name;
    private String id;
    public Spesialization(String name, String id){
        this.name = name;
        this.id= id;
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
