package com.example.riyaza.movieappstage2.module;

public class Trailer {
    private  String id;
    private String name;
    private String key;

    public Trailer(String id, String name, String key){
        this.name = name;
        this.key = key;
        this.id=id;

    }

    public String getname() {

        return name;
    }

    public String getId() {
        return id;
    }

    public String getkey() {

        return key;
    }
}
