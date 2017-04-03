package com.example.hi.irecord;

/**
 * Created by HI on 29-Mar-17.
 */


// This class is for the contacts whose call we are going to record

public class Contact {
    int id;
    String name;
    String number;

    public Contact() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
