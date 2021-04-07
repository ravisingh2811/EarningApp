package com.example.realcash.model;

public class ProfileModel {
    private String name , email , images ;
    private int coins , spins;




    public ProfileModel(String name, String email, String images, int coins , int spins) {
        this.name = name;
        this.email = email;
        this.coins = coins;
        this.images = images;
        this.spins = spins;

    }
    public ProfileModel(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getSpins() {
        return spins;
    }

    public void setSpins(int spins) {
        this.spins = spins;
    }
}
