package com.example.realcash.model;

public class Phonepe {

    private  String id , phonepeCode;

    public Phonepe(){

    }

    public Phonepe(String id, String phonepeCode) {
        this.id = id;
        this.phonepeCode = phonepeCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhonepeCode() {
        return phonepeCode;
    }

    public void setPhonepeCode(String phonepeCode) {
        this.phonepeCode = phonepeCode;
    }
}


